package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    }}
