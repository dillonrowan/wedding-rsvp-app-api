package com.dillon.weddingrsvpapi.service

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository
import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.AddDeleteRsvpDto
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.dillon.weddingrsvpapi.dto.RsvpGroup
import com.dillon.weddingrsvpapi.exception.RsvpGroupNotFoundException
import com.dillon.weddingrsvpapi.exception.RsvpNotFoundException
import spock.lang.Specification


class RsvpServiceSpec extends Specification {

    RsvpRepository rsvpRepository
    RsvpGroupRepository rsvpGroupRepository;
    RsvpService rsvpService

    def setup() {
        rsvpRepository = Mock()
        rsvpGroupRepository = Mock()
        rsvpService = new RsvpService(rsvpRepository, rsvpGroupRepository)
    }

    // Test findById()
    def 'When a valid rsvp exists and findById is called, exception is not thrown'() {
        setup:
        Rsvp rsvp = Mock()
        rsvp.getId() >> 1

        rsvpRepository.existsById(1) >> true

        when:
        rsvpService.findById(1) >> true

        then:
        notThrown(Exception)
    }

    def 'When a valid rsvp exists and findById is called with the incorrect id, findById throws RsvpNotFoundException'() {
        setup:
        rsvpRepository.findById(1) >> Optional.of(new Rsvp(id:  1))

        when:
        rsvpService.findById(1)

        then:
        notThrown(RsvpNotFoundException)
    }

    // Test updateRsvpAttendingAndFoodRestrictions()
    def 'When a valid rsvp does not exists and is updated, updateRsvpAttendingAndFoodRestrictions throws RsvpNotFoundException'() {
        setup:
        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1

        rsvpRepository.findAllById([1]) >> []

        when:
        rsvpService.updateRsvpAttendingAndFoodRestrictions([rsvpOne])

        then:
        thrown RsvpNotFoundException
    }

    def 'When a valid rsvp does exist and is updated, exception is not thrown'() {
        setup:
        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1

        rsvpRepository.findAllById([1]) >> [new Rsvp(id:  1)]

        when:
        rsvpService.updateRsvpAttendingAndFoodRestrictions([rsvpOne])

        then:
        notThrown RsvpNotFoundException
    }

    // test upsert upsertRsvpsToGroup
    def 'When a valid rsvp does not exists and is upserted with a valid name, saveAll is called with correct data'() {
        setup:
        RsvpGroup rsvpGroupOne = Mock()
        rsvpGroupOne.getId() >> 1

        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1
        rsvpOne.getName() >> "test"
        rsvpOne.getRsvpGroup() >> rsvpGroupOne

        rsvpGroupRepository.findById(1) >> Optional.of(rsvpGroupOne)

        when:
        rsvpService.upsertRsvpsToGroup(1L, [rsvpOne])

        then:
        1 * rsvpRepository.saveAll([rsvpOne])
    }

    def 'When a valid rsvp does not exists and is upserted with an invalid name, saveAll is not called'() {
        setup:
        RsvpGroup rsvpGroupOne = Mock()
        rsvpGroupOne.getId() >> 1

        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1
        rsvpOne.getName() >> ""
        rsvpOne.getRsvpGroup() >> rsvpGroupOne

        rsvpGroupRepository.findById(1) >> Optional.of(rsvpGroupOne)

        when:
        rsvpService.upsertRsvpsToGroup(1L, [rsvpOne])

        then:
        0 * rsvpRepository.saveAll(* _)
    }

    def 'When a valid rsvp does not exists and is upserted with name, but the rsvpGroup does not exist exception is thrown'() {
        setup:
        RsvpGroup rsvpGroupOne = Mock()
        rsvpGroupOne.getId() >> 1

        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1
        rsvpOne.getName() >> "test"
        rsvpOne.getRsvpGroup() >> rsvpGroupOne

        rsvpGroupRepository.findById(1) >> Optional.empty()

        when:
        rsvpService.upsertRsvpsToGroup(1L, [rsvpOne])

        then:
        thrown RsvpGroupNotFoundException
    }

    // test deleteRsvps
    def 'When a valid rsvp is attempted to be deleted with delete permissions, deleteAll is called'() {
        setup:
        RsvpGroup rsvpGroupOne = Mock()
        rsvpGroupOne.getId() >> 1
        rsvpGroupOne.getGroupLead() >> "test_lead"
        rsvpGroupOne.modifyGroup >> true

        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1
        rsvpOne.getName() >> "test"
        rsvpOne.getRsvpGroup() >> rsvpGroupOne

        AddDeleteRsvpDto addDeleteRsvpDto = new AddDeleteRsvpDto()
        addDeleteRsvpDto.setNames(["test"])

        rsvpGroupRepository.findById(1) >> Optional.of(rsvpGroupOne)
        rsvpRepository.findAllByNameInAndRsvpGroupId(["test"], 1L) >> [rsvpOne]

        when:
        rsvpService.deleteRsvps(1L, addDeleteRsvpDto)

        then:
        1 * rsvpRepository.deleteAll([rsvpOne])
    }

    def 'When a valid rsvp is attempted to be deleted with delete permissions, but the lead name is the same as the rsvp'() {
        setup:
        RsvpGroup rsvpGroupOne = Mock()
        rsvpGroupOne.getId() >> 1
        rsvpGroupOne.getGroupLead() >> "test"
        rsvpGroupOne.modifyGroup >> true

        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1
        rsvpOne.getName() >> "test"
        rsvpOne.getRsvpGroup() >> rsvpGroupOne

        AddDeleteRsvpDto addDeleteRsvpDto = new AddDeleteRsvpDto()
        addDeleteRsvpDto.setNames(["test"])

        rsvpGroupRepository.findById(1) >> Optional.of(rsvpGroupOne)
        rsvpRepository.findAllByNameInAndRsvpGroupId(["test"], 1L) >> [rsvpOne]

        when:
        rsvpService.deleteRsvps(1L, addDeleteRsvpDto)

        then:
        0 * rsvpRepository.deleteAll(* _)
    }

    def 'When a valid rsvp is attempted to be deleted without delete permissions, deleteAll is not called'() {
        setup:
        RsvpGroup rsvpGroupOne = Mock()
        rsvpGroupOne.getId() >> 1
        rsvpGroupOne.getGroupLead() >> "test_lead"
        rsvpGroupOne.modifyGroup >> false

        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1
        rsvpOne.getName() >> "test"
        rsvpOne.getRsvpGroup() >> rsvpGroupOne

        AddDeleteRsvpDto addDeleteRsvpDto = new AddDeleteRsvpDto()
        addDeleteRsvpDto.setNames(["test"])

        rsvpGroupRepository.findById(1) >> Optional.of(rsvpGroupOne)
        rsvpRepository.findAllByNameInAndRsvpGroupId(["test"], 1L) >> [rsvpOne]

        when:
        rsvpService.deleteRsvps(1L, addDeleteRsvpDto)

        then:
        0 * rsvpRepository.deleteAll(* _)
    }
}
