package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of the JPA database functions for dashbord.
 */
public interface DashboardRepo extends JpaRepository<Dashboard, Long> {
    
    Optional<Dashboard> findById(long id);
    Iterable<Dashboard> findByStartDashboardTrue();
    Iterable<Dashboard> findByDefaultDashboardTrue();

}
