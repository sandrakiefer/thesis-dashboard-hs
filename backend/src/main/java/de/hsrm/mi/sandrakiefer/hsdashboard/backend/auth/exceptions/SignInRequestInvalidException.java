package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when the sign in request for authentication is invalid. 
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SignInRequestInvalidException extends RuntimeException {

    public SignInRequestInvalidException() {
    }

    public SignInRequestInvalidException(String message) {
        super(message);
    }
    
}
