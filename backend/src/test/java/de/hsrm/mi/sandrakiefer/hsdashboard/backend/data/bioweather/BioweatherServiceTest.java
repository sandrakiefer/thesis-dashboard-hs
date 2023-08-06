package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.bioweather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BioweatherServiceTest {
    
    @Autowired
    private BioweatherService bioweatherService;

    @Value("#{'${data.bioweather.categories}'.split(',')}")
    private List<String> biocategories;

    @Test
    @DisplayName("Get bioweather successfully")
    public void testGetBioweather() throws Exception {
        for (String s : biocategories) {
            List<String> l = new ArrayList<String>();
            l.add(s);
            Bioweather b = bioweatherService.getBioweather(l);
            assertEquals(s, b.getCategories().iterator().next().getName());
            assertNotNull(b.getCategories().iterator().next().getEffects()[0]);
            assertNotNull(b.getCategories().iterator().next().getEffects()[1]);
            assertEquals(1, b.getCategories().size());
        }
        Bioweather b = bioweatherService.getBioweather(biocategories);
        assertEquals(biocategories.size(), b.getCategories().size());
        for (int i = 0; i < biocategories.size(); i++) {
            assertNotNull(b.getCategories().iterator().next().getName());
            assertNotNull(b.getCategories().iterator().next().getEffects());
            assertNotNull(b.getCategories().iterator().next().getEffects()[0]);
            assertNotNull(b.getCategories().iterator().next().getEffects()[1]);
        }
    }

    @Test
    @DisplayName("Get all bioweather categories successfully")
    public void testGetAllCategories() throws Exception {
        assertEquals(biocategories, bioweatherService.getAllCategories());
    }

}
