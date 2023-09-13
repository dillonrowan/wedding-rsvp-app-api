package com.dillon.weddingrsvpapi.db;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Repository for retrieving rsvp data.
 */
@Repository
public interface RsvpRepository extends JpaRepository<Rsvp, Long> {
}
