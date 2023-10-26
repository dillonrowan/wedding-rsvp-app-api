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
        def rsvpGroup = RsvpGroup.builder()
            .id(1)
            .email("test@test.com")
            .modifyGroup(true)
            .groupLead("John Smith").build()

        def rsvp = Rsvp.builder()
            .id(1)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .rsvpGroup(rsvpGroup)
            .attending(false)
            .name("John Smith").build()

        expect:
        rsvp.id == 1
        rsvp.rsvpGroup == rsvpGroup
        !rsvp.attending
        rsvp.name == "John Smith"
    }

    def 'Good inputs causes no validation failures'() {
        setup:
        def rsvpGroup = RsvpGroup.builder()
            .id(1)
            .email("test@test.com")
            .modifyGroup(true)
            .groupLead("John Smith").build()

        def rsvp = Rsvp.builder()
            .id(1)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .rsvpGroup(rsvpGroup)
            .attending(false)
            .name("John Smith").build()

        when:
        def violations = validator.validate(rsvp)

        then:
        violations.size() == 0
    }
}