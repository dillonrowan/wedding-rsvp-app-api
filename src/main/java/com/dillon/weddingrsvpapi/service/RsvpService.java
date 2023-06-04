package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import org.springframework.stereotype.Service;

@Service
public class RsvpService {

    RsvpRepository rsvpRepository;

    public RsvpService(RsvpRepository rsvpRepository) {
        this.rsvpRepository = rsvpRepository;
    }

    public Rsvp findAllByPasscode(String passcode) {
        return rsvpRepository.findRsvpByPasscode(passcode);
    }
}
