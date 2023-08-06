package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions.LeagueNotFoundException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions.TeamNotInLeagueException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.tools.Match;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.tools.TableEntry;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.FetchJsonData;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class SportServiceImpl implements SportService {

    private Logger logger = LoggerFactory.getLogger(SportServiceImpl.class);

    @Value("${data.sport.season}")
    private String currentSeason;

    @Autowired
    private FetchJsonData fetchJsonData;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value="sport_info")
    public Sport getSportInfoOfTeam(String leagueShortcut, String team) throws LeagueNotFoundException, TeamNotInLeagueException, ApiNotAvailableException {
        int leagueId = 0;
        int teamId = 0;
        List<String> allTeams = getAllTeams(leagueShortcut);
        if (!allTeams.contains(team)) {
            String msg = String.format("No Sport information for team '%s' available", team);
            logger.error(msg);
            throw new TeamNotInLeagueException(msg);
        }
        int teamCount = allTeams.size();
        Sport s = new Sport();
        // current table position of the given team (and two before and after)
        String urlTable = String.format("https://www.openligadb.de/api/getbltable/%s/%s", leagueShortcut, currentSeason);
        JsonNode nodeTable = fetchJsonData.fetchDataFromUrl(urlTable);
        int currentPos = 0;
        for (int i = 0; i < teamCount; i++) {
            if (nodeTable.get(i).at("/ShortName").textValue().equals(team)) {
                currentPos = i;
                teamId = nodeTable.get(i).at("/TeamInfoId").asInt();
            }
        }
        List<TableEntry> table = new LinkedList<TableEntry>();
        int minus = 2;
        int plus = 2;
        if (currentPos == 0) {
            minus = 0;
            plus = 4;
        }
        if (currentPos == 1) {
            minus = 1;
            plus = 3;
        }
        if (currentPos == teamCount - 1) {
            minus = 4;
            plus = 0;
        }
        if (currentPos == teamCount - 2) {
            minus = 3;
            plus = 1;
        }
        for (int i = currentPos - minus; i <= currentPos + plus; i++) {
            TableEntry t = new TableEntry();
            t.setName(nodeTable.get(i).at("/ShortName").textValue());
            t.setIconLink(nodeTable.get(i).at("/TeamIconUrl").textValue());
            t.setDiff(nodeTable.get(i).at("/GoalDiff").asInt());
            t.setPosition(i + 1);
            t.setMatches(nodeTable.get(i).at("/Matches").asInt());
            t.setPoints(nodeTable.get(i).at("/Points").asInt());
            table.add(t);
        }
        s.setTable(table);
        // get league id
        String urlLeague = String.format("https://www.openligadb.de/api/getmatchdata/%s/%s/1", leagueShortcut, currentSeason);
        JsonNode nodeLeague = fetchJsonData.fetchDataFromUrl(urlLeague).get(0);
        leagueId = nodeLeague.at("/LeagueId").asInt();
        // get last match
        String urlLast = String.format("https://www.openligadb.de/api/getlastmatchbyleagueteam/%s/%s", leagueId, teamId);
        JsonNode nodeLast = fetchJsonData.fetchDataFromUrl(urlLast);
        if (nodeLast.at("/MatchDateTime").isNull()) {
            // first matchday (so last contains second match day)
            s.setFirstDay(true);
            String urlTmp = String.format("https://www.openligadb.de/api/getmatchdata/%s/%s/2", leagueShortcut, currentSeason);
            JsonNode nodeTmp = fetchJsonData.fetchDataFromUrl(urlTmp);
            int idTmp = 0;
            for (int i = 0; i < (teamCount / 2); i++) {
                if (nodeTmp.get(i).at("/Team1/ShortName").textValue().contains(team) || nodeTmp.get(i).at("/Team2/ShortName").textValue().contains(team)) idTmp = i;
            }
            Match mLast = new Match();
            mLast.setTeam1Name(nodeTmp.get(idTmp).at("/Team1/ShortName").textValue());
            mLast.setTeam1Score(-1);
            mLast.setTeam1IconLink(nodeTmp.get(idTmp).at("/Team1/TeamIconUrl").textValue());
            mLast.setTeam2Name(nodeTmp.get(idTmp).at("/Team2/ShortName").textValue());
            mLast.setTeam2Score(-1);
            mLast.setTeam2IconLink(nodeTmp.get(idTmp).at("/Team2/TeamIconUrl").textValue());
            mLast.setDate(nodeTmp.get(idTmp).at("/MatchDateTime").textValue());
            s.setLastMatch(mLast);
        } else {
            Match mLast = new Match();
            mLast.setTeam1Name(nodeLast.at("/Team1/ShortName").textValue());
            mLast.setTeam1Score(nodeLast.at("/MatchResults/0/PointsTeam1").asInt());
            mLast.setTeam1IconLink(nodeLast.at("/Team1/TeamIconUrl").textValue());
            mLast.setTeam2Name(nodeLast.at("/Team2/ShortName").textValue());
            mLast.setTeam2Score(nodeLast.at("/MatchResults/0/PointsTeam2").asInt());
            mLast.setTeam2IconLink(nodeLast.at("/Team2/TeamIconUrl").textValue());
            mLast.setDate(nodeLast.at("/MatchDateTime").textValue());
            s.setLastMatch(mLast);
        }
        // get next match
        String urlNext = String.format("https://www.openligadb.de/api/getnextmatchbyleagueteam/%s/%s", leagueId, teamId);
        JsonNode nodeNext = fetchJsonData.fetchDataFromUrl(urlNext);
        if (nodeNext.at("/MatchDateTime").isNull()) {
            // last matchday (so next contains second last match day)
            s.setLastDay(true);
            String urlTmp = String.format("https://www.openligadb.de/api/getmatchdata/%s/%s/33", leagueShortcut, currentSeason);
            JsonNode nodeTmp = fetchJsonData.fetchDataFromUrl(urlTmp);
            int idTmp = 0;
            for (int i = 0; i < (teamCount / 2); i++) {
                if (nodeTmp.get(i).at("/Team1/ShortName").textValue().contains(team) || nodeTmp.get(i).at("/Team2/ShortName").textValue().contains(team)) idTmp = i;
            }
            Match mNext = new Match();
            mNext.setTeam1Name(nodeTmp.get(idTmp).at("/Team1/ShortName").textValue());
            mNext.setTeam1Score(nodeTmp.get(idTmp).at("/MatchResults/0/PointsTeam1").asInt());
            mNext.setTeam1IconLink(nodeTmp.get(idTmp).at("/Team1/TeamIconUrl").textValue());
            mNext.setTeam2Name(nodeTmp.get(idTmp).at("/Team2/ShortName").textValue());
            mNext.setTeam2Score(nodeTmp.get(idTmp).at("/MatchResults/0/PointsTeam2").asInt());
            mNext.setTeam2IconLink(nodeTmp.get(idTmp).at("/Team2/TeamIconUrl").textValue());
            mNext.setDate(nodeTmp.get(idTmp).at("/MatchDateTime").textValue());
            s.setNextMatch(mNext);
        } else {
            Match mNext = new Match();
            mNext.setTeam1Name(nodeNext.at("/Team1/ShortName").textValue());
            mNext.setTeam1Score(-1);
            mNext.setTeam1IconLink(nodeNext.at("/Team1/TeamIconUrl").textValue());
            mNext.setTeam2Name(nodeNext.at("/Team2/ShortName").textValue());
            mNext.setTeam2Score(-1);
            mNext.setTeam2IconLink(nodeNext.at("/Team2/TeamIconUrl").textValue());
            mNext.setDate(nodeNext.at("/MatchDateTime").textValue());
            s.setNextMatch(mNext);
        }
        return s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value="sport_team")
    public List<String> getAllTeams(String leagueShortcut) throws LeagueNotFoundException, ApiNotAvailableException {
        String urlLeague = String.format("https://www.openligadb.de/api/getbltable/%s/%s", leagueShortcut, currentSeason);
        JsonNode nodeLeague = fetchJsonData.fetchDataFromUrl(urlLeague);
        if (nodeLeague.isEmpty()) {
            String msg = String.format("No Sport information for league '%s' available", leagueShortcut);
            logger.error(msg);
            throw new LeagueNotFoundException(msg);
        }
        String url = String.format("https://www.openligadb.de/api/getavailableteams/%s/%s", leagueShortcut, currentSeason);
        List<String> teams = new ArrayList<String>();
        JsonNode node = fetchJsonData.fetchDataFromUrl(url);
        int i = 0;
        while (true) {
            try {
                teams.add(node.get(i).at("/ShortName").textValue());
                i++;
                continue;
            } catch (NullPointerException e) {
                break;
            }
        }
        return teams;
    }
    
}
