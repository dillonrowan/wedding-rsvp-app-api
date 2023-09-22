package com.dillon.weddingrsvpapi.controller

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository
import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.dillon.weddingrsvpapi.dto.RsvpGroup
import com.dillon.weddingrsvpapi.util.ApiError
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RsvpGroupControllerSpec extends Specification {

    @Autowired
    RsvpGroupRepository rsvpGroupRepository

    @LocalServerPort
    private int port

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper

    def cleanup() {
        rsvpGroupRepository.deleteAll()
    }

    def 'When rsvp group are updated, it returns HttpStatus.OK'() {
        setup:
        def rsvpGroupOne = RsvpGroup.builder()
            .id(1)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .modifyGroup(false)
            .groupLead("John Smith").build()
        rsvpGroupRepository.save(rsvpGroupOne)

        def rsvpGroupTwo = RsvpGroup.builder()
            .id(2)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .modifyGroup(false)
            .groupLead("Jane Doe").build()
        rsvpGroupRepository.save(rsvpGroupTwo)

        def rsvpGroupList = [
            ["id": 1, "dietaryRestrictions": ["NO_PORK", "NO_FISH"]],
            ["id": 2,"dietaryRestrictions": ["NO_PORK", "NO_FISH"]]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvp-groups",
            rsvpGroupList, String)

        and:
        Optional<RsvpGroup> retRsvpGroupOne = rsvpGroupRepository.findById(1)
        Optional<RsvpGroup> retRsvpGroupTwo = rsvpGroupRepository.findById(2)

        then:
        result.statusCode == HttpStatus.OK
        retRsvpGroupOne.get().dietaryRestrictions == [DietaryRestriction.NO_PORK, DietaryRestriction.NO_FISH]
        retRsvpGroupTwo.get().dietaryRestrictions == [DietaryRestriction.NO_PORK, DietaryRestriction.NO_FISH]
    }

    def 'When an rsvp that is not saved is updated, it returns HttpStatus.NOT_FOUND'() {
        setup:
        def rsvpGroupList = [
            ["id": 1, "dietaryRestrictions": ["NO_PORK", "NO_FISH"]],
            ["id": 2,"dietaryRestrictions": ["NO_PORK", "NO_FISH"]]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvp-groups",
            rsvpGroupList, String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.NOT_FOUND
            it.message == "Rsvp group with id 1 was not found"
            it.errors.isEmpty()
        }
    }

    def 'When a rsvp is updated with an invalid request, it returns HttpStatus.BAD_REQUEST'() {
        given:
        def rsvpGroupList = [
            ["dietaryRestrictions": ["NO_PORK", "NO_FISH"]],
            ["dietaryRestrictions": ["NO_PORK", "NO_FISH"]]
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
                "updateRsvpGroups.rsvpGroups[0].id": "Id is mandatory.",
                "updateRsvpGroups.rsvpGroups[1].id": "Id is mandatory."
            ]
        }
    }

    //TODO: add tests for every other controller methods, such as search by name

    def 'When a rsvp is queried, it returns HttpStatus.OK'() {
        given:
        def rsvp = Rsvp.builder()
            .id(1)
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvp/1", String)

        then:
        result.statusCode == HttpStatus.OK
    }

    def 'When a rsvp is queried that does not exist, it returns HttpStatus.NOT_FOUND'() {
        given:
        def rsvp = Rsvp.builder()
            .id(1)
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvp/2", String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
    }
}
