package com.dillon.weddingrsvpapi.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "rsvp")
public class Rsvp {

    public Rsvp() {}

    @Id
    @NonNull
    private String passcode;

    @Enumerated(EnumType.STRING)
    @OrderColumn(name = "dietary_restrictions")
    private List<DietaryRestriction> dietaryRestrictions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "food_allergies")
    private List<FoodAllergies> foodAllergies = new ArrayList<>();

    @NonNull
    private String email;

    @NonNull
    private String name;

    private boolean attending;
}
