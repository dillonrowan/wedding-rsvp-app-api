package com.dillon.weddingrsvpapi.dto;

/**
 * The FoodAllergies enumeration defines the various types of food allergies available for a user
 * to select.
 */
public enum FoodAllergies {
    /**
     * Food allergy for not being able to consume peanuts.
     */
    PEANUTS,

    /**
     * Food allergy for not being able to consume fish.
     */
    FISH,

    /**
     * Food allergy for not being able to consume eggs.
     */
    EGGS,

    /**
     * Food allergy for not being able to consume soy products.
     */
    SOY_PRODUCTS,

    /**
     * Food allergy for not being able to consume dairy products.
     */
    DAIRY,

    /**
     * Food allergy for not being able to consume tree nuts.
     */
    TREE_NUTS,

    /**
     * Food allergy for not being able to consume mushrooms.
     */
    MUSHROOM
}
