package com.dillon.weddingrsvpapi.db;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for retrieving rsvp data.
 */
@Repository
public interface RsvpRepository extends JpaRepository<Rsvp, Long> {
}
