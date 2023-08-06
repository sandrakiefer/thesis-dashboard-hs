package de.hsrm.mi.sandrakiefer.hsdashboard.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service class to manage the casch for requests of the various prepared data and their interfaces.
 * 
 * The caching enables a relief of the external interfaces and a better runtime of the application.
 * 
 * The queries are stored for a certain period of time and only the stored data is replayed 
 * when the query is repeated, thus an optimization takes place.
 */
@Service
public class CacheService {
    
    @Autowired
    CacheManager cacheManager;

    /**
     * Automatically clear the stored data in the cache after 10 minutes.
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void evictAllcachesAtIntervals() {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
    
}
