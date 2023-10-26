package com.dillon.weddingrsvpapi.db

import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.dillon.weddingrsvpapi.dto.RsvpGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RsvpGroupRepositorySpec extends Specification {

    @Autowired
    RsvpGroupRepository rsvpGroupRepository

    @Autowired
    RsvpRepository rsvpRepository

    def 'When a valid name is provided, the group and its members with that member name is returned'() {
        given:
        // expected group and members to return
        def rsvp = Rsvp.builder()
            .id(1)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)
        def rsvpTwo = Rsvp.builder()
            .id(2)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .attending(false)
            .name("Jane Doe").build()
        rsvpRepository.save(rsvpTwo)

        def rsvpGroup = RsvpGroup.builder()
            .id(1)
            .email("test@test.com")
            .modifyGroup(false)
            .rsvps(Set.of(rsvp, rsvpTwo))
            .groupLead("John Smith").build()
        rsvpGroupRepository.save(rsvpGroup)

        // group of members whose nobody's name is similar to the provided name
        def rsvpThree = Rsvp.builder()
            .id(3)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .attending(false)
            .name("Micheal James").build()
        rsvpRepository.save(rsvpThree)
        def rsvpFour = Rsvp.builder()
            .id(4)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .attending(false)
            .name("Gracie James").build()
        rsvpRepository.save(rsvpFour)

        def rsvpGroupTwo = RsvpGroup.builder()
            .id(2)
            .email("test@test.com")
            .modifyGroup(false)
            .rsvps(Set.of(rsvpThree, rsvpFour))
            .groupLead("Gracie James").build()
        rsvpGroupRepository.save(rsvpGroupTwo)

        when:
        List<RsvpGroup> rsvpGroupRet = rsvpGroupRepository.findAllByRsvpsName("jOhN")

        then:
        rsvpGroupRet.size() == 1
        rsvpGroupRet[0].email == "test@test.com"
        !rsvpGroupRet[0].modifyGroup
        rsvpGroupRet[0].groupLead == "John Smith"
        rsvpGroupRet[0].rsvps.size() == 2

        !rsvpGroupRet[0].rsvps[0].attending
        rsvpGroupRet[0].rsvps[0].name == "John Smith"
        rsvpGroupRet[0].rsvps[0].id == 1
        rsvpGroupRet[0].rsvps[0].dietaryRestrictions == [DietaryRestriction.NO_PORK]
        rsvpGroupRet[0].rsvps[0].foodAllergies == [FoodAllergies.DAIRY]
        !rsvpGroupRet[0].rsvps[1].attending
        rsvpGroupRet[0].rsvps[1].name == "Jane Doe"
        rsvpGroupRet[0].rsvps[1].id == 2
        rsvpGroupRet[0].rsvps[1].dietaryRestrictions == [DietaryRestriction.NO_PORK]
        rsvpGroupRet[0].rsvps[1].foodAllergies == [FoodAllergies.DAIRY]
    }
}
