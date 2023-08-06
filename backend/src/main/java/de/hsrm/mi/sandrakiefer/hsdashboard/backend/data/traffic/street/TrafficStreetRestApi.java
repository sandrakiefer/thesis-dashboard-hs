package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * Rest controller for the interaction between frontend and backend to get the information about traffic on the streets, mapped on the path "/api/traffic/streets".
 */
@RestController
@RequestMapping("/api/traffic/streets")
public class TrafficStreetRestApi {

    @Autowired
    private TrafficStreetService trafficStreetService;
    
    /**
     * Endpoint to get the traffic informations of the given streets.
     * 
     * @param streets array of strings with the streetnames
     * @return trafficstreet object that contains all the required information
     * @throws ApiNotAvailableException the json app interface is not reachable (Bad Gateway 502)
     */
    @GetMapping(value = {"/{streets}", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficStreet getTrafficFromStreets(@PathVariable("streets") Optional<String[]> streets) throws ApiNotAvailableException {
        if (streets.isPresent()) {
            return trafficStreetService.getTrafficFromStreets(Arrays.asList(streets.get()));
        } else {
            return trafficStreetService.getTrafficFromStreets(new ArrayList<String>());
        }
    }

    /**
     * Endpoint to get a list of all freeways.
     * 
     * @return list of string with the street names
     */
    @GetMapping(value = "/A", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllStreetsA() {
        return trafficStreetService.getAllStreetsA();
    }

    /**
     * Endpoint to get a list of all national roads.
     * 
     * @return list of string with the street names
     */
    @GetMapping(value = "/B", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllStreetsB() {
        return trafficStreetService.getAllStreetsB();
    }

}
