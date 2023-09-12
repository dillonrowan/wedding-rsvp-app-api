package com.dillon.weddingrsvpapi.dto;

import com.dillon.weddingrsvpapi.dto.DietaryRestriction;
import com.dillon.weddingrsvpapi.dto.FoodAllergies;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A rsvp group represents an associated collection of wedding guests that should be attending together.
 */
@Entity
@Getter
@Setter
@Builder
@Table(name = "rsvp_group")
@NoArgsConstructor
@AllArgsConstructor
public class RsvpGroup {
    /**
     * The unique identifier of the rsvp group.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The head person of the group.
     */
    @Column(name = "group_lead")
    private String groupLead;

    /**
     * The set of rsvp records that are associated to this rsvp group.
     */
    @OneToMany
    @JsonManagedReference
    @JoinColumn(name = "group_id")
    private Set<Rsvp> rsvps = new HashSet<>();

    /**
     * The dietary restrictions belonging to the rsvp group.
     */
    @Enumerated(EnumType.STRING)
    @OrderColumn(name = "dietary_restrictions")
    @Builder.Default
    private List<DietaryRestriction> dietaryRestrictions = new ArrayList<>();

    /**
     * the food allergies belonging to the rsvp group.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "food_allergies")
    @Builder.Default
    private List<FoodAllergies> foodAllergies = new ArrayList<>();

    /**
     * The email belonging to the rsvp group.
     */
    private String email;
}
