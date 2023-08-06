package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when the sign up request for authentication is invalid. 
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SignUpRequestInvalidException extends RuntimeException {

    public SignUpRequestInvalidException() {
    }

    public SignUpRequestInvalidException(String message) {
        super(message);
    }
    
}
