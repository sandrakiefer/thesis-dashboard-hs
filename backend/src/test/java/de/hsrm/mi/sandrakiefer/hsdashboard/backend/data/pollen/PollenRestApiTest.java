package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen;

import java.util.List;
import java.util.Map;

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

@SpringBootTest
@AutoConfigureMockMvc
public class PollenRestApiTest {

    @Autowired
    private MockMvc mockmvc;

    @Value("#{${data.locations}}")
    private Map<String,String> locations;

    @Value("#{'${data.pollen.types}'.split(',')}")
    private List<String> pollentypes;
    
    @Test
    @WithMockUser
    @DisplayName("GET /api/pollen/{location}/{pollentypes} successful")
    public void testGetPollenFromLocation() throws Exception {
        for (String location : locations.keySet()) {
            mockmvc.perform(MockMvcRequestBuilders.get("/api/pollen/{location}/{pollentypes}", location, String.join(",", pollentypes)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location").value(location))
                .andExpect(jsonPath("$.pollution.*", hasSize(pollentypes.size())))
                .andExpect(jsonPath("$.pollution[*].name").exists())
                .andExpect(jsonPath("$.pollution[*].today").exists())
                .andExpect(jsonPath("$.pollution[*].tomorrow").exists());
        }
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/pollen/{location}/{pollentypes} bad request")
    public void testGetPollenFromLocation_badrequest() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/pollen/{location}/{pollentypes}", "Entenhausen", String.join(",", pollentypes))).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/pollen/pollentypes successful")
    public void testGetAllPollenTypes() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/pollen/pollentypes"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(8)))
            .andExpect(jsonPath("$[0]").value(pollentypes.get(0)))
            .andExpect(jsonPath("$[1]").value(pollentypes.get(1)))
            .andExpect(jsonPath("$[2]").value(pollentypes.get(2)))
            .andExpect(jsonPath("$[3]").value(pollentypes.get(3)))
            .andExpect(jsonPath("$[4]").value(pollentypes.get(4)))
            .andExpect(jsonPath("$[5]").value(pollentypes.get(5)))
            .andExpect(jsonPath("$[6]").value(pollentypes.get(6)))
            .andExpect(jsonPath("$[7]").value(pollentypes.get(7)));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/pollen/locations successful")
    public void testGetAllPollenLocations() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/pollen/locations"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(locations.size())));
    }
    
}
