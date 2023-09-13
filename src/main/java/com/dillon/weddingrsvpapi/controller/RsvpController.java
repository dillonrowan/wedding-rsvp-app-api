package com.dillon.weddingrsvpapi.controller;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import com.dillon.weddingrsvpapi.service.RsvpService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rsvp REST controller.
 */
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
     * @param id Integer id number belonging to a record for an rsvp group.
     * @return Rsvp group that belonged to id.
     */
    @GetMapping("/rsvps/{id}")
    Rsvp getRsvp(@PathVariable long id) {
        return rsvpService.findById(id);
    }

    /**
     * Updates rsvps by their id.
     * @param rsvps list of JSON objects that represents rsvps.
     */
    @PostMapping("/update-rsvps")
    public void updateRsvp(@Valid @RequestBody List<Rsvp> rsvps) {
        rsvpService.updateRsvpsAttending(rsvps);
    }

    /**
     * Exception handler for object validation.
     * @param ex Exception thrown when object validation fails.
     *
     * @return Map of errors that had occurred during object validation.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

