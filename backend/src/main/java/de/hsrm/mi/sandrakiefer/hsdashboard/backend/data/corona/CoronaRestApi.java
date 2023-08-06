package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona.exceptions.NoCoronaForThisLocationException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * Rest controller for the interaction between frontend and backend to get the information about corona, mapped on the path "/api/corona".
 */
@RestController
@RequestMapping("/api/corona")
public class CoronaRestApi {
    
    @Autowired
    private CoronaService coronaService;

    /**
     * Endpoint to get information about corona cases of a given location.
     * 
     * @param location location in hessen
     * @return corona object that contains all the required information
     * @throws NoCoronaForThisLocationException the given location is not in hessen (Bad Request 400)
     * @throws ApiNotAvailableException the csv file from swr is not reachable (Bad Gateway 502)
     */
    @GetMapping(value = "/{location}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Corona getCoronaFromLocation(@PathVariable("location") String location) throws NoCoronaForThisLocationException, ApiNotAvailableException {
        return coronaService.getCoronaFromLocation(location);
    }

    /**
     * Endpoint to get a list of all possible locations which corona cases are available.
     * 
     * @return list of strings with all locations
     */
    @GetMapping(value = "/locations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllCoronaLocations() {
        return coronaService.getAllCoronaLocations();
    }

}
