package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona;

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
public class CoronaRestApiTest {
    
    @Autowired
    private MockMvc mockmvc;

    @Value("#{${data.locations}}")
    private Map<String,String> locations;

    @Test
    @WithMockUser
    @DisplayName("GET /api/corona/{location} successful")
    public void testGetCoronaFromLocation() throws Exception {
        for (String location : locations.keySet()) {
            mockmvc.perform(MockMvcRequestBuilders.get("/api/corona/{location}", location))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.incidence").exists())
                .andExpect(jsonPath("$.cases").exists())
                .andExpect(jsonPath("$.compareLastWeek").exists())
                .andExpect(jsonPath("$.compareLastWeekPercent").exists());
        }
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/corona/{location} bad request")
    public void testGetCoronaFromLocation_badrequest() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/corona/{location}", "Entenhausen")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/corona/locations successful")
    public void testGetAllCoronaLocations() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/corona/locations"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(locations.keySet().size())));
    }

}
