package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona;

import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona.exceptions.NoCoronaForThisLocationException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * A service for obtaining current corona cases from the csv file from swr and filtering out the essential information.
 */
public interface CoronaService {

    /**
     * Obtain, process and return the corona informations of a given location.
     * 
     * @param location location in hessen
     * @return corona object that contains all the required information
     * @throws NoCoronaForThisLocationException the given location is not in hessen (bad request)
     * @throws ApiNotAvailableException the csv file from swr is not reachable (bad gateway)
     */
    Corona getCoronaFromLocation(String location) throws NoCoronaForThisLocationException, ApiNotAvailableException;
    
    /**
     * Get a list of all possible locations which corona cases are available.
     * 
     * @return list of strings with all locations
     */
    List<String> getAllCoronaLocations();

}
