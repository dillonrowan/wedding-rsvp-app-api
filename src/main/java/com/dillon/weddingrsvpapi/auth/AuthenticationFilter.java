package com.dillon.weddingrsvpapi.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

/** Filter responsible for authentication requests containing API keys. */
@Component
public class AuthenticationFilter extends GenericFilterBean {
    /** The authentication service */
    private final AuthenticationService authenticationService;

    /**
     * Constructor for the AuthenticationFilter.
     *
     * @param authenticationService The local authentication service.
     */
    public AuthenticationFilter(AuthenticationService authenticationService) {
        super();
        this.authenticationService = authenticationService;
    }

    /** {@inheritDoc} */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            Authentication authentication = authenticationService.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception exp) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpResponse.getWriter();
            writer.print("Invalid Credentials.");
            writer.flush();
            writer.close();
            return;
        }
        chain.doFilter(request, response);
    }
}
