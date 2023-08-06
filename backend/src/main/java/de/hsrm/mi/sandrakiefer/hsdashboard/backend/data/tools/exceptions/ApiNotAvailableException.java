package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown if the api for data fetching is not available.
 * Represents the http response status codes 502 bad gateway.
 */
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ApiNotAvailableException extends IOException {

    public ApiNotAvailableException() { }

    public ApiNotAvailableException(String message) {
        super(message);
    }
    
}
