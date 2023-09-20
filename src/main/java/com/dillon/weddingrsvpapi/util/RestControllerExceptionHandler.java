package com.dillon.weddingrsvpapi.util;

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
        //return errors;
//        return null;
    }
}
