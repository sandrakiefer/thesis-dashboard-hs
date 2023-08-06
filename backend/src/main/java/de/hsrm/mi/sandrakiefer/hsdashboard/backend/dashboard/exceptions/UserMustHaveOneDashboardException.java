package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when the last dashboard of a user is attempted to be deleted.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserMustHaveOneDashboardException extends RuntimeException {

    public UserMustHaveOneDashboardException() {
    }

    public UserMustHaveOneDashboardException(String message) {
        super(message);
    }
    
}
