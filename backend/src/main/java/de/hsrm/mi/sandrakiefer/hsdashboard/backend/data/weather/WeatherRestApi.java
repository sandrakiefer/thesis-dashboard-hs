package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather.exceptions.NoWeatherForecastForThisLocationException;

/**
 * Rest controller for the interaction between frontend and backend to get the information about weather forecast, mapped on the path "/api/weather".
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherRestApi {

    @Autowired
    private WeatherService weatherService;

    /**
     * Endpoint to get the weather forecast of a given location.
     * 
     * @param location location in hessen
     * @return weather object that contains all the required information
     * @throws NoWeatherForecastForThisLocationException the given location is not in hessen (Bad Request 400)
     * @throws ApiNotAvailableException the rest api interface of the ard is not reachable (Bad Gateway 502)
     */
    @GetMapping(value = "/{location}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Weather getWeatherFromLocation(@PathVariable("location") String location) throws NoWeatherForecastForThisLocationException, ApiNotAvailableException {
        return weatherService.getWeatherFromLocation(location);
    }

    /**
     * Endpoint to get a list of all possible weather forecast locations.
     * 
     * @return list of strings with all locations
     */
    @GetMapping(value = "/locations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllWeatherLocations() {
        return weatherService.getAllWeatherLocations();
    }
    
}
