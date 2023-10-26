package com.dillon.weddingrsvpapi.controller

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository
import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.dillon.weddingrsvpapi.dto.RsvpGroup
import com.dillon.weddingrsvpapi.util.ApiError
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RsvpGroupControllerSpec extends Specification {

    @Autowired
    RsvpRepository rsvpRepository

    @Autowired
    RsvpGroupRepository rsvpGroupRepository

    @LocalServerPort
    private int port

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper

    def setup() {
        restTemplate.getRestTemplate().setInterceptors(
            Collections.singletonList((request, body, execution) -> {
                request.getHeaders().add("x-api-key", "secret");
                return execution.execute(request, body);
            } as ClientHttpRequestInterceptor)
        )
    }

    def cleanup() {
        rsvpRepository.deleteAll()
        rsvpGroupRepository.deleteAll()
    }

    // Test /update-rsvp-groups
    def 'When rsvp groups are updated, it returns HttpStatus.OK'() {
        setup:
        def rsvpGroupOne = RsvpGroup.builder()
            .email("test@test.com")
            .modifyGroup(false)
            .groupLead("John Smith").build()
        def rsvpGroupOneSaved = rsvpGroupRepository.save(rsvpGroupOne)

        def rsvpGroupTwo = RsvpGroup.builder()
            .email("test@test.com")
            .modifyGroup(false)
            .groupLead("Jane Doe").build()
        def rsvpGroupTwoSaved = rsvpGroupRepository.save(rsvpGroupTwo)

        def rsvpGroupList = [
            ["id": rsvpGroupOneSaved.id, "email": "success@test.com"],
            ["id": rsvpGroupTwoSaved.id,"email": "success@test.com"]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvp-groups",
            rsvpGroupList, String)

        and:
        Optional<RsvpGroup> retRsvpGroupOne = rsvpGroupRepository.findById(rsvpGroupOneSaved.id)
        Optional<RsvpGroup> retRsvpGroupTwo = rsvpGroupRepository.findById(rsvpGroupTwoSaved.id)

        then:
        result.statusCode == HttpStatus.OK
        retRsvpGroupOne.get().email == "success@test.com"
        retRsvpGroupTwo.get().email == "success@test.com"
    }

    def 'When an rsvp group that is not saved is updated, it returns HttpStatus.NOT_FOUND'() {
        setup:
        def rsvpGroupList = [
            ["id": 1, "email": "success@test.com"],
            ["id": 2, "email": "success@test.com"]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvp-groups",
            rsvpGroupList, String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.NOT_FOUND
            it.message == "Rsvp group with id 1 was not found."
            it.errors.isEmpty()
        }
    }

    def 'When a rsvp is updated with an invalid request, it returns HttpStatus.BAD_REQUEST'() {
        given:
        def rsvpGroupList = [
            ["modifyGroup": true],
            ["modifyGroup": true]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvp-groups",
            rsvpGroupList, String)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.BAD_REQUEST
            it.message == "Request was invalid"
            it.errors == [
                "updateRsvpGroupEmails.rsvpGroups[0].id": "Id is mandatory.",
                "updateRsvpGroupEmails.rsvpGroups[1].id": "Id is mandatory."
            ]
        }
    }

    // Test /rsvp-groups
    def 'When a rsvp group is queried by id, it returns HttpStatus.OK'() {
        given:
        def rsvpGroup = RsvpGroup.builder()
            .modifyGroup(true)
            .groupLead("John Smith").build()
        def rsvpGroupRepositorySaved = rsvpGroupRepository.save(rsvpGroup)

        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvp-groups/${rsvpGroupRepositorySaved.id}", String)

        then:
        result.statusCode == HttpStatus.OK
    }

    def 'When a rsvp group is queried by id that does not exist, it returns HttpStatus.NOT_FOUND'() {
        given:
        def rsvpGroup = RsvpGroup.builder()
            .id(1)
            .modifyGroup(true)
            .groupLead("John Smith").build()
        rsvpGroupRepository.save(rsvpGroup)

        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvp-groups/2", String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.NOT_FOUND
            it.message == "Rsvp group with id 2 was not found."
            it.errors.isEmpty()
        }
    }

    def 'When a rsvp group is queried by id with an invalid id, it returns HttpStatus.BAD_REQUEST'() {
        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvp-groups/john", String)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.BAD_REQUEST
            it.message == "Invalid argument in request."
            it.errors == [
                "id": "Failed to convert value of type 'java.lang.String' to required type 'long'; For input string: \"john\""
            ]
        }
    }

    // Test rsvp-groups-by-name
    def 'When a rsvp group is queried by its name that exists with members, it returns HttpStatus.OK'() {
        given:
        def rsvp = Rsvp.builder()
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        def rsvpGroup = RsvpGroup.builder()
            .modifyGroup(true)
            .rsvps(Set.of(rsvp))
            .groupLead("John Smith").build()
        rsvpGroupRepository.save(rsvpGroup)

        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvp-groups-by-name/john", String)

        then:
        result.statusCode == HttpStatus.OK
    }

    def 'When a rsvp group is queried by its name that exists no members, it returns HttpStatus.NOT_FOUND'() {
        given:
        def rsvpGroup = RsvpGroup.builder()
            .modifyGroup(true)
            .groupLead("John Smith").build()
        rsvpGroupRepository.save(rsvpGroup)

        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvp-groups-by-name/john", String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.NOT_FOUND
            it.message == "Could not find rsvp groups that had any members with similar name."
            it.errors.isEmpty()
        }
    }

    def 'When a rsvp group is queried by its name that exists no matching members, it returns HttpStatus.NOT_FOUND'() {
        given:
        def rsvpOne = Rsvp.builder()
            .attending(false)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .name("John Smith").build()
        rsvpRepository.save(rsvpOne)

        def rsvpTwo = Rsvp.builder()
            .attending(false)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .name("Jane Doe").build()
        rsvpRepository.save(rsvpTwo)

        def rsvpGroup = RsvpGroup.builder()
            .rsvps(Set.of(rsvpOne, rsvpTwo))
            .modifyGroup(true)
            .groupLead("John Smith").build()
        rsvpGroupRepository.save(rsvpGroup)

        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvp-groups-by-name/bob", String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.NOT_FOUND
            it.message == "Could not find rsvp groups that had any members with similar name."
            it.errors.isEmpty()
        }
    }
}
