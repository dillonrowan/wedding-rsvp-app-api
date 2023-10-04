package com.dillon.weddingrsvpapi.controller;

import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import com.dillon.weddingrsvpapi.service.RsvpGroupService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
     * Constructor for the RsvpGroupController class.
     *
     * @param rsvpGroupService RsvpGroupService bean.
     */
    public RsvpGroupController(RsvpGroupService rsvpGroupService) {
        this.rsvpGroupService = rsvpGroupService;
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
    public void updateRsvpGroups(@RequestBody List<@Valid RsvpGroup> rsvpGroups) {
        rsvpGroupService.updateRsvpGroups(rsvpGroups);
    }
}
