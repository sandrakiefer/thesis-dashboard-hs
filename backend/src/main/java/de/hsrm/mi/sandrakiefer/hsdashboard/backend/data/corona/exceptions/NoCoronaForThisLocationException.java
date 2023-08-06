package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown if no corona data is available for the requested location.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoCoronaForThisLocationException extends RuntimeException {

    public NoCoronaForThisLocationException() {
    }

    public NoCoronaForThisLocationException(String message) {
        super(message);
    }
    
}
