package com.dillon.weddingrsvpapi.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

/**
 * The AuthenticationService is responsible for checking the incoming API key against the known
 * credentials.
 */
@Service
public class AuthenticationService {
    /** The expected header where the API key will be stored in each request. */
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    /** The API key for the FPP Data Sync service. */
    private final String apiKey;

    /**
     * Constructor for the AuthenticationService.
     *
     * @param apiKey The api key.
     */
    public AuthenticationService(@Value("${rsvp.api.client.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Checks the API key in the servlet request against the stored credentials.
     *
     * @param request the HttpServletRequest.
     * @return The {@link ApiKeyToken} is returned upon successful authentication.
     */
    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(this.apiKey)) {
            throw new BadCredentialsException("Invalid API Key.");
        }

        return new ApiKeyToken(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
