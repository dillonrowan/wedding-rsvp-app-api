package com.dillon.weddingrsvpapi.controller

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository
import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.dillon.weddingrsvpapi.dto.RsvpGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import spock.lang.Specification

class RsvpGroupControllerSpec extends Specification {

    @Autowired
    RsvpGroupRepository rsvpGroupRepository

    @LocalServerPort
    private int port

    @Autowired
    TestRestTemplate restTemplate;

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

    def 'When an rsvp that is not saved is updated, it returns HttpStatus.CONFLICT'() {
        setup:
        def rsvpList = [
            ["id": 1, "attending": true],
            ["id": 2, "attending": true]
        ]

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/api/update-rsvps",
            rsvpList, String)

        then:
        result.statusCode == HttpStatus.CONFLICT
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
        result.getBody().contains("Id is mandatory.")
    }

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
