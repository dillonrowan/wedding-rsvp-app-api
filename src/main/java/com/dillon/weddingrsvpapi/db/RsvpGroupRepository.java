package com.dillon.weddingrsvpapi.db;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import com.dillon.weddingrsvpapi.dto.RsvpGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for retrieving rsvp group data.
 */
@Repository
public interface RsvpGroupRepository extends JpaRepository<RsvpGroup, Long> {
}
