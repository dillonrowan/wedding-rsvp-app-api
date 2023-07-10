package com.dillon.weddingrsvpapi;

import lombok.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WeddingRsvpApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeddingRsvpApiApplication.class, args);
    }
}
