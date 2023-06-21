package com.dillon.weddingrsvpapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * An exception that represents situations where an rsvp was not found by its passcode.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RsvpNotFoundException extends RuntimeException {
    /**
     * Passcode of the rsvp that was not found.
     */
    private final String passcode;
}
