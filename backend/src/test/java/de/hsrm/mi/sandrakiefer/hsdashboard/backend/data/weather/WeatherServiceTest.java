package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather;

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

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather.exceptions.NoWeatherForecastForThisLocationException;

@SpringBootTest
public class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Value("#{${data.locations}}")
    private Map<String,String> locations;
    
    @Test
    @DisplayName("Get weather from location successfully")
    public void testGetWeatherFromLocation() throws Exception {
        for (String location : locations.keySet()) {
            Weather w = weatherService.getWeatherFromLocation(location);
            assertTrue(w.getLocation().equals(location));
            assertNotNull(w.getTempCurrent());
            assertNotNull(w.getRainProbabilityCurrent());
            assertNotNull(w.getHumidityCurrent());
            assertNotNull(w.getWindSpeedCurrent());
            assertNotNull(w.getIconCurrent());
            assertTrue(1 <= w.getIconCurrent() && w.getIconCurrent() <= 55);
            assertNotNull(w.getIconTextCurrent());
            assertNotNull(w.getTempMaxToday());
            assertNotNull(w.getTempMinToday());
            assertNotNull(w.getTempNextSixHours());
            assertEquals(6, w.getTempNextSixHours().size());
            assertNotNull(w.getIconNextSixHours());
            assertEquals(6, w.getIconNextSixHours().size());
            assertNotNull(w.getTempMinNextSixDays());
            assertEquals(6, w.getTempMinNextSixDays().size());
            assertNotNull(w.getTempMaxNextSixDays());
            assertEquals(6, w.getTempMaxNextSixDays().size());
            assertNotNull(w.getIconNextSixDays());
            assertEquals(6, w.getIconNextSixDays().size());
            for (int i : w.getIconNextSixDays()) {
                assertTrue(1 <= i && i <= 55);
            }
        }
    }

    @Test
    @DisplayName("Try to get weather from wrong location")
    public void testGetWeatherFromLocation_NoWeatherForecastForThisLocationException() {
        assertThrows(NoWeatherForecastForThisLocationException.class, () -> weatherService.getWeatherFromLocation("Entenhausen"));
    }

    @Test
    @DisplayName("Get all weather locations successfully")
    public void testGetAllWeatherLocations() {
        assertEquals(new ArrayList<String>(locations.keySet()), weatherService.getAllWeatherLocations());
    }
    
}
