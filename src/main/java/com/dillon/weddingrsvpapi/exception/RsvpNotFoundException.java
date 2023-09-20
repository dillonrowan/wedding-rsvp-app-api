package com.dillon.weddingrsvpapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RsvpNotFoundException extends RuntimeException {
    private final long id;
}
