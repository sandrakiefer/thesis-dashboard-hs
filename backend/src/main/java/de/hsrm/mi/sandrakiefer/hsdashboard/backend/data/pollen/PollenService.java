package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen;

import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen.exceptions.NoPollenForecastForThisLocationException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * A service for obtaining pollen forecast data from the ard rest api and filtering out the essential information.
 */
public interface PollenService {
    
    /**
     * Obtain, process and return the pollen forecast of a given location.
     * 
     * @param location location in sessen
     * @param types pollen types from which the pollution is to be returned
     * @return pollen object that contains all the required information
     * @throws NoPollenForecastForThisLocationException the given location is not in hessen (bad request)
     * @throws ApiNotAvailableException the rest api interface of the ard is not reachable (bad gateway)
     */
    Pollen getPollenFromLocation(String location, List<String> types) throws NoPollenForecastForThisLocationException, ApiNotAvailableException;

    /**
     * Get a list of all possible pollen types.
     * 
     * @return list of strings with all types
     */
    List<String> getAllPollenTypes();

    /**
     * Get a list of all possible pollen forecast locations.
     * 
     * @return list of strings with all locations
     */
    List<String> getAllPollenLocations();

}
