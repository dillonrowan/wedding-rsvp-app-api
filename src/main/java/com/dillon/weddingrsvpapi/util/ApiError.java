package com.dillon.weddingrsvpapi.util;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * API Error class.
 */
@Data
public class ApiError {
    /**
     * HTTP Status code.
     */
    private HttpStatus status;

    /**
     * Error message.
     */
    private String message;

    /**
     * A map of errors to be sent to the client.
     */
    private Map<String, String> errors;

    /**
     * Empty constructor used for JSON deserialization.
     * <p>
     * Note that the other constructors should be favored in code.
     */
    public ApiError() {

    }

    /**
     * API Error constructor.
     *
     * @param status  The HTTP Status.
     * @param message The error message.
     * @param errors  A map of errors to error messages.
     */
    public ApiError(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    /**
     * Create an API Error without any errors.
     *
     * @param status  The HTTP status.
     * @param message The error message.
     */
    public ApiError(HttpStatus status, String message) {
        this(status, message, new HashMap<>());
    }
}
