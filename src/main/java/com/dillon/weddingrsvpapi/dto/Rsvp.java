package com.dillon.weddingrsvpapi.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The rsvp group associated with this rsvp.
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "group_id")
    private RsvpGroup rsvpGroup;

    /**
     * The name belonging to the rsvp
     */
    private String name;

    /**
     * Denotes if the guest is attending the wedding or not.
     */
    private Boolean attending;

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
}
