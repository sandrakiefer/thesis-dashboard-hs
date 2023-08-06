package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona.exceptions.NoCoronaForThisLocationException;

@SpringBootTest
public class CoronaServiceTest {

    @Autowired
    private CoronaService coronaService;

    @Value("#{${data.locations}}")
    private Map<String,String> locations;
    
    @Test
    @DisplayName("Get corona from location successfully")
    public void testGetCoronaFromLocation() throws Exception {
        for (String location : locations.keySet()) {
            Corona c = coronaService.getCoronaFromLocation(location);
            assertTrue(c.getLocation().equals(location));
            assertNotNull(c.getIncidence());
            assertNotNull(c.getCases());
            assertNotNull(c.getCompareLastWeek());
            assertNotNull(c.getCompareLastWeekPercent());
        }
    }

    @Test
    @DisplayName("Try to get corona from wrong location")
    public void testGetCoronaFromLocation_NoCoronaForThisLocationException() {
        assertThrows(NoCoronaForThisLocationException.class, () -> coronaService.getCoronaFromLocation("Entenhausen"));
    }

    @Test
    @DisplayName("Get all corona locations")
    public void testGetAllCoronaLocations() throws Exception {
        assertEquals(new ArrayList<String>(locations.keySet()), coronaService.getAllCoronaLocations());
    }
    
}
