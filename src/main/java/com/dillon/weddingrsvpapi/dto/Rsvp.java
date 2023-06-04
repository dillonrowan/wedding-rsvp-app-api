package com.dillon.weddingrsvpapi.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

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
    @Id
    @GeneratedValue
    private long id;

    private String passcode;

    @Column(name = "dietary_restrictions")
    @ElementCollection
    private List<String> dietaryRestrictions = new ArrayList<>();


//    @Column(columnDefinition = "text[]")
    @Column(name = "food_allergies")
    @ElementCollection
    private List<String> foodAllergies = new ArrayList<>();

    private String email;

    private String name;

    private boolean attending;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Rsvp rsvp = (Rsvp) o;
        return Objects.equals(id, rsvp.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
