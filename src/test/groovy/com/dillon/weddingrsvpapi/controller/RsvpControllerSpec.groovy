package com.dillon.weddingrsvpapi.controller

import com.dillon.weddingrsvpapi.controller.RsvpController
import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.web.client.RestTemplate
import org.springframework.jdbc.core.JdbcTemplate

import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest
class RsvpControllerSpec extends Specification{

    @Autowired
    RsvpRepository rsvpRepository

    @LocalServerPort
    private int port

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    JdbcTemplate jdbcTemplate

    def "should be a simple assertion"() {
//        given:
//        System.out.println(Arrays.asList(applicationContext.getBeanDefinitionNames()));
        expect:
        jdbcTemplate != null
        rsvpRepository != null
    }

//    def 'When a finished good component is posted, it returns HttpStatus.CREATED'() {
//        when:
//        def result = restTemplate.postForEntity("http://localhost:${port}/upsert_rsvp", [], String)
//
//        then:
//        result.statusCode == HttpStatus.CREATED
//    }
//
//
//
//    def 'When a rsvp is updated, it returns HttpStatus.UPDATED'() {
//
//        given:
//        def rsvp = Rsvp.builder()
//            .passcode("abcde")
//            .dietaryRestrictions(Arrays.asList("NO_PORK") as List<DietaryRestriction>)
//            .foodAllergies(Arrays.asList("DAIRY" as FoodAllergies))
//            .email("test@test.com")
//            .attending(false)
//            .name("John Smith")
//            .accompanyingGuests(0).build()
//        def result = restTemplate.postForEntity("http://localhost:${port}/upsert_rsvp", [
//            "passcode"  : "abcde",
//            "attending" : true
//        ], String)
//
//        when:
//        boolean myBool = true;
////        def result = restTemplate.postForEntity("http://localhost:${port}/upsert_rsvp", [
////            "passcode"  : "abcde",
////            "attending" : true
////        ], String)
//
//        then:
//        myBool
////        result.statusCode == HttpStatus.CREATED
//    }
}
