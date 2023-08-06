package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen.tools.Pollution;

/**
 * Pollen class for storing the given information and export it as json.
 */
public class Pollen {

    private String location;

    private List<Pollution> pollution = new ArrayList<Pollution>();

    @Value("#{${data.pollen.pollution}}")
    private Map<Integer,String> pollenstring;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Pollution> getPollution() {
        return pollution;
    }

    public void setPollution(List<Pollution> pollution) {
        this.pollution = pollution;
    }

    @Override
    public String toString() {
        String retToday = String.format("Die Pollenbelastung für heute in %s ist für ", location);
        for (Pollution p : pollution) {
            retToday += String.format("%s %s, ", p.getName(), pollenstring.get(p.getToday()));
        }
        String retTomorrow = String.format("Die Pollenbelastung für morgen in %s ist für ", location);
        for (Pollution p : pollution) {
            retTomorrow += String.format("%s %s, ", p.getName(), pollenstring.get(p.getTomorrow()));
        }
        return (retToday.substring(0, retToday.length() - 2) + ". ") + retTomorrow.substring(0, retTomorrow.length() - 2) + ".";
    }

}
