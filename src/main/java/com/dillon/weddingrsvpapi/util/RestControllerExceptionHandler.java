package com.dillon.weddingrsvpapi.util;

import com.dillon.weddingrsvpapi.exception.MisMatchedIdException;
import com.dillon.weddingrsvpapi.exception.RsvpNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleValidationExceptions(
            ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((violation) -> {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        });
        return new ApiError(HttpStatus.BAD_REQUEST, "Request was invalid", errors);
    }

    //TODO: make exception if rsvp group id not found

    //TODO: use this if we sent an rsvp record to update that did not exist in dp. make one for group
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RsvpNotFoundException.class)
    public ApiError handleRsvpNotFoundException(RsvpNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, "Rsvp with id " + e.getId() + " was not found");
    }
}
