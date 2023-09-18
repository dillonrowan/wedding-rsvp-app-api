package com.dillon.weddingrsvpapi.db

import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.dillon.weddingrsvpapi.dto.RsvpGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@DataJpaTest
class RsvpGroupRepositorySpec extends Specification {

    @Autowired
    RsvpGroupRepository rsvpGroupRepository

    @Autowired
    RsvpRepository rsvpRepository

    def 'When an invalid id is provided, no result is returned'() {
        given:
        def rsvp = Rsvp.builder()
            .id(1)
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        def rsvpGroup = RsvpGroup.builder()
            .id(1)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .modifyGroup(false)
            .rsvps(Set.of(rsvp))
            .groupLead("John Smith").build()
        rsvpGroupRepository.save(rsvpGroup)

        expect:
        rsvpGroupRepository.findById(2).isEmpty()
    }

    def 'When a valid id is provided, the correct result is returned'() {
        given:
        def rsvpGroup = RsvpGroup.builder()
            .id(1)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .modifyGroup(false)
            .groupLead("John Smith").build()
        rsvpGroupRepository.save(rsvpGroup)

        when:
        Optional<RsvpGroup> retRsvpGroup = rsvpGroupRepository.findById(1)

        then:
        retRsvpGroup.get().id == 1
        retRsvpGroup.get().groupLead == "John Smith"
        retRsvpGroup.get().dietaryRestrictions == List.of( DietaryRestriction.NO_PORK )
        retRsvpGroup.get().foodAllergies == List.of( FoodAllergies.DAIRY )
        retRsvpGroup.get().email == "test@test.com"
        !retRsvpGroup.get().modifyGroup
    }
}
