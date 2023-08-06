package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown if no weather data is available for the requested location.
 * Represents the http response status codes 400 bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoWeatherForecastForThisLocationException extends RuntimeException {

    public NoWeatherForecastForThisLocationException() {
    }

    public NoWeatherForecastForThisLocationException(String message) {
        super(message);
    }
    
}
