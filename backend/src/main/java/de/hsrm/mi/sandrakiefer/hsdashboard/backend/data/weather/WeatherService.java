package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather;

import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather.exceptions.NoWeatherForecastForThisLocationException;

/**
 * A service for obtaining weather forecast data from the ard rest api and filtering out the essential information.
 */
public interface WeatherService {

    /**
     * Obtain, process and return the weather forecast of a given location.
     * 
     * @param location location in hessen
     * @return weather object that contains all the required information
     * @throws NoWeatherForecastForThisLocationException the given location is not in hessen (bad request)
     * @throws ApiNotAvailableException the rest api interface of the ard is not reachable (bad gateway)
     */
    Weather getWeatherFromLocation(String location) throws NoWeatherForecastForThisLocationException, ApiNotAvailableException;

    /**
     * Get a list of all possible weather forecast locations.
     * 
     * @return list of strings with all locations
     */
    List<String> getAllWeatherLocations();
    
}
