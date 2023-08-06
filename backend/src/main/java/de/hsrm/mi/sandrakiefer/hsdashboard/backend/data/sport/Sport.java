package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport;

import java.util.ArrayList;
import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.tools.Match;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.tools.TableEntry;

/**
 * Sport class for storing the given information and export it as json.
 */
public class Sport {

    private List<TableEntry> table = new ArrayList<TableEntry>();

    private Match lastMatch;

    private Match nextMatch;

    private boolean firstDay = false;
    private boolean lastDay = false;

    public List<TableEntry> getTable() {
        return table;
    }

    public void setTable(List<TableEntry> table) {
        this.table = table;
    }

    public Match getLastMatch() {
        return lastMatch;
    }

    public void setLastMatch(Match lastMatch) {
        this.lastMatch = lastMatch;
    }

    public Match getNextMatch() {
        return nextMatch;
    }

    public void setNextMatch(Match nextMatch) {
        this.nextMatch = nextMatch;
    }

    public boolean isFirstDay() {
        return firstDay;
    }

    public void setFirstDay(boolean firstDay) {
        this.firstDay = firstDay;
    }

    public boolean isLastDay() {
        return lastDay;
    }

    public void setLastDay(boolean lastDay) {
        this.lastDay = lastDay;
    }

    @Override
    public String toString() {
        String tableString = "Der aktuelle Ausschnitt der Tabelle lautet ";
        for (TableEntry e : table) {
            tableString += e.getName() + ", ";
        }
        return (tableString.substring(0, tableString.length() - 2) + ". ") + lastMatch.toString() + nextMatch.toString();
    }
    
}
