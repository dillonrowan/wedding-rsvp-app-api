package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.exception.RsvpNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RsvpService {

    RsvpRepository rsvpRepository;

    public RsvpService(RsvpRepository rsvpRepository) {
        this.rsvpRepository = rsvpRepository;
    }

    public Rsvp findByPasscode(String passcode) {
        return rsvpRepository.findById(passcode).orElseThrow(() -> new RsvpNotFoundException(passcode));
    }

    /**
     * Updates a rsvp record in the database.
     * If the given ID does not match the component, a {@link RsvpNotFoundException} is thrown.
     *
     * @param rsvp Rsvp to update.
     */
    @Transactional
    public void updateRsvp(Rsvp rsvp) {

        if(!rsvpRepository.existsById(rsvp.getPasscode())) {
            throw new RsvpNotFoundException(rsvp.getPasscode());
        }
        rsvpRepository.save(rsvp);
    }
}
