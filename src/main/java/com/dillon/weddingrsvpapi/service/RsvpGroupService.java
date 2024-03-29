package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import com.dillon.weddingrsvpapi.exception.RsvpGroupNotFoundByNameException;
import com.dillon.weddingrsvpapi.exception.RsvpGroupNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
     * Constructor for the RsvpGroupService class.
     *
     * @param rsvpGroupRepository Rsvp group repository service bean.
     */
    public RsvpGroupService(RsvpGroupRepository rsvpGroupRepository) {
        this.rsvpGroupRepository = rsvpGroupRepository;
    }

    /**
     * Finds rsvp group for specified id.
     *
     * @param id Id to search rsvp group for.
     * @return Rsvp group record belonging to the id. Throws {@link RsvpGroupNotFoundException} if rsvp is not found.
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
     * Throws {@link RsvpGroupNotFoundByNameException} if no rsvp groups are found.
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
     * @param rsvpGroupFromRequest Rsvps groups to update.
     */
    @Transactional
    public void updateRsvpGroupEmails(List<RsvpGroup> rsvpGroupFromRequest) {

        // Find rsvp groups that exist with the list provided in the request.
        List<RsvpGroup> existingRsvpGroups = rsvpGroupRepository.findAllById(rsvpGroupFromRequest.stream().map(RsvpGroup::getId).collect(Collectors.toList()));

        // Create map where id is key and value is rsvp group.
        Map<Long, RsvpGroup> existingRsvpGroupsMap = existingRsvpGroups.stream()
                .collect(Collectors.toMap(RsvpGroup::getId, Function.identity()));

        // Update the rsvp groups that exist using what was provided from the request. Matching by id. Only update email.
        for(RsvpGroup r : rsvpGroupFromRequest) {
            if(existingRsvpGroupsMap.containsKey(r.getId())) {
                RsvpGroup rsvpGroup = existingRsvpGroupsMap.get(r.getId());
                rsvpGroup.setEmail(r.getEmail());
                existingRsvpGroupsMap.put(r.getId(), rsvpGroup); // replace what was queried with what was sent from request
            } else {
                throw new RsvpGroupNotFoundException(r.getId());
            }
        }
        rsvpGroupRepository.saveAll(existingRsvpGroupsMap.values());
    }
}
