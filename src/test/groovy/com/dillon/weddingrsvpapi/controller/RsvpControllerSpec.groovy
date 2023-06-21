package com.dillon.weddingrsvpapi.controller

import com.dillon.weddingrsvpapi.WeddingRsvpApiApplication
import com.dillon.weddingrsvpapi.controller.RsvpController
import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor
import org.springframework.web.client.RestTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.http.HttpStatus


import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration
//@DataJpaTest
class RsvpControllerSpec extends Specification{

    @Autowired
    RsvpRepository rsvpRepository

    @LocalServerPort
    private int port

    @Autowired
    TestRestTemplate restTemplate;

//    @Autowired
//    ApplicationContext applicationContext;

    @Autowired
    JdbcTemplate jdbcTemplate

    def cleanup() {
        rsvpRepository.deleteAll()
    }

    def "insert via RsvpRepository"() {
        given:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)
        List<Rsvp> rsvps = rsvpRepository.findByPasscode("abcde")

        expect:
        rsvps.size() > 0
    }

    def 'When a rsvp is updated, it returns HttpStatus.UPDATED'() {

        given:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(List.of( DietaryRestriction.NO_PORK ))
            .foodAllergies(List.of( FoodAllergies.DAIRY ))
            .email("test@test.com")
            .attending(false)
            .name("John Smith").build()
        rsvpRepository.save(rsvp)

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/update-rsvp", [
            "passcode"  : "abcde",
            "attending" : true
        ], String)

        then:
        result.statusCode == HttpStatus.UPDATED
    }
}
