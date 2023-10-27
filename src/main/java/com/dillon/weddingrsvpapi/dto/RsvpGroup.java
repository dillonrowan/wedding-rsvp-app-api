package com.dillon.weddingrsvpapi.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Id is mandatory.")
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
    @Builder.Default
    private Set<Rsvp> rsvps = new HashSet<>();

    /**
     * The email belonging to the rsvp group.
     */
    private String email;

    /**
     * The denotes is members of this group have privilege to modify their group.
     */
    @Column(name = "modify_group")
    private boolean modifyGroup;
}
