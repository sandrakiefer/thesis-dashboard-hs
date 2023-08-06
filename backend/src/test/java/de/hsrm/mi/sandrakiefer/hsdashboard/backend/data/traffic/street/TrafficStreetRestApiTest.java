package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class TrafficStreetRestApiTest {

    @Autowired
    private MockMvc mockmvc;

    @Value("#{'${data.traffic.street.a}'.split(',')}")
    private List<String> autobahnen;

    @Value("#{'${data.traffic.street.b}'.split(',')}")
    private List<String> bundesstrassen;

    @Test
    @WithMockUser
    @DisplayName("GET api/traffic/streets/{streets} successful")
    public void testGetTrafficFromStreets() throws Exception {
        List<String> streets = Stream.concat(autobahnen.stream(), bundesstrassen.stream()).collect(Collectors.toList());
        ResultActions actions = mockmvc.perform(MockMvcRequestBuilders.get("/api/traffic/streets/{streets}", streets))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.traffic").exists());
        String response = actions.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains("streetName") && response.contains("symbol") && response.contains("directionFrom") && response.contains("directionTo") && response.contains("desription"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET api/traffic/streets/A successful")
    public void testGetAllStreetsA() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/traffic/streets/A"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(autobahnen.size())));
    }

    @Test
    @WithMockUser
    @DisplayName("GET api/traffic/streets/B successful")
    public void testGetAllStreetsB() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/traffic/streets/B"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(bundesstrassen.size())));
    }

}
