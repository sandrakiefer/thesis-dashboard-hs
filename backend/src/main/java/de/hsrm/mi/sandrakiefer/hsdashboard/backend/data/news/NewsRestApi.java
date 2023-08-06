package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * Rest controller for the interaction between frontend and backend to get the information about the news, mapped on the path "/api/news".
 */
@RestController
@RequestMapping("/api/news")
public class NewsRestApi {

    @Autowired
    private NewsService newsService;

    /**
     * Endpoint to get the five newest news headlines.
     * 
     * @param number number of news headlines
     * @return news object that contains all the required information
     * @throws ApiNotAvailableException the json app interface is not reachable (Bad Gateway 502)
     */
    @GetMapping(value = "/latest", params = "number", produces = MediaType.APPLICATION_JSON_VALUE)
    public News getNewsLatest(@RequestParam int number) throws ApiNotAvailableException {
        return newsService.getNewsLatest(number);
    }

    /**
     * Endpoint to get the five most clicked news headlines.
     * 
     * @param number number of news headlines
     * @return news object that contains all the required information
     * @throws ApiNotAvailableException the json app interface is not reachable (Bad Gateway 502)
     */
    @GetMapping(value = "/most", params = "number", produces = MediaType.APPLICATION_JSON_VALUE)
    public News getNewsMost(@RequestParam int number) throws ApiNotAvailableException {
        return newsService.getNewsMost(number);
    }
    
}
