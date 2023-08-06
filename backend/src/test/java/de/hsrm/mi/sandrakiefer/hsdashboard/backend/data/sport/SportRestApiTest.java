package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SportRestApiTest {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private SportService sportService;

    @Test
    @WithMockUser
    @DisplayName("GET /api/sport/{league}/{team} successful")
    public void testGetSportInfoOfTeam() throws Exception {
        for (String team : sportService.getAllTeams("bl1")) {
            mockmvc.perform(MockMvcRequestBuilders.get("/api/sport/{league}/{team}", "bl1", team))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.table.*", hasSize(5)))
                .andExpect(jsonPath("$.table.[*].name").exists())
                .andExpect(jsonPath("$.table.[*].diff").exists())
                .andExpect(jsonPath("$.table.[*].position").exists())
                .andExpect(jsonPath("$.table.[*].matches").exists())
                .andExpect(jsonPath("$.table.[*].points").exists())
                .andExpect(jsonPath("$.lastMatch.*", hasSize(7)))
                .andExpect(jsonPath("$.nextMatch.*", hasSize(7)))
                .andExpect(jsonPath("$.lastDay", anyOf(is(false), is(true))))
                .andExpect(jsonPath("$.firstDay", anyOf(is(false), is(true))));
        }
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/sport/{league}/{team} bad request (wrong team)")
    public void testGetSportInfoOfTeam_badrequestTeam() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/sport/{league}/{team}", "bl1", "Entenhausen")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/sport/{league}/{team} bad request (wrong league)")
    public void testGetSportInfoOfTeam_badrequestLeague() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/sport/{league}/{team}", "Entenhausen", "Mainz")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/sport/{league}/teams successful")
    public void testGetAllTeams() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/sport/bl1/teams"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(18)));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/sport/{league}/teams bad request")
    public void testGetAllTeams_badrequest() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/sport/Entenhausen/teams")).andExpect(status().isBadRequest());
    }
    
}
