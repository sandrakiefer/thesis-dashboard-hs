package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.bioweather;

import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * A service for retrieving bioweather data from the ard rest api and filtering out the essential information.
 */
public interface BioweatherService {
    
    /**
     * Obtain, process and return the bio weather of given categories.
     * 
     * @param categories possible categories for bioweather
     * @return bioweather object that contains all the required information
     * @throws ApiNotAvailableException the rest api interface of the ard is not reachable (bad gateway)
     */
    Bioweather getBioweather(List<String> categories) throws ApiNotAvailableException;

    /**
     * Get a list of all possible bio weather categories.
     * 
     * @return list of strings with all categories
     */
    List<String> getAllCategories();

}
