package com.dillon.weddingrsvpapi.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/** The authentication token class for the API key flow. */
public class ApiKeyToken extends AbstractAuthenticationToken {
    /** The API Key value. */
    private final String apiKey;

    /**
     * Constructor for the ApiKeyToken
     *
     * @param apiKey The API key.
     * @param authorities The granted authorities.
     */
    public ApiKeyToken(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    /** {@inheritDoc} */
    @Override
    public Object getCredentials() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Object getPrincipal() {
        return apiKey;
    }
}
