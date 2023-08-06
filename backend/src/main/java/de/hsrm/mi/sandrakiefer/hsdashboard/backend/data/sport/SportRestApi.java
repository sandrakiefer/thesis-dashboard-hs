package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions.LeagueNotFoundException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions.TeamNotInLeagueException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

/**
 * Rest controller for the interaction between frontend and backend to get the information about sport, mapped on the path "/api/sport".
 */
@RestController
@RequestMapping("/api/sport")
public class SportRestApi {
    
    @Autowired
    private SportService sportService;

    /**
     * Endpoint to get the current league informations of a given team of the league.
     * 
     * @param leagueShortcut shortcut of the league on openligadb
     * @param team name of the team
     * @return sport object that contains all the required information
     * @throws LeagueNotFoundException the given league can not be found (Bad Request 400)
     * @throws TeamNotInLeagueException the given team is not in the first bundesliga (Bad Request 400)
     * @throws ApiNotAvailableException the json api interface of openligadb is not reachable (Bad Gateway 502)
     */
    @GetMapping(value = "/{leagueShortcut}/{team}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Sport getSportInfoOfTeam(@PathVariable("leagueShortcut") String leagueShortcut, @PathVariable("team") String team) throws LeagueNotFoundException, TeamNotInLeagueException, ApiNotAvailableException {
        return sportService.getSportInfoOfTeam(leagueShortcut, team);
    }

    /**
     * Endpoint to get a list of all teams of the league.
     * 
     * @param leagueShortcut shortcut of the league on openligadb
     * @return list of strings with all teams
     * @throws LeagueNotFoundException the given league can not be found (Bad Request 400)
     * @throws ApiNotAvailableException the json api interface of openligadb is not reachable (Bad Gateway 502)
     */
    @GetMapping(value = "/{leagueShortcut}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllTeams(@PathVariable("leagueShortcut") String leagueShortcut) throws LeagueNotFoundException, ApiNotAvailableException {
        return sportService.getAllTeams(leagueShortcut);
    }

}
