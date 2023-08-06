package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * Service to load data from a json interface.
 */
@Service
public class FetchJsonData {

    private Logger logger = LoggerFactory.getLogger(FetchJsonData.class);
    
    /**
     * Loads a json object with the information of the given url.
     * 
     * @param url url from which the data should be loaded
     * @return json node object containing the data from the given url
     * @throws ApiNotAvailableException api of the given url is not available (bad gateway)
     */
    public JsonNode fetchDataFromUrl(String url) throws ApiNotAvailableException {
        try {
            return new ObjectMapper().readTree(new URL(url));
        } catch (IOException e) {
            String msg = String.format("URL '%s' not available", url);
            logger.error(msg);
            throw new ApiNotAvailableException(msg);
        }
    }
}
