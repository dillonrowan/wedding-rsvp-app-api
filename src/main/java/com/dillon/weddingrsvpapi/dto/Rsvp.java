package com.dillon.weddingrsvpapi.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @Data
 * @Entity
 * @Builder
 * @NoArgsConstructor
 * @AllArgsConstructor
 * @Table(name = "component_xref")
 * @Schema(description = SUB_COMPONENT_REF_DESCRIPTION)
 * @IdClass(SubComponentRef.SubComponentRefId.class)
 */

@Entity
@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@Builder
@Table(name = "rsvp")
@NoArgsConstructor
@AllArgsConstructor
public class Rsvp {

    @Id
    @NonNull
    private String passcode;

    @Enumerated(EnumType.STRING)
    @OrderColumn(name = "dietary_restrictions")
    @Builder.Default
    private List<DietaryRestriction> dietaryRestrictions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "food_allergies")
    @Builder.Default
    private List<FoodAllergies> foodAllergies = new ArrayList<>();

    @NonNull
    private String email;

    @NonNull
    private String name;

    private boolean attending;

    @Column(name = "accompanying_guests")
    private int accompanyingGuests;
}
