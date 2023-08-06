package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street;

import java.util.ArrayList;
import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street.tools.Street;

/**
 * TrafficStreet class for storing the given information and export it as json.
 */
public class TrafficStreet {

    private List<Street> traffic = new ArrayList<Street>();

    public List<Street> getTraffic() {
        return traffic;
    }

    public void setTraffic(List<Street> traffic) {
        this.traffic = traffic;
    }

    @Override
    public String toString() {
        String ret = "Folgende Verkehrsmeldungen für die Straßen liegen vor: ";
        for (Street s : traffic) {
            ret += s.toString();
        }
        return ret;
    }
    
}
