package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of the JPA database functions for widgets.
 */
public interface WidgetRepo extends JpaRepository<Widget, Long> { }
