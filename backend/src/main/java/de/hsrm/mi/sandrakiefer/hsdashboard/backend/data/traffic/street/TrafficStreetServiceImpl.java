package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.FetchJsonData;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street.tools.Street;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class TrafficStreetServiceImpl implements TrafficStreetService {

    @Value("#{${data.traffic.street.categories}}")
    private Map<String,String> trafficcategories;

    @Value("#{'${data.traffic.street.a}'.split(',')}")
    private List<String> autobahnen;

    @Value("#{'${data.traffic.street.b}'.split(',')}")
    private List<String> bundesstrassen;

    private String url = "https://app.hessenschau.de/verkehr/index.json";
    
    @Autowired
    private FetchJsonData fetchJsonData;
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value="trafficStreet")
    public TrafficStreet getTrafficFromStreets(List<String> streets) throws ApiNotAvailableException {
        TrafficStreet t = new TrafficStreet();
        List<Street> s = new ArrayList<>();
        JsonNode node = fetchJsonData.fetchDataFromUrl(url);
        int i = 0;
        while (node.at(String.format("/messages/%d", i)).isObject()) {
            if (node.at(String.format("/messages/%d/route/source", i)).textValue().equals("PRIVATE_TRANSPORT")) {
                String routeType = node.at(String.format("/messages/%d/route/type", i)).textValue();
                String routeNumber = node.at(String.format("/messages/%d/route/routeIdentifier", i)).textValue();
                String streetName = "";
                if (routeType.equals("NATIONWIDE_ROAD")) streetName = "B" + routeNumber;
                if (routeType.equals("FREEWAY")) streetName = "A" + routeNumber;
                if (streets.contains(streetName)) {
                    Street street = new Street();
                    street.setStreetName(streetName);
                    String categorie = node.at(String.format("/messages/%d/info", i)).textValue();
                    if (trafficcategories.containsKey(categorie)) {
                        street.setSymbol(trafficcategories.get(categorie));
                    } else {
                        street.setSymbol("Warnung");
                    }
                    String[] direction = node.at(String.format("/messages/%d/title", i)).textValue().split(" - ");
                    if (direction.length >= 2) {
                        street.setDirectionFrom(direction[0]);
                        street.setDirectionTo(direction[1]);
                    } else {
                        street.setDirectionFrom(node.at(String.format("/messages/%d/title", i)).textValue());
                        street.setDirectionTo(node.at(String.format("/messages/%d/title", i)).textValue());
                    }
                    street.setDesription(node.at(String.format("/messages/%d/subtitle", i)).textValue());
                    if (node.at(String.format("/messages/%d/description", i)).textValue().contains("beide")) {
                        street.setBothDirections(true);
                    } else {
                        street.setBothDirections(false);
                    }
                    s.add(street);
                }
            }
            i++;
        }
        t.setTraffic(s);
        return t;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllStreetsA() {
        return autobahnen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllStreetsB() {
        return bundesstrassen;
    }
    
}
