package com.dillon.weddingrsvpapi.controller

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository
import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.RsvpGroup
import com.dillon.weddingrsvpapi.util.ApiError
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.test.context.ActiveProfiles
import org.springframework.http.HttpStatus

import spock.lang.Specification


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RsvpControllerSpec extends Specification {

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
    }

    // Test /update-rsvps
    def 'When a rsvps are updated, it returns HttpStatus.OK'() {
        setup:
        def rsvpOne = Rsvp.builder()
            .attending(false)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .name("John Smith").build()
        def rsvpOneSaved = rsvpRepository.save(rsvpOne)

        def rsvpTwo = Rsvp.builder()
            .attending(false)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .name("Jane Doe").build()
        def rsvpTwoSaved = rsvpRepository.save(rsvpTwo)

        def rsvpList = [
            ["id": rsvpOneSaved.id, "attending": true, "dietaryRestrictions": ["NO_FISH"], "foodAllergies": ["FISH"]],
            ["id": rsvpTwoSaved.id, "attending": true, "dietaryRestrictions": ["NO_FISH"], "foodAllergies": ["FISH"]]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvps",
            rsvpList, String)

        and:
        Optional<Rsvp> retRsvpOne = rsvpRepository.findById(rsvpOneSaved.id)
        Optional<Rsvp> retRsvpTwo = rsvpRepository.findById(rsvpTwoSaved.id)

        then:
        result.statusCode == HttpStatus.OK
        retRsvpOne.get().attending == true
        retRsvpOne.get().dietaryRestrictions == [DietaryRestriction.NO_FISH]
        retRsvpOne.get().foodAllergies == [FoodAllergies.FISH]
        retRsvpTwo.get().attending == true
        retRsvpTwo.get().dietaryRestrictions == [DietaryRestriction.NO_FISH]
        retRsvpTwo.get().foodAllergies == [FoodAllergies.FISH]
    }

    def 'When an rsvp that is not saved is updated, it returns HttpStatus.NOT_FOUND'() {
        setup:
        def rsvpList = [
            ["id": 1, "attending": true],
            ["id": 2, "attending": true]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvps",
            rsvpList, String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.NOT_FOUND
            it.message == "Rsvp with id 1 was not found."
            it.errors.isEmpty()
        }
    }

    // Test /rsvps
    def 'When a rsvp is queried, it returns HttpStatus.OK'() {
        setup:
        def rsvp = Rsvp.builder()
            .attending(false)
            .name("John Smith").build()
        def savedRsvp = rsvpRepository.save(rsvp)

        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvps/${savedRsvp.id}", String)

        then:
        result.statusCode == HttpStatus.OK
    }

    def 'When a rsvp is queried that does not exist, it returns HttpStatus.NOT_FOUND'() {
        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvps/2", String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.NOT_FOUND
            it.message == "Rsvp with id 2 was not found."
            it.errors.isEmpty()
        }
    }

    def 'When a rsvp is queried by id with an invalid id, it returns HttpStatus.BAD_REQUEST'() {
        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvps/john", String)

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
}
