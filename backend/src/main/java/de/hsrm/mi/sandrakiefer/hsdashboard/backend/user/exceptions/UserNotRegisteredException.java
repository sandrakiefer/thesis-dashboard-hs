package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when an attempt is made to interact with an unregistered user.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotRegisteredException extends RuntimeException {

    public UserNotRegisteredException() {
    }

    public UserNotRegisteredException(String message) {
        super(message);
    }
    
}
