package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service responsible for managing rsvps.
 */
@Service
public class RsvpService {

    /**
     * Rsvp repository bean.
     */
    RsvpRepository rsvpRepository;

    /**
     * Constructor for the RsvpService class.
     *
     * @param rsvpRepository Rsvp repository service bean.
     */
    public RsvpService(RsvpRepository rsvpRepository) {
        this.rsvpRepository = rsvpRepository;
    }

    /**
     * Finds rsvp for specified passcode.
     *
     * @param id Passcode to search rsvp for.
     * @return Rsvp record belonging to passcode. Throws ResponseStatusException if rsvp is not found.
     */
    public Rsvp findById(long id) {
        return rsvpRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find rsvp with id.\n")
        );
    }

//    /**
//     * Finds rsvp for specified passcode.
//     *
//     * @param passcode Passcode to search rsvp for.     *
//     * @return Rsvp record belonging to passcode. Throws ResponseStatusException if rsvp is not found.
//     */
//    public Rsvp findByPasscode(String passcode) {
//        return rsvpRepository.findById(passcode).orElseThrow(() ->
//            new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find rsvp with passcode.\n")
//        );
//    }
//
//    /**
//     * Updates a rsvp record in the database.
//     *
//     * @param rsvp Rsvp to update.
//     */
//    @Transactional
//    public void updateRsvp(Rsvp rsvp) {
//
//        if(!rsvpRepository.existsById(rsvp.getPasscode())) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not update rsvp with passcode.\n");
//        }
//        rsvpRepository.save(rsvp);
//    }
}
