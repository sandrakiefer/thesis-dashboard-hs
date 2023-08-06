package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.news;

import java.util.ArrayList;
import java.util.List;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.news.tools.Headline;

/**
 * News class for storing the given information and export it as json.
 */
public class News {

    private List<Headline> headlines = new ArrayList<Headline>();

    public List<Headline> getHeadlines() {
        return headlines;
    }

    public void setHeadlines(List<Headline> headlines) {
        this.headlines = headlines;
    }

    @Override
    public String toString() {
        String ret = "Die aktuellen Nachrichten lauten ";
        for (Headline h : headlines) {
            ret += String.format("%s, ", h.getTitle());
        }
        return ret.substring(0, ret.length() - 2) + ".";
    }

}
