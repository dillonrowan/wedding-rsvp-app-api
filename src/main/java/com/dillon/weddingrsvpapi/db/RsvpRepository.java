package com.dillon.weddingrsvpapi.db;

import com.dillon.weddingrsvpapi.dto.Rsvp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for retrieving rsvp data.
 */
@Repository
public interface RsvpRepository extends JpaRepository<Rsvp, String> {

    /**
     * Finds rsvp records for a given passcode
     *
     * @param passcode String to fetch a rsvp with.     *
     * @return Rsvp record that belongs to provided passcode.
     */
    @Query("select r from Rsvp r where r.passcode = ?1")
    List<Rsvp> findByPasscode(String passcode);
}
