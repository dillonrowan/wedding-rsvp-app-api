package com.dillon.weddingrsvpapi.service

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.dillon.weddingrsvpapi.dto.RsvpGroup
import com.dillon.weddingrsvpapi.exception.RsvpGroupNotFoundException
import com.dillon.weddingrsvpapi.exception.RsvpNotFoundException
import spock.lang.Specification

class RsvpGroupServiceSpec extends Specification {
    RsvpGroupRepository rsvpGroupRepository
    RsvpGroupService rsvpGroupService

    def setup() {
        rsvpGroupRepository = Mock()
        rsvpGroupService = new RsvpGroupService(rsvpGroupRepository)
    }

    // Test findById()
    def 'When a valid rsvp group exists and findById is called, exception is not thrown'() {
        setup:
        RsvpGroup rsvpGroup = Mock()
        rsvpGroup.getId() >> 1

        rsvpGroupRepository.existsById(1) >> true

        when:
        rsvpGroupService.findById(1) >> true

        then:
        notThrown(RsvpGroupNotFoundException)
    }

    def 'When a valid rsvp group exists and findById is called with the incorrect id, findById throws RsvpNotFoundException'() {
        setup:
        rsvpGroupRepository.findById(1) >> Optional.of(new RsvpGroup(id:  1))

        when:
        rsvpGroupService.findById(1)

        then:
        notThrown(RsvpGroupNotFoundException)
    }

    // Test updateRsvpGroups()
    def 'When a valid rsvp group does not exists and is updated, updateRsvpGroups throws RsvpGroupNotFoundException'() {
        setup:
        RsvpGroup rsvpGroupOne = Mock()
        rsvpGroupOne.getId() >> 1

        rsvpGroupRepository.findAllById([1]) >> []

        when:
        rsvpGroupService.updateRsvpGroups([rsvpGroupOne])

        then:
        thrown RsvpGroupNotFoundException
    }

    def 'When a valid rsvp group does exist and is updated, exception is not thrown'() {
        setup:
        RsvpGroup rsvpGroupOne = Mock()
        rsvpGroupOne.getId() >> 1

        rsvpGroupRepository.findAllById([1]) >> [new RsvpGroup(id:  1)]

        when:
        rsvpGroupService.updateRsvpGroups([rsvpGroupOne])

        then:
        notThrown RsvpGroupNotFoundException
    }

    // Test findAllBySimilarMemberName()
    def 'When a valid rsvp group does exist and findAllBySimilarMemberName is called, exception is not thrown'() {
        setup:
        RsvpGroup rsvpGroupOne = Mock()
        rsvpGroupOne.getGroupLead() >> "John Smith"

        rsvpGroupRepository.findAllByRsvpsName("John Smith") >> [new RsvpGroup(groupLead: "John Smith")]

        when:
        rsvpGroupService.findAllBySimilarMemberName("John Smith")

        then:
        notThrown RsvpGroupNotFoundException
    }
}
