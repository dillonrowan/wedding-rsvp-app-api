package com.dillon.weddingrsvpapi.controller;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import com.dillon.weddingrsvpapi.service.RsvpGroupService;
import com.dillon.weddingrsvpapi.service.RsvpService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Rsvp group REST controller.
 */
@Validated
@RestController
@RequestMapping("/api")
public class RsvpGroupController {
    /**
     * Rsvp group service bean.
     */
    private final RsvpGroupService rsvpGroupService;

    /**
     * Rsvp service bean.
     */
    private final RsvpService rsvpService;

    /**
     * Constructor for the RsvpGroupController class.
     *
     * @param rsvpGroupService RsvpGroupService bean.
     */
    public RsvpGroupController(RsvpGroupService rsvpGroupService, RsvpService rsvpService) {
        this.rsvpGroupService = rsvpGroupService;
        this.rsvpService = rsvpService;
    }

    /**
     * Gets a rsvp group by the provided id.
     *
     * @param id Long id number belonging to a record for an rsvp group.
     * @return Rsvp group that belonged to id.
     */
    @GetMapping("/rsvp-groups/{id}")
    RsvpGroup getRsvpGroup(@PathVariable long id) {
        return rsvpGroupService.findById(id);
    }

    /**
     * Gets a list of rsvp groups by the provided member name.
     *
     * @param name The group member name used to search for rsvp groups.
     * @return Rsvp group records whose members contain similar name.
     * Throws RsvpGroupNotFoundException if rsvp group is not found.
     */
    @GetMapping("/rsvp-groups-by-name/{name}")
    List<RsvpGroup> getRsvpGroupByName(@PathVariable String name) {
        return rsvpGroupService.findAllBySimilarMemberName(name);
    }

    /**
     * Updates rsvp groups by their id.
     * @param rsvpGroups List of JSON objects that represents rsvp groups.
     */
    @PostMapping("/update-rsvp-groups")
    public void updateRsvpGroupEmails(@RequestBody List<@Valid RsvpGroup> rsvpGroups) {
        rsvpGroupService.updateRsvpGroupEmails(rsvpGroups);
    }

    /**
     * Updates rsvp groups by their id. Updates rsvps by their id. Will only update the email value in the rsvp groups.
     * Will only update the attending status and food restrictions in the rsvps.
     * @param rsvpGroups List of JSON objects that represents rsvp groups.
     */
    @PostMapping("/update-rsvp-and-rsvp-groups")
    public void updateRsvpAndRsvpGroups(@RequestBody List<@Valid RsvpGroup> rsvpGroups) {
        rsvpGroupService.updateRsvpGroupEmails(rsvpGroups);

        List<Rsvp> rsvps = new ArrayList<>();
        for (RsvpGroup rsvpGroup : rsvpGroups) {
            rsvps.addAll(rsvpGroup.getRsvps());
        }
        rsvpService.updateRsvpAttendingAndFoodRestrictions(rsvps);
    }
}
