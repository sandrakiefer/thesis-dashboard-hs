package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.bioweather;

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
 * Rest controller for the interaction between frontend and backend to get the information about the bioweather, mapped on the path "/api/bioweather".
 */
@RestController
@RequestMapping("/api/bioweather")
public class BioweatherRestApi {

    @Autowired
    private BioweatherService bioweatherService;

    /**
     * Endpoint to get the bioweather of given categories.
     * 
     * @param categories possible categories for bioweather
     * @return bioweather object that contains all the required information
     * @throws ApiNotAvailableException the rest api interface of the ard is not reachable (Bad Gateway 502)
     */
    @GetMapping(value = {"/{categories}", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Bioweather getBioweather(@PathVariable("categories") Optional<String[]> categories) throws ApiNotAvailableException {
        if (categories.isPresent()) {
            return bioweatherService.getBioweather(Arrays.asList(categories.get()));
        } else {
            return bioweatherService.getBioweather(new ArrayList<String>());
        }
    }

    /**
     * Endpoint to get a list of all possible bioweather categories.
     * 
     * @return list of strings with all categories
     */
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllCategories() {
        return bioweatherService.getAllCategories();
    }
    
}
