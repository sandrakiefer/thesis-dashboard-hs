package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street.tools.Street;

@SpringBootTest
public class TrafficStreetServiceTest {
    
    @Autowired
    private TrafficStreetService trafficStreetService;

    @Value("#{'${data.traffic.street.a}'.split(',')}")
    private List<String> autobahnen;

    @Value("#{'${data.traffic.street.b}'.split(',')}")
    private List<String> bundesstrassen;

    @Test
    @DisplayName("Get traffic from streets successfully")
    public void testGetTrafficFromStreets() throws Exception {
        List<String> streets = Stream.concat(autobahnen.stream(), bundesstrassen.stream()).collect(Collectors.toList());
        TrafficStreet t = trafficStreetService.getTrafficFromStreets(streets);
        if (t.getTraffic().size() > 0) {
            for (Street s : t.getTraffic()) {
                assertTrue(streets.contains(s.getStreetName()));
                assertNotNull(s.getSymbol());
                assertNotNull(s.getDirectionFrom());
                assertNotNull(s.getDirectionTo());
                assertNotNull(s.getDesription());
            }
        }
        List<String> test = new ArrayList<String>();
        test.add("Entenhausen");
        t = trafficStreetService.getTrafficFromStreets(test);
        assertEquals(0, t.getTraffic().size());
    }

    @Test
    @DisplayName("Get all streets of type Autobahn successfully")
    public void testGetAllStreetsA() throws Exception {
        assertEquals(autobahnen, trafficStreetService.getAllStreetsA());
    }

    @Test
    @DisplayName("Get all strees of type Bundesstraße successfully")
    public void testGetAllStreetsB() throws Exception {
        assertEquals(bundesstrassen, trafficStreetService.getAllStreetsB());
    }
}
