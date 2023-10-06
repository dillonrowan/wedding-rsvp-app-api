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
class RsvpRepositorySpec extends Specification {

    @Autowired
    RsvpRepository rsvpRepository

    def cleanup() {
        rsvpRepository.deleteAll()
    }

    def 'When an invalid id is provided, no result is returned'() {
        given:
        def rsvp = Rsvp.builder()
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        expect:
        rsvpRepository.findById(2).isEmpty()
    }

    def 'When a valid id is provided, the correct result is returned'() {
        given:
        def rsvp = Rsvp.builder()
            .attending(false)
            .name("John Smith").build()
        def rsvpRepositorySaved = rsvpRepository.save(rsvp)

        when:
        Optional<Rsvp> retRsvp = rsvpRepository.findById(rsvpRepositorySaved.id)

        then:
        retRsvp.get().id == rsvpRepositorySaved.id
        !retRsvp.get().attending
        retRsvp.get().name == "John Smith"
    }
}
