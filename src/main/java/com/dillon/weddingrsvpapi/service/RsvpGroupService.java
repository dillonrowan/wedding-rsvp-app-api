package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository;
import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
     * @param rsvpGroupRepository Rsvp repository service bean.
     */
    public RsvpGroupService(RsvpGroupRepository rsvpGroupRepository) {
        this.rsvpGroupRepository = rsvpGroupRepository;
    }

    /**
     * Finds rsvp for specified passcode.
     *
     * @param id Id to search rsvp for.
     * @return Rsvp record belonging to passcode. Throws ResponseStatusException if rsvp is not found.
     */
    public RsvpGroup findById(long id) {
        return rsvpGroupRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find rsvp group with id.\n")
        );
    }

    /**
     * Finds rsvp groups that have members with similar name.
     *
     * @param name The group member name used to find groups.
     * @return Rsvp groups that have at least 1 member with a similar name.
     * Throws ResponseStatusException if no rsvp groups are found.
     */
    public List<RsvpGroup> findAllBySimilarMemberName(String name) {
        List<RsvpGroup> rsvpGroups = rsvpGroupRepository.findAllByRsvpsName(name);
        if(rsvpGroups.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Could not find rsvp groups that had any members with similar name.\n");
        }
        return rsvpGroups;
    }
}
