package com.dillon.weddingrsvpapi.controller

import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.util.ApiError
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.http.HttpStatus


import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RsvpControllerSpec extends Specification {

    @Autowired
    RsvpRepository rsvpRepository

    @LocalServerPort
    private int port

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper

    def cleanup() {
        rsvpRepository.deleteAll()
    }

    def 'When a rsvps are updated, it returns HttpStatus.OK'() {
        setup:
        def rsvpOne = Rsvp.builder()
            .id(1)
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvpOne)

        def rsvpTwo = Rsvp.builder()
            .id(2)
            .attending(false)
            .name("Jane Doe").build()
        rsvpRepository.save(rsvpTwo)

        def rsvpList = [
            ["id": 1, "attending": true],
            ["id": 2, "attending": true]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvps",
            rsvpList, String)

        and:
        Optional<Rsvp> retRsvpOne = rsvpRepository.findById(1)
        Optional<Rsvp> retRsvpTwo = rsvpRepository.findById(2)

        then:
        result.statusCode == HttpStatus.OK
        retRsvpOne.get().attending == true
        retRsvpTwo.get().attending == true
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
            it.message == "Rsvp with id 1 was not found"
            it.errors.isEmpty()
        }
    }

    def 'When a rsvp is updated with an invalid request, it returns HttpStatus.BAD_REQUEST'() {
        given:
        def rsvp = Rsvp.builder()
            .id(1)
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        def rsvpList = [
            ["attending": true],
            ["attending": true]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvps",
            rsvpList, String)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.BAD_REQUEST
            it.message == "Request was invalid"
            it.errors == [
                "updateRsvp.rsvps[0].id": "Id is mandatory.",
                "updateRsvp.rsvps[1].id": "Id is mandatory."
            ]
        }
    }

    def 'When a rsvp is queried that does not exist, it returns HttpStatus.NOT_FOUND'() {
        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvps/2", String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        with(objectMapper.readValue(result.body, ApiError)) {
            it.status == HttpStatus.NOT_FOUND
            it.message == "Rsvp with id 2 was not found"
            it.errors.isEmpty()
        }
    }

    def 'When a rsvp is queried, it returns HttpStatus.OK'() {
        setup:
        def rsvp = Rsvp.builder()
            .id(1)
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        when:
        def result = restTemplate.getForEntity("http://localhost:${port}/api/rsvps/1", String)

        then:
        result.statusCode == HttpStatus.OK
    }
}
