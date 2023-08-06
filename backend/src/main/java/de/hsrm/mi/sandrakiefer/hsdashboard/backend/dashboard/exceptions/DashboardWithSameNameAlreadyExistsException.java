package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when a new dashboard is to be created and the user already has a dashboard with the same name.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DashboardWithSameNameAlreadyExistsException extends RuntimeException {

    public DashboardWithSameNameAlreadyExistsException() {
    }

    public DashboardWithSameNameAlreadyExistsException(String message) {
        super(message);
    }
    
}
