package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.corona;

/**
 * Corona class for storing the given information and export it as json.
 */
public class Corona {
    
    private String location;

    private int incidence;
    private int cases;

    private double compareLastWeek;
    private int compareLastWeekPercent;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIncidence() {
        return incidence;
    }

    public void setIncidence(int incidence) {
        this.incidence = incidence;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public double getCompareLastWeek() {
        return compareLastWeek;
    }

    public void setCompareLastWeek(double compareLastWeek) {
        this.compareLastWeek = compareLastWeek;
        if (compareLastWeek > 1) {
            this.compareLastWeekPercent = (int) ((compareLastWeek % 1) * 100);
        } else if (compareLastWeek < 1) {
            this.compareLastWeekPercent = 100 - ((int) ((compareLastWeek % 1) * 100));
        } else {
            this.compareLastWeekPercent = 0;
        }
    }

    public int getCompareLastWeekPercent() {
        return compareLastWeekPercent;
    }

    @Override
    public String toString() {
        String s1 = String.format("Die aktuelle 7-Tage-Inzidenz in %s beträgt %d. ", location, incidence);
        String s2 = String.format("Insgesamt sind %d Menschen in %s akut mit dem Coronavirus infiziert. ", cases, location);
        String s3 = String.format("Im Vergleich zur Vorwoche sind die Fälle um %d Prozent " + (compareLastWeek > 1 ? "gestiegen. " : "gesunken. "), compareLastWeekPercent);
        return s1 + s2 + s3;
    }
    
}
