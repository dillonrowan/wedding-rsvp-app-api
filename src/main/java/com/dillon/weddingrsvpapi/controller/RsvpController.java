package com.dillon.weddingrsvpapi.controller;

import com.dillon.weddingrsvpapi.db.RsvpRepository;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.service.RsvpService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RsvpController {

    private final RsvpService rsvpService;

    public RsvpController(RsvpService rsvpService) {
        this.rsvpService = rsvpService;
    }

    @PostMapping("/rsvp")
    List<Rsvp> getRsvp(@RequestBody Rsvp rsvp) {
        return rsvpService.findAllByPasscode(rsvp.getPasscode());
    }

    @PostMapping("/upsert_rsvp")
    public void upsertRsvp(@RequestBody Rsvp rsvp) {
        rsvpService.upsertRsvp(rsvp);
    }
}
