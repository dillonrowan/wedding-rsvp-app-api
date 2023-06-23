package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RsvpService {

    RsvpRepository rsvpRepository;

    public RsvpService(RsvpRepository rsvpRepository) {
        this.rsvpRepository = rsvpRepository;
    }

    public Rsvp findByPasscode(String passcode) {
        return rsvpRepository.findById(passcode).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find rsvp with passcode.\n")
        );
    }

    /**
     * Updates a rsvp record in the database.
     * @param rsvp Rsvp to update.
     */
    @Transactional
    public void updateRsvp(Rsvp rsvp) {

        if(!rsvpRepository.existsById(rsvp.getPasscode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not update rsvp with passcode.\n");
        }
        rsvpRepository.save(rsvp);
    }
}
