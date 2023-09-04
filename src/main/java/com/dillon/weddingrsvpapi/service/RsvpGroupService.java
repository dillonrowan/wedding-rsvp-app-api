package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository;
import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service responsible for managing rsvp groups.
 */
@Service
public class RsvpGroupService {
    /**
     * Rsvp group repository bean.
     */
    RsvpGroupRepository rsvpGroupRepository;

    /**
     * Constructor for the RsvpService class.
     *
     * @param rsvpRepository Rsvp repository service bean.
     */
    public RsvpGroupService(RsvpGroupRepository rsvpGroupRepository) {
        this.rsvpGroupRepository = rsvpGroupRepository;
    }

    /**
     * Finds rsvp for specified passcode.
     *
     * @param passcode Passcode to search rsvp for.     *
     * @return Rsvp record belonging to passcode. Throws ResponseStatusException if rsvp is not found.
     */
    public RsvpGroup findById(long id) {
        return rsvpGroupRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find rsvp group with id.\n")
        );
    }
}
