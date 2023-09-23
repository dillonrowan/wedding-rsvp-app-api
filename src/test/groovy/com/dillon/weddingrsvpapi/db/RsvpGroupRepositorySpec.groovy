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

    def 'When a valid name is provided, the group and its members with that member name is returned'() {
        given:
        def rsvp = Rsvp.builder()
            .id(1)
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)
        def rsvpTwo = Rsvp.builder()
            .id(2)
            .attending(false)
            .name("Jane Doe").build()
        rsvpRepository.save(rsvpTwo)

        def rsvpGroup = RsvpGroup.builder()
            .id(1)
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .modifyGroup(false)
            .rsvps(Set.of(rsvp, rsvpTwo))
            .groupLead("John Smith").build()
        rsvpGroupRepository.save(rsvpGroup)

        when:
        List<RsvpGroup> rsvpGroupRet = rsvpGroupRepository.findAllByRsvpsName("jOhN")

        then:
        rsvpGroupRet.size() == 1
        rsvpGroupRet[0].dietaryRestrictions == [DietaryRestriction.NO_PORK]
        rsvpGroupRet[0].foodAllergies == [FoodAllergies.DAIRY]
        rsvpGroupRet[0].email == "test@test.com"
        !rsvpGroupRet[0].modifyGroup
        rsvpGroupRet[0].groupLead == "John Smith"
        rsvpGroupRet[0].rsvps.size() == 2

        !rsvpGroupRet[0].rsvps[0].attending
        rsvpGroupRet[0].rsvps[0].name == "John Smith"
        rsvpGroupRet[0].rsvps[0].id == 1
        !rsvpGroupRet[0].rsvps[1].attending
        rsvpGroupRet[0].rsvps[1].name == "Jane Doe"
        rsvpGroupRet[0].rsvps[1].id == 2
    }
}
