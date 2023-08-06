package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown if the requested team can not be found for the requested league.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TeamNotInLeagueException extends RuntimeException {

    public TeamNotInLeagueException() {
    }

    public TeamNotInLeagueException(String message) {
        super(message);
    }
    
}
