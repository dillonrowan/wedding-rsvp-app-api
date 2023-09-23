package com.dillon.weddingrsvpapi.service

import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.Rsvp
import com.dillon.weddingrsvpapi.exception.RsvpNotFoundException
import spock.lang.Specification


class RsvpServiceSpec extends Specification {

    RsvpRepository rsvpRepository
    RsvpService rsvpService

    def setup() {
        rsvpRepository = Mock()
        rsvpService = new RsvpService(rsvpRepository)
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

    // Test updateRsvpsAttending()
    def 'When a valid rsvp does not exists and is updated, updateRsvpsAttending throws RsvpNotFoundException'() {
        setup:
        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1

        rsvpRepository.findAllById([1]) >> []

        when:
        rsvpService.updateRsvpsAttending([rsvpOne])

        then:
        thrown RsvpNotFoundException
    }

    def 'When a valid rsvp does exist and is updated, exception is not thrown'() {
        setup:
        Rsvp rsvpOne = Mock()
        rsvpOne.getId() >> 1

        rsvpRepository.findAllById([1]) >> [new Rsvp(id:  1)]

        when:
        rsvpService.updateRsvpsAttending([rsvpOne])

        then:
        notThrown RsvpNotFoundException
    }
}
