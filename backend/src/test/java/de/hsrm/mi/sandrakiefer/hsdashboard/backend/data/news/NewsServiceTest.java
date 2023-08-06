package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.news;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.news.tools.Headline;

@SpringBootTest
public class NewsServiceTest {

    @Autowired
    private NewsService newsService;

    @Test
    @DisplayName("Get latest news successfully")
    public void testGetNewsLatest() throws Exception {
        News n = newsService.getNewsLatest(5);
        assertEquals(5, n.getHeadlines().size());
        for (Headline h : n.getHeadlines()) {
            assertNotNull(h.getTitle());
            assertNotNull(h.getLink());
            assertTrue(h.getLink().startsWith("https://www.hessenschau.de/"));
        }
    }

    @Test
    @DisplayName("Get most clicked news successfully")
    public void testGetNewsMost() throws Exception {
        News n = newsService.getNewsMost(5);
        assertEquals(5, n.getHeadlines().size());
        for (Headline h : n.getHeadlines()) {
            assertNotNull(h.getTitle());
            assertNotNull(h.getLink());
            assertTrue(h.getLink().startsWith("https://www.hessenschau.de/"));
        }
    }
    
}
