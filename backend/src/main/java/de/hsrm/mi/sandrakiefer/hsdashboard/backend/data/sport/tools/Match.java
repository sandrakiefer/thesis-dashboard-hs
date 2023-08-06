package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helper class match for sport class for storing the given information and export it as json.
 */
public class Match {

    private String team1Name;
    private int team1Score;
    private String team1IconLink;

    private String team2Name;
    private int team2Score;
    private String team2IconLink;

    private String date;

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public String getTeam1IconLink() {
        return team1IconLink;
    }

    public void setTeam1IconLink(String team1IconLink) {
        this.team1IconLink = team1IconLink;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public String getTeam2IconLink() {
        return team2IconLink;
    }

    public void setTeam2IconLink(String team2IconLink) {
        this.team2IconLink = team2IconLink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (team1Score == -1) {
            return String.format("Das Spiel zwischen %s und %s findet am %s statt. ", team1Name, team2Name, dateTime.format(formatter));
        } else {
            return String.format("Das Spiel am %s zwischen %s und %s ging %d zu %d aus. ", dateTime.format(formatter), team1Name, team2Name, team1Score, team2Score);
        }
    }
    
}
