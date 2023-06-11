package controller

import com.dillon.weddingrsvpapi.db.RsvpRepository
import com.dillon.weddingrsvpapi.dto.DietaryRestriction
import com.dillon.weddingrsvpapi.dto.FoodAllergies
import com.dillon.weddingrsvpapi.dto.Rsvp
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.test.context.ActiveProfiles
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification
import lombok.*;

//@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RsvpControllerSpec extends Specification {
    @LocalServerPort
    int port

    @Autowired
    RsvpRepository rsvpRepository;

    @Autowired
    TestRestTemplate restTemplate;

    String baseUrl;

    @PostConstruct
    public void initialise(){
        baseUrl = "http://localhost:" + port;
    }

    def cleanup() {
        rsvpRepository.deleteAll()
    }

    def setup() {
        rsvpRepository = Mock()
        restTemplate = new TestRestTemplate("sa","sa")
//        restTemplate.getRestTemplate().setInterceptors(
//            Collections.singletonList((request, body, execution) -> {
//                request.getHeaders().add("x-wsag-secret-key", "secret");
//                request.getHeaders().add("x-wsag-client-id", "app-name")
//                return execution.execute(request, body);
//            } as ClientHttpRequestInterceptor)
//        )
    }

    def 'When a rsvp is updated, it returns HttpStatus.UPDATED'() {
        given:
        def rsvp = Rsvp.builder()
            .passcode("abcde")
            .dietaryRestrictions(Arrays.asList("NO_PORK") as List<DietaryRestriction>)
            .foodAllergies(Arrays.asList("DAIRY" as FoodAllergies))
            .email("test@test.com")
            .attending(false)
            .accompanyingGuests(0)
        rsvpRepository.save(rsvp)

        //TODO: why am i not getting result back from this? are we not connected to h2?
        def List<Rsvp> myRsvp = rsvpRepository.findByPasscode("abcde")
        System.out.println(myRsvp)

        when:
        def result = restTemplate.postForEntity("http://localhost:${port}/update_rsvp", [
            "passcode"  : "abcde",
            "attending" : true
        ], String)
//        def result = restTemplate.postForEntity("update_rsvp", [
//            "passcode"  : "abcde",
//            "attending" : true
//        ], String)

        then:
        result.statusCode == HttpStatus.CREATED
    }
}
