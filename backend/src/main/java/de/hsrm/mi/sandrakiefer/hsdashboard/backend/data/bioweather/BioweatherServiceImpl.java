package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.bioweather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.bioweather.tools.Categorie;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.FetchJsonData;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class BioweatherServiceImpl implements BioweatherService {

    @Value("#{'${data.bioweather.categories}'.split(',')}")
    private List<String> biocategories;

    @Autowired
    private FetchJsonData fetchJsonData;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value="bioweather")
    public Bioweather getBioweather(List<String> categories) throws ApiNotAvailableException {
        Bioweather b = new Bioweather();
        List<Categorie> ret = new ArrayList<Categorie>();
        for (String s : biocategories) {
            if (categories.contains(s)) {
                List<String> keywords = new ArrayList<String>();
                JsonNode node = fetchJsonData.fetchDataFromUrl("https://api.prod.wetterdaten.hr.de/api/v1/biowetter/deutschland/Hessen_Rheinland-Pfalz_Saarland").get(0);
                for (int i = 0; i <= 6; i++) {
                    String name = node.at(String.format("/vorhersagen/0/effects/%d/name", i)).textValue();
                    if (name.toLowerCase().contains(s.toLowerCase())) {
                        String value = node.at(String.format("/vorhersagen/0/effects/%d/value", i)).textValue();
                        if (s.equals("Herz- und Kreislaufgeschehen")) value += " (" + name.split("\\(")[1];
                        if (s.equals("Rheumatische Beschwerden")) value += " (" + name.split(" ")[2] + " Form)";
                        keywords.add(value);
                    }
                }
                for (int i = 0; i <= 3; i++) {
                    String name = node.at(String.format("/vorhersagen/0/recommendations/%d/name", i)).textValue();
                    if (name.toLowerCase().contains(s.toLowerCase())) {
                        String[] tmp = node.at(String.format("/vorhersagen/0/recommendations/%d/value", i)).textValue().split(";");
                        for (String item : tmp) {
                            if (!item.equals("keine")) {
                                if (item.contains(", - ")) {
                                    keywords.add(item.replace(", - ", ", "));
                                } else {
                                    keywords.add(item);
                                }
                            }
                        }
                    }
                }
                Categorie c = new Categorie();
                c.setName(s);
                c.setEffects(keywords.toArray(new String[0]));
                ret.add(c);
            }
        }
        Collections.sort(ret);
        b.setCategories(ret);
        return b;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllCategories() {
        return biocategories;
    }

}
