package com.dillon.weddingrsvpapi.db;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RsvpRepository extends JpaRepository<Rsvp, Long> {

    @Query("SELECT id, passcode, dietaryRestrictions, foodAllergies, email, name, attending " +
            "FROM Rsvp where passcode = :passcode")
    Rsvp findRsvpByPasscode(@Param("passcode") String passcode);
}
