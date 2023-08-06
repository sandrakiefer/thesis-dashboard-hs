package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown if an error occurs when interacting with a dashboard through the rest api.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DashboardApiException extends RuntimeException {

    public DashboardApiException() {
    }

    public DashboardApiException(String message) {
        super(message);
    }
    
}
