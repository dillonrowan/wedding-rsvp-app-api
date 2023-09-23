package com.dillon.weddingrsvpapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Exception class for rsvp group records not found by a provided name.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RsvpGroupNotFoundByNameException extends RuntimeException {
    private final String name;
}
