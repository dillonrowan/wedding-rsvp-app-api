package com.dillon.weddingrsvpapi.dto;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //TODO: remove this and instead populate hashset of Rsvp in the RsvpGroup request
    @ManyToOne
    @JoinColumn(name = "group_id")
    private RsvpGroup rsvpGroup;

//    @ManyToOne
////    @JoinColumn(name="group_id", nullable=true)
//    private RsvpGroup rsvpGroup;

    /**
     * The name belonging to the rsvp
     */
    private String name;

    /**
     * Denotes if the guest is attending the wedding or not.
     */
    private boolean attending;
}
