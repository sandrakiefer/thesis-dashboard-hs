package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown if a start dashboard has already been added to the user.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyEditetDashboardsException extends RuntimeException {

    public UserAlreadyEditetDashboardsException() {
    }

    public UserAlreadyEditetDashboardsException(String message) {
        super(message);
    }
    
}
