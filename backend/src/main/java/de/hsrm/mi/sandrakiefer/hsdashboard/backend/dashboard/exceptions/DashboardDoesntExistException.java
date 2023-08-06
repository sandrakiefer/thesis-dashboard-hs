package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when an interaction is to be made with a dashboard that does not exist.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DashboardDoesntExistException extends RuntimeException {

    public DashboardDoesntExistException() {
    }

    public DashboardDoesntExistException(String message) {
        super(message);
    }
    
}
