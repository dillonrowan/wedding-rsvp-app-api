package com.dillon.weddingrsvpapi.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A rsvp represents data pertaining to a single wedding guest.
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@Table(name = "rsvp")
@NoArgsConstructor
@AllArgsConstructor
public class Rsvp {

    /**
     * The unique identifier of the rsvp.
     */
    @Id
    @NotBlank(message = "Passcode is mandatory.")
    private String passcode;

    /**
     * The dietary restrictions belonging to the rsvp.
     */
    @Enumerated(EnumType.STRING)
    @OrderColumn(name = "dietary_restrictions")
    @Builder.Default
    private List<DietaryRestriction> dietaryRestrictions = new ArrayList<>();

    /**
     * the food allergies belonging to the rsvp.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "food_allergies")
    @Builder.Default
    private List<FoodAllergies> foodAllergies = new ArrayList<>();

    /**
     * The email belonging to the rsvp.
     */
    private String email;

    /**
     * The name belonging to the rsvp
     */
    private String name;

    /**
     * Denotes if the guest is attending the wedding or not.
     */
    private boolean attending;

    /**
     * Additional guests belonging to the rsvp.
     */
    @Column(name = "accompanying_guests")
    private int accompanyingGuests;
}
