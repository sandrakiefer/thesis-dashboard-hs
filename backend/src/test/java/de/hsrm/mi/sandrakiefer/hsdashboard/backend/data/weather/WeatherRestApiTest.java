package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherRestApiTest {

    @Autowired
    private MockMvc mockmvc;

    @Value("#{${data.locations}}")
    private Map<String,String> locations;

    @Test
    @WithMockUser
    @DisplayName("GET /api/weather/{location} successful")
    public void testGetWeatherFromLocation() throws Exception {
        for (String location : locations.keySet()) {
            mockmvc.perform(MockMvcRequestBuilders.get("/api/weather/{location}", location))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value(location))
                .andExpect(jsonPath("$.tempCurrent").exists())
                .andExpect(jsonPath("$.rainProbabilityCurrent").exists())
                .andExpect(jsonPath("$.humidityCurrent").exists())
                .andExpect(jsonPath("$.windSpeedCurrent").exists())
                .andExpect(jsonPath("$.iconCurrent").exists())
                .andExpect(jsonPath("$.iconTextCurrent").exists())
                .andExpect(jsonPath("$.tempMaxToday").exists())
                .andExpect(jsonPath("$.tempMinToday").exists())
                .andExpect(jsonPath("$.tempNextSixHours").exists())
                .andExpect(jsonPath("$.iconNextSixHours").exists())
                .andExpect(jsonPath("$.tempMinNextSixDays").exists())
                .andExpect(jsonPath("$.tempMaxNextSixDays").exists())
                .andExpect(jsonPath("$.iconNextSixDays").exists());
        }
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/weather/{location} bad request")
    public void testGetWeatherFromLocation_badrequest() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/weather/{location}", "Entenhausen")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/weather/locations successful")
    public void testGetAllWeatherLocations() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/weather/locations"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(locations.keySet().size())));
    }

}
