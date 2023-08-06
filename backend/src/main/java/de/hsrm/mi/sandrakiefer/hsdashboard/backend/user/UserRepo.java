package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of the JPA database functions for users.
 */
public interface UserRepo extends JpaRepository<User, String> { }
