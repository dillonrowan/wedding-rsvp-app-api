package com.dillon.weddingrsvpapi.db;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for retrieving rsvp group data.
 */
@Repository
public interface RsvpGroupRepository extends JpaRepository<RsvpGroup, Long> {
    /**
     * Finds rsvp groups that have members whose names are similar to passed in string.
     *
     * @param name String to fetch rsvp groups with.
     * @return Rsvp record that belongs to provided passcode.
     */
    @Query("select r from RsvpGroup r inner join r.rsvps members where LOWER(members.name) LIKE LOWER( CONCAT('%', :name, '%') ) ")
    List<RsvpGroup> findAllByRsvpsName(String name);
}
