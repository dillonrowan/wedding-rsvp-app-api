package com.dillon.weddingrsvpapi.db;

import com.dillon.weddingrsvpapi.dto.DietaryRestriction;
import com.dillon.weddingrsvpapi.dto.FoodAllergies;
import com.dillon.weddingrsvpapi.dto.Rsvp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RsvpRepository extends JpaRepository<Rsvp, Long> {

//    @Query("SELECT id, passcode, dietaryRestrictions, foodAllergies, email, name, attending " +
//            "FROM Rsvp where passcode = :passcode")
//    Rsvp findRsvpByPasscode(@Param("passcode") String passcode);

    @Query("select r from Rsvp r where r.passcode = ?1")
    List<Rsvp> findByPasscode(String passcode);

//    @Modifying
//    @Query("update Rsvp r set r.dietaryRestrictions = ?1, r.foodAllergies = ?2, r.email = ?3, r.name = ?4, r.attending = ?5 where r.passcode = ?6")
//    void setRsvpByPasscode(Rsvp rsvp);


}
