package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RsvpService {

    RsvpRepository rsvpRepository;

    public RsvpService(RsvpRepository rsvpRepository) {
        this.rsvpRepository = rsvpRepository;
    }

    public List<Rsvp> findAllByPasscode(String passcode) {
        return rsvpRepository.findByPasscode(passcode);
    }
}
