package com.dillon.weddingrsvpapi.dto

import jakarta.validation.Validation
import spock.lang.Specification

class RsvpSpec extends Specification {
    def validator

    def setup() {
        def factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    def 'Getters work appropriately on the Rsvp class'() {
        setup:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .attending(false)
            .accompanyingGuests(1)
            .name("John Smith").build()

        expect:
        rsvp.passcode == "abcde"
        rsvp.dietaryRestrictions == List.of( DietaryRestriction.NO_PORK )
        rsvp.foodAllergies == List.of( FoodAllergies.DAIRY )
        rsvp.email == "test@test.com"
        !rsvp.attending
        rsvp.name == "John Smith"
    }

    def 'Bad inputs causes validation to fail'() {
        setup:
        def rsvp = Rsvp.builder()
            .passcode(null).build()

        when:
        def violations = validator.validate(rsvp)

        then:
        violations.size() == 1
    }

    def 'Good inputs causes no validation failures'() {
        setup:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .attending(false)
            .accompanyingGuests(1)
            .name("John Smith").build()

        when:
        def violations = validator.validate(rsvp)

        then:
        violations.size() == 0
    }
}