package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.news;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsRestApiTest {
    
    @Autowired
    private MockMvc mockmvc;

    @Test
    @WithMockUser
    @DisplayName("GET /api/news/latest successful")
    public void testGetNewsLatest() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/news/latest?number=5"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.headlines").exists())
            .andExpect(jsonPath("$.headlines.*", hasSize(5)))
            .andExpect(jsonPath("$.headlines.[*].title").exists())
            .andExpect(jsonPath("$.headlines.[*].link").exists())
            .andExpect(jsonPath("$.headlines.[0].link", startsWith("https://www.hessenschau.de/")))
            .andExpect(jsonPath("$.headlines.[1].link", startsWith("https://www.hessenschau.de/")))
            .andExpect(jsonPath("$.headlines.[2].link", startsWith("https://www.hessenschau.de/")))
            .andExpect(jsonPath("$.headlines.[3].link", startsWith("https://www.hessenschau.de/")))
            .andExpect(jsonPath("$.headlines.[4].link", startsWith("https://www.hessenschau.de/")));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/news/most successful")
    public void testGetNewsMost() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/news/most?number=5"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.headlines").exists())
            .andExpect(jsonPath("$.headlines.*", hasSize(5)))
            .andExpect(jsonPath("$.headlines.[*].title").exists())
            .andExpect(jsonPath("$.headlines.[*].link").exists())
            .andExpect(jsonPath("$.headlines.[0].link", startsWith("https://www.hessenschau.de/")))
            .andExpect(jsonPath("$.headlines.[1].link", startsWith("https://www.hessenschau.de/")))
            .andExpect(jsonPath("$.headlines.[2].link", startsWith("https://www.hessenschau.de/")))
            .andExpect(jsonPath("$.headlines.[3].link", startsWith("https://www.hessenschau.de/")))
            .andExpect(jsonPath("$.headlines.[4].link", startsWith("https://www.hessenschau.de/")));
    }

}
