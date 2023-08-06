package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.traffic.street.tools;

/**
 * Helper class street for traffic street class for storing the given information and export it as json.
 */
public class Street {
    
    private String streetName;

    private String symbol;

    private String directionFrom;
    private String directionTo;

    private String desription;

    private boolean bothDirections;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDirectionFrom() {
        return directionFrom;
    }

    public void setDirectionFrom(String directionFrom) {
        this.directionFrom = directionFrom;
    }

    public String getDirectionTo() {
        return directionTo;
    }

    public void setDirectionTo(String directionTo) {
        this.directionTo = directionTo;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public boolean isBothDirections() {
        return bothDirections;
    }

    public void setBothDirections(boolean bothDirections) {
        this.bothDirections = bothDirections;
    }

    @Override
    public String toString() {
        return String.format("%s in der %s Richtung %s nach %s %s. ", symbol, streetName, directionFrom, directionTo, desription);
    }

}
