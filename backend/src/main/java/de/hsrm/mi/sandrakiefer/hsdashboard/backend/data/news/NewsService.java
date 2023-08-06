package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.news;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * A service for obtaining news headlines from the json hessenschau app interface and filtering out the essential information.
 */
public interface NewsService {
    
    /**
     * Obtain, process and return the five newest news headlines.
     * 
     * @param number number of news headlines
     * @return news object that contains all the required information
     * @throws ApiNotAvailableException the json app interface is not reachable (bad gateway)
     */
    News getNewsLatest(int number) throws ApiNotAvailableException;

    /**
     * Obtain, process and return the five most clicked news headlines.
     * 
     * @param number number of news headlines
     * @return news object that contains all the required information
     * @throws ApiNotAvailableException the json app interface is not reachable (bad gateway)
     */
    News getNewsMost(int number) throws ApiNotAvailableException;

}
