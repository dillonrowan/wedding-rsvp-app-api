package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository;
import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import com.dillon.weddingrsvpapi.exception.RsvpGroupNotFoundByNameException;
import com.dillon.weddingrsvpapi.exception.RsvpGroupNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                new RsvpGroupNotFoundException(id)
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
            throw new RsvpGroupNotFoundByNameException(name);
        }
        return rsvpGroups;
    }

    /**
     * Updates rsvp group records in the database.
     * Updates the attending value of existing rsvp groups provided from parameter.
     *
     * @param rsvpGroupFromRequest Rsvps to update.
     */
    @Transactional
    public void updateRsvpGroups(List<RsvpGroup> rsvpGroupFromRequest) {

        // Find rsvp groups that exist with the list provided in the request.
        List<RsvpGroup> existingRsvpGroups = rsvpGroupRepository.findAllById(rsvpGroupFromRequest.stream().map(RsvpGroup::getId).collect(Collectors.toList()));

        // Create map where id is key and value is rsvp group.
        Map<Long, RsvpGroup> existingRsvpGroupsMap = existingRsvpGroups.stream()
                .collect(Collectors.toMap(RsvpGroup::getId, Function.identity()));

        // Update the rsvp groups that exist using what was provided from the request. Matching by id.
        for(RsvpGroup r : rsvpGroupFromRequest) {
            if(existingRsvpGroupsMap.containsKey(r.getId())) {
                RsvpGroup rsvpGroup = existingRsvpGroupsMap.get(r.getId());
                rsvpGroup.setDietaryRestrictions(r.getDietaryRestrictions());
                rsvpGroup.setFoodAllergies(r.getFoodAllergies());
                rsvpGroup.setEmail(r.getEmail());
                existingRsvpGroupsMap.put(r.getId(), rsvpGroup); // replace what was queried with what was sent from request
            } else {
                throw new RsvpGroupNotFoundException(r.getId());
            }
        }
        rsvpGroupRepository.saveAll(existingRsvpGroupsMap.values());
    }
}
