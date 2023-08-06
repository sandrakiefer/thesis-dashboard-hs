package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of the JPA database functions for timeslots.
 */
public interface TimeslotRepo extends JpaRepository<Timeslot, Long> { }
