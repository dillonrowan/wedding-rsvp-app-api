package com.dillon.weddingrsvpapi.dto

import jakarta.validation.Validation
import spock.lang.Specification

class RsvpGroupSpec extends Specification {
    def validator

    def setup() {
        def factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    def 'Getters work appropriately on the Rsvp group class'() {
        setup:
        def rsvp = Rsvp.builder()
            .id(1)
            .attending(false)
            .name("John Smith").build()

        def rsvpGroup = RsvpGroup.builder()
            .id(1)
            .rsvps(Set.of(rsvp))
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .modifyGroup(true)
            .groupLead("John Smith").build()

        expect:
        rsvpGroup.id == 1
        rsvpGroup.rsvps == Set.of(rsvp)
        rsvpGroup.modifyGroup
        rsvpGroup.dietaryRestrictions == List.of( DietaryRestriction.NO_PORK )
        rsvpGroup.foodAllergies == List.of( FoodAllergies.DAIRY )
        rsvpGroup.email == "test@test.com"
        rsvpGroup.groupLead == "John Smith"
    }

    def 'Good inputs causes no validation failures'() {
        setup:
        def rsvp = Rsvp.builder()
            .id(1)
            .attending(false)
            .name("John Smith").build()

        def rsvpGroup = RsvpGroup.builder()
            .id(1)
            .rsvps(Set.of(rsvp))
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .modifyGroup(true)
            .groupLead("John Smith").build()

        when:
        def violations = validator.validate(rsvpGroup)

        then:
        violations.size() == 0
    }
}
