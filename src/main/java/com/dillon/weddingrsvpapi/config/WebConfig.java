package com.dillon.weddingrsvpapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class.
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * The allowed origin to make requests.
     */
    @Value("${allowed.origin}")
    private String allowedOrigin;

    /**
     * Configures CORS policy. Adds "POST", "GET", "PUT", "PATCH", "DELETE", "OPTIONS" as allowed request methods.
     * Sets the allowed origin to the value in {@link allowedOrigin}.
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("POST", "GET", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
