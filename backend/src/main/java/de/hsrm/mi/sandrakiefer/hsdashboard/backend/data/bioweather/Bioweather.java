package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.bioweather;

import java.util.ArrayList;
import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.bioweather.tools.Categorie;

/**
 * Bioweather class for storing the given information and export it as json.
 */
public class Bioweather {

    private List<Categorie> categories = new ArrayList<Categorie>();

    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        String ret = "Das aktuelle Biowetter sagt ";
        for (Categorie c : categories) {
            ret += String.format("%s für %s, ", c.getEffects()[0], c.getClass());
        }
        return ret.substring(0, ret.length() - 2) + ". ";
    }

}
