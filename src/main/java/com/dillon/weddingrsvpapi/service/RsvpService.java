package com.dillon.weddingrsvpapi.service;

import com.dillon.weddingrsvpapi.db.RsvpGroupRepository;
import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.AddDeleteRsvpDto;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import com.dillon.weddingrsvpapi.exception.RsvpGroupNotFoundException;
import com.dillon.weddingrsvpapi.exception.RsvpNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service responsible for managing rsvps.
 */
@Service
public class RsvpService {
    /**
     * Rsvp repository bean.
     */
    RsvpRepository rsvpRepository;

    RsvpGroupRepository rsvpGroupRepository;

    /**
     * Constructor for the RsvpService class.
     *
     * @param rsvpRepository Rsvp repository service bean.
     */
    public RsvpService(RsvpRepository rsvpRepository, RsvpGroupRepository rsvpGroupRepository) {
        this.rsvpRepository = rsvpRepository;
        this.rsvpGroupRepository = rsvpGroupRepository;
    }

    /**
     * Finds rsvp for specified passcode.
     *
     * @param id Passcode to search rsvp for.
     * @return Rsvp record belonging to passcode. Throws {@link RsvpNotFoundException} if rsvp is not found.
     */
    public Rsvp findById(long id) {
        return rsvpRepository.findById(id).orElseThrow(() ->
                new RsvpNotFoundException(id)
        );
    }

    /**
     * Updates rsvp records in the database.
     * Updates the attending value of existing rsvps provided from rsvpsFromRequest.
     * Throws {@link RsvpNotFoundException} if rsvp is not found.
     *
     * @param rsvpsFromRequest Rsvps to update.
     */
    @Transactional
    public void updateRsvpAttendingAndFoodRestrictions(List<Rsvp> rsvpsFromRequest) {

        // Find rsvps that exist with the list provided in the request.
        List<Long> idsFromRequest = rsvpsFromRequest.stream().map(Rsvp::getId).toList();
        List<Rsvp> existingRsvps = rsvpRepository.findAllById(idsFromRequest);

        // Create map where id is key and value is rsvp.
        Map<Long, Rsvp> existingRsvpsMap = existingRsvps.stream()
                .collect(Collectors.toMap(Rsvp::getId, Function.identity()));

        // Update the rsvps that exist using what was provided from the request. Matching by id. Only update attending
        // status, dietary restrictions, and food allergies.
        for(Rsvp r : rsvpsFromRequest) {
            if(existingRsvpsMap.containsKey(r.getId())) {
                Rsvp rsvp = existingRsvpsMap.get(r.getId());
                rsvp.setAttending(r.getAttending());
                rsvp.setFoodAllergies(r.getFoodAllergies());
                rsvp.setDietaryRestrictions(r.getDietaryRestrictions());
                existingRsvpsMap.put(r.getId(), rsvp); // Replace what was queried with what was sent from request.
            } else {
                // If request had any ids not in database throw exception.
                throw new RsvpNotFoundException(r.getId());
            }
        }
        rsvpRepository.saveAll(existingRsvpsMap.values());
    }

    @Transactional
    public void addRsvpsToGroup(long groupId, AddDeleteRsvpDto addDeleteRsvpDto) {

        // Check if group exists
        RsvpGroup rsvpGroup = rsvpGroupRepository.findById(groupId).orElseThrow(() -> new RsvpGroupNotFoundException(groupId));

        if (rsvpGroup.isModifyGroup()) {
            List<Rsvp> rsvpsToAdd = new ArrayList<>();
            for (String name : addDeleteRsvpDto.getNames()) {
                Rsvp rsvp = Rsvp.builder()
                        .name(name)
                        .rsvpGroup(rsvpGroup)
                        .dietaryRestrictions(new ArrayList<>())
                        .foodAllergies(new ArrayList<>())
                        .attending(true)
                        .build();
                rsvpsToAdd.add(rsvp);
            }
            rsvpRepository.saveAll(rsvpsToAdd);
        }
    }

    @Transactional
    public void deleteRsvps(long groupId, AddDeleteRsvpDto addDeleteRsvpDto) {

        // Check if group exists
        RsvpGroup rsvpGroup = rsvpGroupRepository.findById(groupId).orElseThrow(() -> new RsvpGroupNotFoundException(groupId));
        if (rsvpGroup.isModifyGroup()) {
            if (!rsvpGroupRepository.existsById(groupId)) {
                throw new RsvpGroupNotFoundException(groupId);
            }

            // Check if rsvps exists
            List<Rsvp> rsvps = rsvpRepository.findAllByNameIn(addDeleteRsvpDto.getNames());
            rsvpRepository.deleteAll(rsvps);
        }
    }
}
