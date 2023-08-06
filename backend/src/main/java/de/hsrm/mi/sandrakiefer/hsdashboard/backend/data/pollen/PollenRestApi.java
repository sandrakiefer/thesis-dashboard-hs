package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen;

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

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen.exceptions.NoPollenForecastForThisLocationException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * Rest controller for the interaction between frontend and backend to get the information about pollen, mapped on the path "/api/pollen".
 */
@RestController
@RequestMapping("/api/pollen")
public class PollenRestApi {

    @Autowired
    private PollenService pollenService;

    /**
     * Endpoint to get the pollen forecast of a given location.
     * 
     * @param location location in hessen
     * @param types pollen types from which the pollution is to be returned
     * @return pollen object that contains all the required information
     * @throws NoPollenForecastForThisLocationException the given location is not in hessen (Bad Request 400)
     * @throws ApiNotAvailableException the rest api interface of the ard is not reachable (Bad Gateway 502)
     */
    @GetMapping(value = {"/{location}/{types}", "/{location}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Pollen getPollenFromLocation(@PathVariable("location") String location, @PathVariable("types") Optional<String[]> types) throws NoPollenForecastForThisLocationException, ApiNotAvailableException {
        if (types.isPresent()) {
            return pollenService.getPollenFromLocation(location, Arrays.asList(types.get()));
        } else {
            return pollenService.getPollenFromLocation(location, new ArrayList<String>());
        }
    }

    /**
     * Endpoint to get a list of all possible pollen types.
     * 
     * @return list of strings with all types
     */
    @GetMapping(value = "/pollentypes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllPollenTypes() {
        return pollenService.getAllPollenTypes();
    }

    /**
     * Endpoint to get a list of all possible pollen forecast locations.
     * 
     * @return list of strings with all locations
     */
    @GetMapping(value = "/locations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllPollenLocations() {
        return pollenService.getAllPollenLocations();
    }
    
}
