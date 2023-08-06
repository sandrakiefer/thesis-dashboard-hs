package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona.exceptions.NoCoronaForThisLocationException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.FetchCsvData;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * {@inheritDoc}
 */
@Service
public class CoronaServiceImpl implements CoronaService {

    private Logger logger = LoggerFactory.getLogger(CoronaServiceImpl.class);

    private String url = "https://d.swr.de/corona_header_react/data/kreise_corona_main.csv";

    @Value("#{${data.locations}}")
    private Map<String,String> locations;

    @Autowired
    private FetchCsvData fetchCsvData;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value="corona")
    public Corona getCoronaFromLocation(String location) throws ApiNotAvailableException, NoCoronaForThisLocationException {
        if (!locations.containsKey(location)) {
            String msg = String.format("No Corona Numbers for location '%s' available", location);
            logger.error(msg);
            throw new NoCoronaForThisLocationException(msg);
        }
        String locationSyntaxCSV = location;
        switch (location) {
            case "Landkreis Kassel":
                locationSyntaxCSV = "Kassel"; break;
            case "Kassel":
                locationSyntaxCSV = "Kassel (Stadt)"; break;
            case "Landkreis Offenbach":
                locationSyntaxCSV = "Offenbach"; break;
            case "Offenbach":
                locationSyntaxCSV = "Offenbach am Main"; break;
        }
        Corona c = new Corona();
        for (List<String> line : fetchCsvData.fetchDataFromUrl(url)) {
            if (line.contains(locationSyntaxCSV)) {
                c.setLocation(location);
                c.setIncidence((int) Math.round(Double.parseDouble(line.get(5))));
                c.setCases(Integer.parseInt(line.get(4)));
                c.setCompareLastWeek(Double.parseDouble(line.get(6)));
            }
        }
        return c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllCoronaLocations() {
        return new ArrayList<String>(locations.keySet());
    }
    
}
