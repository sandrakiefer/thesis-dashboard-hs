package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.bioweather.tools;

/**
 * Helper class categorie for bioweather class for storing the given information and export it as json.
 */
public class Categorie implements Comparable<Categorie> {

    private String name;

    private String[] effects;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getEffects() {
        return effects;
    }

    public void setEffects(String[] effects) {
        this.effects = effects;
    }

    @Override
    public int compareTo(Categorie o) {
        return this.getName().compareTo(o.getName());
    }
    
}
