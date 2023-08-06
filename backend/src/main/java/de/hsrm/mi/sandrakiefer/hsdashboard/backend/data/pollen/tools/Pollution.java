package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.pollen.tools;

/**
 * Helper class pollution for pollen class for storing the given information and export it as json.
 */
public class Pollution implements Comparable<Pollution> {

    private String name;

    private int today;
    private int tomorrow;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getToday() {
        return today;
    }

    public void setToday(int today) {
        this.today = today;
    }

    public int getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(int tomorrow) {
        this.tomorrow = tomorrow;
    }

    /**
     * Sorts pollution by name
     */
    @Override
    public int compareTo(Pollution o) {
        return this.getName().compareTo(o.getName());
    }

}
