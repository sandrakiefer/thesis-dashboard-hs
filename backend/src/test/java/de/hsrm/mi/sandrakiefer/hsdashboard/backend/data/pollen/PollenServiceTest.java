package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen.exceptions.NoPollenForecastForThisLocationException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen.tools.Pollution;

@SpringBootTest
public class PollenServiceTest {

    @Autowired
    private PollenService pollenService;

    @Value("#{${data.locations}}")
    private Map<String,String> locations;

    @Value("#{'${data.pollen.types}'.split(',')}")
    private List<String> pollentypes;

    @Test
    @DisplayName("Get pollen from location successfully")
    public void testGetPollenFromLocation() throws Exception {
        for (String location : locations.keySet()) {
            Pollen p = pollenService.getPollenFromLocation(location, pollentypes);
            assertEquals(location, p.getLocation());
            assertNotNull(p.getPollution());
            List<String> pollennames = new ArrayList<String>();
            for (Pollution pol : p.getPollution()) {
                assertNotNull(pol.getName());
                assertNotNull(pol.getToday());
                assertNotNull(pol.getTomorrow());
                pollennames.add(pol.getName());
                assertTrue(0 <= pol.getToday() &&  pol.getToday() <= 3);
                assertTrue(0 <= pol.getTomorrow() &&  pol.getTomorrow() <= 3);
            }
            assertTrue(pollennames.containsAll(pollentypes));
        }
    }

    @Test
    @DisplayName("Try to get pollen from wrong location")
    public void testGetPollenFromLocation_NoPollenForecastForThisLocationException() throws Exception {
        assertThrows(NoPollenForecastForThisLocationException.class, () -> pollenService.getPollenFromLocation("Entenhausen", pollentypes));
    }

    @Test
    @DisplayName("Get all pollen types successfully")
    public void testGetAllPollenTypes() throws Exception {
        assertEquals(pollentypes, pollenService.getAllPollenTypes());
    }

    @Test
    @DisplayName("Get all pollen locations successfully")
    public void testGetAllPollenLocations() throws Exception {
        assertEquals(new ArrayList<String>(locations.keySet()), pollenService.getAllPollenLocations());
    }
    
}
