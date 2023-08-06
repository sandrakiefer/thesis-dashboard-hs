package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.bioweather;

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

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class BioweatherRestApiTest {

    @Autowired
    private MockMvc mockmvc;

    @Value("#{'${data.bioweather.categories}'.split(',')}")
    private List<String> biocategories;

    @Test
    @WithMockUser
    @DisplayName("GET /api/bioweather/{categories} successful")
    public void testGetBioweather() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/bioweather/{categories}", String.join(",", biocategories)))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.categories").exists())
            .andExpect(jsonPath("$.categories.*", hasSize(biocategories.size())))
            .andExpect(jsonPath("$.categories.[*].name").exists())
            .andExpect(jsonPath("$.categories.[*].effects").exists())
            .andExpect(jsonPath("$.categories.[*].effects.[0]").exists())
            .andExpect(jsonPath("$.categories.[*].effects.[1]").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/bioweather/categories successful")
    public void testGetAllCategories() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/bioweather/categories"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[0]").value(biocategories.get(0)))
            .andExpect(jsonPath("$[1]").value(biocategories.get(1)))
            .andExpect(jsonPath("$[2]").value(biocategories.get(2)))
            .andExpect(jsonPath("$[3]").value(biocategories.get(3)));
    }
    
}
