package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * Service to load data from a csv interface.
 */
@Service
public class FetchCsvData {
    
    private Logger logger = LoggerFactory.getLogger(FetchCsvData.class);

    /**
     * Load a CSV file from the given url and render each line as an entry of a list.
     * 
     * @param url url from which the data should be loaded
     * @return list with each line of the CSV file and its comma separated entries as a list of strings
     * @throws ApiNotAvailableException api of the given url is not available (bad gateway)
     */
    public List<List<String>> fetchDataFromUrl(String url) throws ApiNotAvailableException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            List<List<String>> ret = new ArrayList<List<String>>();
            String line;
            while ((line = reader.readLine()) != null) {
                ret.add(Arrays.asList(line.split(",")));
            }
            return ret;
        } catch (IOException e) {
            String msg = String.format("CSV from URL '%s' is not available", url);
            logger.error(msg);
            throw new ApiNotAvailableException(msg);
        }
    }

}
