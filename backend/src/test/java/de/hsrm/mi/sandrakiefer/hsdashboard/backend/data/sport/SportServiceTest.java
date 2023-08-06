package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions.LeagueNotFoundException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.exceptions.TeamNotInLeagueException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.sport.tools.TableEntry;

@SpringBootTest
public class SportServiceTest {
    
    @Autowired
    private SportService sportService;

    @Test
    @DisplayName("Get sport info of team successfully")
    public void testGetSportInfoOfTeam() throws Exception {
        for (String team : sportService.getAllTeams("bl1")) {
            Sport s = sportService.getSportInfoOfTeam("bl1", team);
            assertEquals(5, s.getTable().size());
            for(TableEntry t : s.getTable()) {
                assertNotNull(t.getName());
                assertNotNull(t.getDiff());
                assertNotNull(t.getPosition());
                assertNotNull(t.getMatches());
                assertNotNull(t.getPoints());
            }
            assertNotNull(s.getLastMatch());
            assertTrue(s.getLastMatch().getTeam1Name().equals(team) || s.getLastMatch().getTeam2Name().equals(team));
            assertNotNull(s.getNextMatch());
            assertTrue(s.getNextMatch().getTeam1Name().equals(team) || s.getNextMatch().getTeam2Name().equals(team));
            assertNotNull(s.isLastDay());
            assertNotNull(s.isFirstDay());
        }
    }

    @Test
    @DisplayName("Try to get sport info from wrong team")
    public void testGetSportInfoOfTeam_NoSportInfoForThisTeamException() throws Exception {
        assertThrows(TeamNotInLeagueException.class, () -> sportService.getSportInfoOfTeam("bl1", "Entenhausen"));
    }

    @Test
    @DisplayName("Try to get sport info from wrong league")
    public void testGetSportInfoOfTeam_LeagueNotFoundException() throws Exception {
        assertThrows(LeagueNotFoundException.class, () -> sportService.getSportInfoOfTeam("Entenhausen", "Mainz"));
    }

    @Test
    @DisplayName("Get all sport teams successfully")
    public void testGetAllTeams() throws Exception {
        assertNotNull(sportService.getAllTeams("bl1"));
        assertEquals(18, sportService.getAllTeams("bl1").size());
    }

    @Test
    @DisplayName("Try to get all sport teams from wrong league")
    public void testGetAllTeams_LeagueNotFoundException() throws Exception {
        assertThrows(LeagueNotFoundException.class, () -> sportService.getAllTeams("Entenhausen"));
    }

}
