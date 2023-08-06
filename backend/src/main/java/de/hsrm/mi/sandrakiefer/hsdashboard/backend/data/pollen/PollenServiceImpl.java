package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen.exceptions.NoPollenForecastForThisLocationException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen.tools.Pollution;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.FetchJsonData;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class PollenServiceImpl implements PollenService {

    @Value("#{${data.pollen.regions}}")
    private Map<String,String> regions;

    @Value("#{${data.locations}}")
    private Map<String,String> locations;

    @Value("#{'${data.pollen.locations.nm}'.split(',')}")
    private List<String> locationsNM;

    @Value("#{'${data.pollen.locations.rs}'.split(',')}")
    private List<String> locationsRS;

    @Value("#{'${data.pollen.types}'.split(',')}")
    private List<String> pollentypes;

    private String url = "https://api.prod.wetterdaten.hr.de/api/v1/pollenflug/deutschland/Hessen";

    @Autowired
    private FetchJsonData fetchJsonData;
    
    private Logger logger = LoggerFactory.getLogger(PollenServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value="pollen")
    public Pollen getPollenFromLocation(String location, List<String> types) throws NoPollenForecastForThisLocationException, ApiNotAvailableException {
        String regionApi;
        if (locations.containsKey(location)) {
            if (locationsNM.contains(location)) {
                regionApi = regions.get("Nord- und Mittelhessen");
            } else {
                regionApi = regions.get("Rhein-Main und S\u00fcdhessen");
            }
        } else {
            String msg = String.format("No Pollen Forecast for region '%s' available", location);
            logger.error(msg);
            throw new NoPollenForecastForThisLocationException(msg);
        }
        Pollen p = new Pollen();
        JsonNode node = fetchJsonData.fetchDataFromUrl(url).get(0);
        p.setLocation(location);
        int id = 0;
        if (!node.at(String.format("/regionen/%d/name", id)).textValue().equals(regionApi)) id = 1;
        List<Pollution> pollution = new ArrayList<Pollution>();
        for (int i = 0; i <= 7; i++) {
            String name = node.at(String.format("/regionen/%d/pollen/%d/name", id, i)).textValue().replace("ae", "ä").replace("ss", "ß");
            if (types.contains(name)) {
                Pollution pol = new Pollution();
                pol.setName(name);
                String tmpToday = node.at(String.format("/regionen/%d/pollen/%d/tag00/belastungsstufe/index", id, i)).textValue();
                pol.setToday(Integer.parseInt(tmpToday.substring(tmpToday.length() - 1)));
                if (node.findValue("tag01") != null) {
                    String tmpTomorrow = node.at(String.format("/regionen/%d/pollen/%d/tag01/belastungsstufe/index", id, i)).textValue();
                    pol.setTomorrow(Integer.parseInt(tmpTomorrow.substring(tmpTomorrow.length() - 1)));
                } else {
                    String tmpTomorrow = node.at(String.format("/regionen/%d/pollen/%d/tag00/belastungsstufe/index", id, i)).textValue();
                    pol.setTomorrow(Integer.parseInt(tmpTomorrow.substring(tmpTomorrow.length() - 1)));
                }
                pollution.add(pol);
            }
        }
        Collections.sort(pollution);
        p.setPollution(pollution);
        return p;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllPollenTypes() {
        return pollentypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllPollenLocations() {
            return new ArrayList<String>(locations.keySet());
    }
    
}
