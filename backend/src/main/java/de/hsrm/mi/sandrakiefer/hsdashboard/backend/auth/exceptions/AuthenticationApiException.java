package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when an eroor occurs during request for authentication. 
 * Represents the http response status codes 401 unauthorized.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationApiException extends RuntimeException {

    public AuthenticationApiException() {
    }

    public AuthenticationApiException(String message) {
        super(message);
    }

}
