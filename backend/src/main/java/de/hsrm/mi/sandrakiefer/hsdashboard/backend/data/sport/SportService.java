package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport;

import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions.LeagueNotFoundException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions.TeamNotInLeagueException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * A service for obtaining sport data from the openligadb json api and filtering out the essential information.
 */
public interface SportService {

    /**
     * Obtain, process and return the current league informations of a given team of the league.
     * 
     * @param leagueShortcut shortcut of the league on openligadb
     * @param team name of the team
     * @return sport object that contains all the required information
     * @throws LeagueNotFoundException the given league can not be found (bad request)
     * @throws TeamNotInLeagueException the given team is not in the first bundesliga (bad request)
     * @throws ApiNotAvailableException the json api interface of openligadb is not reachable (bad gateway)
     */
    Sport getSportInfoOfTeam(String leagueShortcut, String team) throws LeagueNotFoundException, TeamNotInLeagueException, ApiNotAvailableException;
    
    /**
     * Get a list of all teams of the league.
     * 
     * @param leagueShortcut shortcut of the league on openligadb
     * @return list of strings with all teams
     * @throws LeagueNotFoundException the given league can not be found (bad request)
     * @throws ApiNotAvailableException the json api interface of openligadb is not reachable (bad gateway)
     */
    List<String> getAllTeams(String leagueShortcut) throws LeagueNotFoundException, ApiNotAvailableException;

}
