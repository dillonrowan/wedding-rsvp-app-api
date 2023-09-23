package com.dillon.weddingrsvpapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Exception class for rsvp records not found by a provided id.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RsvpNotFoundException extends RuntimeException {
    private final long id;
}
