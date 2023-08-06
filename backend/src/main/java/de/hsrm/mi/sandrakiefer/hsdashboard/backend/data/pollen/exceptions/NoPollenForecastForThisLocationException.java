package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown if no pollen data is available for the requested location.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoPollenForecastForThisLocationException extends RuntimeException {

    public NoPollenForecastForThisLocationException() {
    }

    public NoPollenForecastForThisLocationException(String message) {
        super(message);
    }
    
}
