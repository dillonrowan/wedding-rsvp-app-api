package com.dillon.weddingrsvpapi.db

import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@DataJpaTest
class RsvpRepositorySpec extends Specification {

    @Autowired
    RsvpRepository rsvpRepository

    def 'When an invalid passcode is provided, no result is returned'() {
        given:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        expect:
        rsvpRepository.findByPasscode("bbbbb").isEmpty()
    }

    def 'When a valid passcode is provided, the correct result is returned'() {
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
        List<Rsvp> rsvps = rsvpRepository.findByPasscode("abcde")

        then:
        rsvps.size() == 1
        rsvps.get(0).passcode == "abcde"
        rsvps.get(0).dietaryRestrictions == List.of( DietaryRestriction.NO_PORK )
        rsvps.get(0).foodAllergies == List.of( FoodAllergies.DAIRY )
        rsvps.get(0).email == "test@test.com"
        !rsvps.get(0).attending
        rsvps.get(0).name == "John Smith"
    }

    def 'When a valid passcode is provided, the correct result is returned'() {
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
        List<Rsvp> rsvps = rsvpRepository.findByPasscode("abcde")

        then:
        rsvps.size() == 1
        rsvps.get(0).passcode == "abcde"
        rsvps.get(0).dietaryRestrictions == List.of( DietaryRestriction.NO_PORK )
        rsvps.get(0).foodAllergies == List.of( FoodAllergies.DAIRY )
        rsvps.get(0).email == "test@test.com"
        !rsvps.get(0).attending
        rsvps.get(0).name == "John Smith"
    }
}
