package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street;

import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * A service for obtaining traffic street data from the json app interface and filtering out the essential information.
 */
public interface TrafficStreetService {
    
    /**
     * Obtain, process and return the traffic informations of the given streets.
     * 
     * @param streets list of streetnames
     * @return trafficstreet object that contains all the required information
     * @throws ApiNotAvailableException the json app interface is not reachable (bad gateway)
     */
    TrafficStreet getTrafficFromStreets(List<String> streets) throws ApiNotAvailableException;

    /**
     * Get a list of all freeways.
     * 
     * @return list of string with the street names
     */
    List<String> getAllStreetsA();

    /**
     * Get a list of all national roads.
     * 
     * @return list of string with the street names
     */
    List<String> getAllStreetsB();

}
