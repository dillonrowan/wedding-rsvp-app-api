package com.dillon.weddingrsvpapi.controller;

import com.dillon.weddingrsvpapi.dto.AddDeleteRsvpDto;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.service.RsvpService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Rsvp REST controller.
 */
@Validated
@RestController
@RequestMapping("/api")
public class RsvpController {

    /**
     * Rsvp service bean.
     */
    private final RsvpService rsvpService;

    /**
     * Constructor for the RsvpController class.
     *
     * @param rsvpService RsvpService bean.
     */
    public RsvpController(RsvpService rsvpService) {
        this.rsvpService = rsvpService;
    }

    /**
     * Gets a rsvp by the provided passcode.
     *
     * @param id Long id number belonging to a record for an rsvp group.
     * @return Rsvp group that belonged to id.
     */
    @GetMapping("/rsvps/{id}")
    Rsvp getRsvp(@PathVariable long id) {
        return rsvpService.findById(id);
    }

    /**
     * Updates rsvps attending status by their id.
     * @param rsvps List of JSON objects that represents rsvps.
     */
    @PostMapping("/update-rsvps")
    public void updateRsvp(@RequestBody List<@Valid Rsvp> rsvps) {
        rsvpService.updateRsvpAttendingAndFoodRestrictions(rsvps);
    }

    @PutMapping("/rsvps/{groupId}")
    public void upsertRsvpsToGroup(@PathVariable long groupId, @RequestBody @Valid List<Rsvp> rsvps) {
        rsvpService.upsertRsvpsToGroup(groupId, rsvps);
    }

    @DeleteMapping("/rsvps/{groupId}")
    public void deleteRsvpsFromGroup(@PathVariable long groupId, @RequestBody @Valid AddDeleteRsvpDto addDeleteRsvpDto) {
        rsvpService.deleteRsvps(groupId, addDeleteRsvpDto);
    }
}

