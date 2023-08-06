package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown if the requested league can not be found.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LeagueNotFoundException extends RuntimeException {

    public LeagueNotFoundException() {
    }

    public LeagueNotFoundException(String message) {
        super(message);
    }
    
}
