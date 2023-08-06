package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when an interaction is to be made with a dashboard that does not belong to the user.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DashboardDoesNotBelongToTheUserException extends RuntimeException {

    public DashboardDoesNotBelongToTheUserException() {
    }

    public DashboardDoesNotBelongToTheUserException(String message) {
        super(message);
    }
    
}
