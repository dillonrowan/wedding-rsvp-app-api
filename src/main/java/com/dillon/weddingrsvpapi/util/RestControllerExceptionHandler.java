package com.dillon.weddingrsvpapi.util;

import com.dillon.weddingrsvpapi.exception.RsvpGroupNotFoundByNameException;
import com.dillon.weddingrsvpapi.exception.RsvpGroupNotFoundException;
import com.dillon.weddingrsvpapi.exception.RsvpNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.HashMap;
import java.util.Map;

/**
 * Rest controller exception handler. Specifies how to handle different exceptions thrown during runtime.
 */
@RestControllerAdvice
public class RestControllerExceptionHandler {
    /**
     * Exception handler for ConstraintViolationException.
     * @param e The ConstraintViolationException invoking this method.
     * @return {@link ApiError} Object containing error data.
     */
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

    /**
     * Exception handler for RsvpGroupNotFoundException.
     * @param e The RsvpGroupNotFoundException invoking this method.
     * @return {@link ApiError} Object containing error data.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RsvpGroupNotFoundException.class)
    public ApiError handleRsvpGroupNotFoundException(RsvpGroupNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, "Rsvp group with id " + e.getId() + " was not found.");
    }

    /**
     * Exception handler for RsvpGroupNotFoundByNameException.
     * @param e The RsvpGroupNotFoundByNameException invoking this method.
     * @return {@link ApiError} Object containing error data.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RsvpGroupNotFoundByNameException.class)
    public ApiError handleRsvpGroupNotFoundByNameException(RsvpGroupNotFoundByNameException e) {
        return new ApiError(HttpStatus.NOT_FOUND, "Could not find rsvp groups that had any members with similar name.");
    }

    /**
     * Exception handler for RsvpNotFoundException.
     * @param ex The RsvpNotFoundException invoking this method.
     * @return {@link ApiError} Object containing error data.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RsvpNotFoundException.class)
    public ApiError handleRsvpNotFoundException(RsvpNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, "Rsvp with id " + e.getId() + " was not found.");
    }

    /**
     * Exception handler for RequestArgumentTypeMismatchException.
     * @param e The RequestArgumentTypeMismatchException invoking this method.
     * @return {@link ApiError} Object containing error data.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiError RequestArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put(e.getPropertyName(), e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, "Invalid argument in request.", errors);
    }
}
