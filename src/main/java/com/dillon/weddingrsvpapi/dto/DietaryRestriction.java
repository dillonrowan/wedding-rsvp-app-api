package com.dillon.weddingrsvpapi.dto;

/**
 * The DietaryRestriction enumeration defines the various types of dietary restrictions available for a user
 * to select.
 */
public enum DietaryRestriction {
    /**
     * Dietary restriction for not being able to consume red meat.
     */
    NO_RED_MEAT,

    /**
     * Dietary restriction for not being able to consume chicken.
     */
    NO_CHICKEN,

    /**
     * Dietary restriction for not being able to consume fish.
     */
    NO_FISH,

    /**
     * Dietary restriction for not being able to consume eggs.
     */
    NO_EGGS,

    /**
     * Dietary restriction for not being able to consume pork.
     */
    NO_PORK,

    /**
     * Dietary restriction for not being able to consume anything not specified in this enumeration.
     */
    OTHER,

    /**
     * Dietary restriction for not being able to consume dairy products.
     */
    DAIRY
}
