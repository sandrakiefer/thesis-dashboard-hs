package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.news;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.news.tools.Headline;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.FetchJsonData;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private FetchJsonData fetchJsonData;

    private String titleMorgen = "Hessen am Morgen: Die Nachrichten im Ticker";
    private String titleCorona = "Coronavirus in Hessen: Die wichtigsten Nachrichten im Ticker";
    private String titleSport = "Aktuelles von Eintracht Frankfurt & Darmstadt 98: News im Bundesliga-Ticker";
    private String titleKultur = "Meldungen aus der hessischen Kulturlandschaft im Ticker";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public News getNewsLatest(int number) throws ApiNotAvailableException {
        return getNews("https://app.hessenschau.de/neu/neueste-beitraege-filter-100.json", number);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public News getNewsMost(int number) throws ApiNotAvailableException {
        return getNews("https://app.hessenschau.de/hs-app-alles-heute-100.json", number);
    }

    /**
     * Outsourced function to retrieve the news headlines.
     * 
     * @param url url to query the data
     * @return news object that contains all the required information
     * @throws ApiNotAvailableException the json app interface is not reachable (bad gateway)
     */
    @Cacheable(value="news")
    private News getNews(String url, int number) throws ApiNotAvailableException {
        News n = new News();
        List<Headline> headlines = new ArrayList<Headline>();
        JsonNode node = fetchJsonData.fetchDataFromUrl(url);
        for (int i = 0; i < number; i++) {
            Headline h = new Headline();
            h.setLink(node.at(String.format("/teasers/%d/sharingUrl", i)).textValue());
            h.setTitle(node.at(String.format("/teasers/%d/hr:title", i)).textValue());
            // catch and handle special cases ticker
            String cmsTitle = node.at(String.format("/teasers/%d/sophora:id", i)).textValue();
            if (cmsTitle.contains("hessen-am-morgen")) h.setTitle(titleMorgen);
            if (cmsTitle.contains("corona-hessen-ticker")) h.setTitle(titleCorona);
            if (cmsTitle.contains("bundesliga-ticker")) h.setTitle(titleSport);
            if (cmsTitle.contains("hessen-kultur-ticker")) h.setTitle(titleKultur);
            headlines.add(h);
        }
        n.setHeadlines(headlines);
        return n;
    }
    
}
