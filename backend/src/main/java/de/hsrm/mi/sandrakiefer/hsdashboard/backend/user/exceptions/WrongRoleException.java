package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when an interaction is made and the user does not have the correct role.
 * Represents the http response status codes 403 forbidden.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class WrongRoleException extends RuntimeException {

    public WrongRoleException() {
    }

    public WrongRoleException(String message) {
        super(message);
    }
    
}
