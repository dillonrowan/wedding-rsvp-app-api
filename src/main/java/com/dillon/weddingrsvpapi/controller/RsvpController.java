package com.dillon.weddingrsvpapi.controller;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.service.RsvpService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Rsvp REST controller.
 */
@RestController
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
     * @param rsvp JSON object that represents a rsvp. Must contain passcode in order to find the corresponding rsvp
     *             in the database.
     * @return
     */
    @PostMapping("/rsvp")
    Rsvp getRsvp(@Valid @RequestBody Rsvp rsvp) {
        return rsvpService.findByPasscode(rsvp.getPasscode());
    }

    /**
     * Updates rsvp by its passcode.
     * @param rsvp JSON object that represents rsvp. If the passcode in this object exists in the database, that
     *             record will be updated with the contents of this parameter.
     */
    @PostMapping("/update-rsvp")
    public void updateRsvp(@Valid @RequestBody Rsvp rsvp) {
        rsvpService.updateRsvp(rsvp);
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
