package com.dillon.weddingrsvpapi.service

import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.Rsvp
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification


class RsvpServiceSpec extends Specification {

    RsvpRepository rsvpRepository
    RsvpService rsvpService

    def setup() {
        rsvpRepository = Mock()
        rsvpService = new RsvpService(rsvpRepository)
    }

    def 'When a valid rsvp exists and is updated, updateRsvp does not throw an exception'() {
        setup:
        Rsvp rsvp = Mock()
        rsvp.getPasscode() >> "abcde"

        rsvpRepository.existsById("abcde") >> true

        when:
        rsvpService.updateRsvp(rsvp)

        then:
        notThrown(Exception)
    }

    def 'When a valid rsvp does not exists and is updated, updateRsvp throws an exception'() {
        setup:
        Rsvp rsvp = Mock()
        rsvp.getPasscode() >> "abcde"

        rsvpRepository.existsById("abcde") >> false

        when:
        rsvpService.updateRsvp(rsvp)

        then:
        thrown ResponseStatusException
    }

    def 'When an rsvp that does exist is queried, findByPasscode does not throw and Exception'() {
        setup:
        rsvpRepository.findById("aaaaa") >> Optional.of(new Rsvp(passcode:  "aaaaa"))

        when:
        rsvpService.findByPasscode("aaaaa")

        then:
        notThrown(Exception)
    }

    def 'When an rsvp that does not exist is queried, findByPasscode throws ResponseStatusException'() {
        setup:
        rsvpRepository.findById("bbbbb") >> Optional.empty()

        when:
        rsvpService.findByPasscode("bbbbb")

        then:
        thrown ResponseStatusException
    }
}
