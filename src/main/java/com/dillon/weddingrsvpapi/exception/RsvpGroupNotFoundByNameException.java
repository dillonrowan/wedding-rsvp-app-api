package com.dillon.weddingrsvpapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RsvpGroupNotFoundByNameException extends RuntimeException {
    private final String name;
}
