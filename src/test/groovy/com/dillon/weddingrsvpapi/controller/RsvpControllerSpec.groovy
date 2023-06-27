package com.dillon.weddingrsvpapi.controller

import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.http.HttpStatus


import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RsvpControllerSpec extends Specification{

    @Autowired
    RsvpRepository rsvpRepository

    @LocalServerPort
    private int port

    @Autowired
    TestRestTemplate restTemplate;

    def cleanup() {
        rsvpRepository.deleteAll()
    }

    def 'When a rsvp is updated, it returns HttpStatus.OK'() {
        given:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/update-rsvp", [
            "passcode"  : "abcde",
            "attending" : true
        ], String)

        then:
        result.statusCode == HttpStatus.OK
    }

    def 'When an rsvp that is not saved is updated, it returns HttpStatus.CONFLICT'() {
        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/update-rsvp", [
            "passcode" : "abcde"
        ], String)

        then:
        result.statusCode == HttpStatus.CONFLICT
    }

    def 'When a rsvp is updated with an invalid request, it returns HttpStatus.BAD_REQUEST'() {
        given:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/update-rsvp", [
            "attending" : true,
        ], String)

        then:
        result.getBody().contains("Passcode is mandatory.")
        result.statusCode == HttpStatus.BAD_REQUEST
    }

    def 'When a rsvp is queried, it returns HttpStatus.OK'() {
        given:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/rsvp", [
            "passcode"  : "abcde"
        ], String)

        then:
        result.statusCode == HttpStatus.OK
    }

    def 'When a rsvp is queried that does not exist, it returns HttpStatus.NOT_FOUND'() {
        given:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/rsvp", [
            "passcode"  : "bbbbb"
        ], String)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
    }
}
