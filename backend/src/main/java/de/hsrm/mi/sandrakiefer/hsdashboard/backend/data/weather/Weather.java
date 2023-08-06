package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Weather class for storing the given information and export it as json.
 */
public class Weather {

    private String location;

    private double tempCurrent;
    private double rainProbabilityCurrent;
    private double humidityCurrent;
    private double windSpeedCurrent;
    private int iconCurrent;
    private String iconTextCurrent;

    private double tempMaxToday;
    private double tempMinToday;

    private List<Double> tempNextSixHours = new ArrayList<Double>();
    private List<Integer> iconNextSixHours = new ArrayList<Integer>();

    private List<Double> tempMinNextSixDays = new ArrayList<Double>();
    private List<Double> tempMaxNextSixDays = new ArrayList<Double>();
    private List<Integer> iconNextSixDays = new ArrayList<Integer>();

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTempCurrent() {
        return tempCurrent;
    }

    public void setTempCurrent(double tempCurrent) {
        this.tempCurrent = tempCurrent;
    }

    public double getRainProbabilityCurrent() {
        return rainProbabilityCurrent;
    }

    public void setRainProbabilityCurrent(double rainProbabilityCurrent) {
        this.rainProbabilityCurrent = rainProbabilityCurrent;
    }

    public double getHumidityCurrent() {
        return humidityCurrent;
    }

    public void setHumidityCurrent(double humidityCurrent) {
        this.humidityCurrent = humidityCurrent;
    }

    public double getWindSpeedCurrent() {
        return windSpeedCurrent;
    }

    public void setWindSpeedCurrent(double windSpeedCurrent) {
        this.windSpeedCurrent = windSpeedCurrent;
    }

    public int getIconCurrent() {
        return iconCurrent;
    }

    public void setIconCurrent(int iconCurrent) {
        this.iconCurrent = iconCurrent;
    }

    public String getIconTextCurrent() {
        return iconTextCurrent;
    }

    public void setIconTextCurrent(String iconTextCurrent) {
        this.iconTextCurrent = iconTextCurrent;
    }

    public double getTempMinToday() {
        return tempMinToday;
    }

    public void setTempMinToday(double tempMinToday) {
        this.tempMinToday = tempMinToday;
    }

    public double getTempMaxToday() {
        return tempMaxToday;
    }

    public void setTempMaxToday(double tempMaxToday) {
        this.tempMaxToday = tempMaxToday;
    }

    public List<Double> getTempNextSixHours() {
        return tempNextSixHours;
    }

    public void setTempNextSixHours(List<Double> tempNextSixHours) {
        this.tempNextSixHours = tempNextSixHours;
    }

    public List<Integer> getIconNextSixHours() {
        return iconNextSixHours;
    }

    public void setIconNextSixHours(List<Integer> iconNextSixHours) {
        this.iconNextSixHours = iconNextSixHours;
    }

    public List<Double> getTempMinNextSixDays() {
        return tempMinNextSixDays;
    }

    public void setTempMinNextSixDays(List<Double> tempMinNextSixDays) {
        this.tempMinNextSixDays = tempMinNextSixDays;
    }

    public List<Double> getTempMaxNextSixDays() {
        return tempMaxNextSixDays;
    }

    public void setTempMaxNextSixDays(List<Double> tempMaxNextSixDays) {
        this.tempMaxNextSixDays = tempMaxNextSixDays;
    }

    public List<Integer> getIconNextSixDays() {
        return iconNextSixDays;
    }

    public void setIconNextSixDays(List<Integer> iconNextSixDays) {
        this.iconNextSixDays = iconNextSixDays;
    }

    @Override
    public String toString() {
        String s1 = String.format("Die aktuelle Temperatur in %s beträgt %f Grad und es ist %s. ", location,
                tempCurrent, iconTextCurrent);
        String s2 = String.format(
                "Die Wahrscheinlichkeit für die Niederschlag in der nächsten Stunde liegt bei %f Prozent, die Luftfeuchtigkeit liegt bei %f und die Windgeschwindigkeit bei %f. ",
                rainProbabilityCurrent, humidityCurrent, windSpeedCurrent);
        String s3 = String.format(
                "Der Höchstwert für die Lufttemperatur heute liegt bei %f Grad und der Tiefstwert bei %f Grad. ",
                tempMaxToday, tempMinToday);
        String s4 = String.format(
                "In des nächsten sechs Tagen sind Temperaturen zwischen %f Grad und %f Grad zu erwarten. ",
                Collections.max(tempMaxNextSixDays), Collections.min(tempMinNextSixDays));
        return s1 + s2 + s3 + s4;
    }

}
