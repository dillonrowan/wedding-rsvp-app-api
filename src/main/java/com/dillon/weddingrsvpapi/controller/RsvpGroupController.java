package com.dillon.weddingrsvpapi.controller;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import com.dillon.weddingrsvpapi.service.RsvpGroupService;
import com.dillon.weddingrsvpapi.service.RsvpService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rsvp group REST controller.
 */
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
     * @param id Integer id number belonging to a record for an rsvp group.
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
     * @return Rsvp record belonging to passcode. Throws ResponseStatusException if rsvp is not found.
     */
    @GetMapping("/rsvp-groups-by-name/{name}")
    List<RsvpGroup> getRsvpGroupByName(@PathVariable String name) {
        return rsvpGroupService.findAllBySimilarMemberName(name);
    }
}
