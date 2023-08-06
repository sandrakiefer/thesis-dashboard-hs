package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.RepoCleaner;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.Dashboard;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.DashboardService;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesNotBelongToTheUserException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesntExistException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Weekday;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserAlreadyExistsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserNotRegisteredException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Role;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DashboardService dashboardService;
    
    @Autowired
    private RepoCleaner repoCleaner;

    private String firstname = "Jöndhard";
    private String lastname = "Biffel";
    private String email = "joendhard.biffel@hs-rm.de";
    private String password = "Sich3re$PasSw0rD17";

    @BeforeEach
    @AfterEach
    public void cleanUp(){
        repoCleaner.clean();
    }

    @Test
    @DisplayName("Find user by mail successfully")
    public void testFindUserByMail() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        assertNotNull(userService.findUserByMail(email));
        assertEquals(u.getFirstname(), userService.findUserByMail(email).getFirstname());
        assertEquals(u.getLastname(), userService.findUserByMail(email).getLastname());
        assertNotNull(userService.findUserByMail(email).getTimeslots());
        assertEquals(3, userService.findUserByMail(email).getTimeslots().size());
        assertNotNull(userService.findUserByMail(email).getDashboards());
        assertEquals(1, userService.findUserByMail(email).getDashboards().size());
        assertEquals(u.getPassword(), userService.findUserByMail(email).getPassword());
        assertEquals("USER", userService.findUserByMail(email).getRole().toString());
    }

    @Test
    @DisplayName("Try to find user with not existing mail")
    public void testFindUserByMail_NotExistingMail() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> userService.findUserByMail("test@123.de"));
    }

    @Test
    @DisplayName("Register user successfully")
    public void testRegisterUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        assertNotNull(u);
        assertEquals(1, u.getDashboards().size());
        assertEquals("Hessenschau Start-Dashboard", u.getDashboards().iterator().next().getName());
        assertFalse(u.getDashboards().iterator().next().isStartDashboard());
    }

    @Test
    @DisplayName("Try to register already registered user")
    public void testRegisterUser_UserAlreadyExists() throws Exception {
        User u1 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u1);
        User u2 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(u2));
    }

    @Test
    @DisplayName("Get timeslots of a user")
    public void testGetTimeslotsOfUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        assertEquals(3, userService.getTimeslotsOfUser(email).size());
        assertEquals(userService.getTimeslotsOfUser(email).get(0), new Timeslot(LocalTime.of(6, 0), LocalTime.of(11, 59, 59)));
        assertEquals(userService.getTimeslotsOfUser(email).get(1), new Timeslot(LocalTime.of(12, 0), LocalTime.of(16, 59, 59)));
        assertEquals(userService.getTimeslotsOfUser(email).get(2), new Timeslot(LocalTime.of(17, 0), LocalTime.of(21, 59, 59)));
    }

    @Test
    @DisplayName("Try to get timeslots of not registered user")
    public void testGetTimeslotsOfUser_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> userService.getTimeslotsOfUser("test@123.de"));
    }

    @Test
    @DisplayName("Change timeslots from user successfully")
    public void testChangeTimeslots() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        assertEquals(u.getTimeslots().get(0), new Timeslot(LocalTime.of(6, 0), LocalTime.of(11, 59, 59)));
        assertEquals(u.getTimeslots().get(1), new Timeslot(LocalTime.of(12, 0), LocalTime.of(16, 59, 59)));
        assertEquals(u.getTimeslots().get(2), new Timeslot(LocalTime.of(17, 0), LocalTime.of(21, 59, 59)));
        List<Timeslot> t = new ArrayList<Timeslot>();
        t.add(0, new Timeslot(LocalTime.of(0, 0), LocalTime.of(11, 59, 59)));
        t.add(1, new Timeslot(LocalTime.of(12, 0), LocalTime.of(16, 59, 59)));
        t.add(2, new Timeslot(LocalTime.of(17, 0), LocalTime.of(23, 59, 59)));
        List<Timeslot> ret = userService.changeTimeslots(email, t);
        assertEquals(ret.get(0), new Timeslot(LocalTime.of(0, 0), LocalTime.of(11, 59, 59)));
        assertEquals(ret.get(1), new Timeslot(LocalTime.of(12, 0), LocalTime.of(16, 59, 59)));
        assertEquals(ret.get(2), new Timeslot(LocalTime.of(17, 0), LocalTime.of(23, 59, 59)));
    }

    @Test
    @DisplayName("Try to change timeslots from not registered user")
    public void testChangeTimeslots_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> userService.changeTimeslots("test@123.de", new ArrayList<Timeslot>()));
    }

    @Test
    @DisplayName("Get occupied timeslots of all weekdays from user successfully")
    public void testGetOccupiedTimeslotsOfAllWeekdays() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        long id = u.getDashboards().iterator().next().getId();
        List<Weekday> weekdays = userService.getOccupiedTimeslotsOfAllWeekdays(email, id);
        for (Weekday w : weekdays) {
            assertEquals(0, w.getTimeslots().size());
        }
        Dashboard newDashboard = dashboardService.newEmptyDashboard(email, "tmp");
        String weekdaysString[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
        List<Weekday> weekdaysChange = new ArrayList<Weekday>();
        for (int i = 0; i < 7; i++) {
            if (i == 2) {
                weekdaysChange.add(new Weekday(weekdaysString[i], u.getTimeslots(), i));
            } else {
                weekdaysChange.add(new Weekday(weekdaysString[i], new ArrayList<Timeslot>(), i));
            }
        }
        newDashboard.setWeekdays(weekdaysChange);
        dashboardService.editDashboard(email, newDashboard);
        List<Weekday> weekdaysNew = userService.getOccupiedTimeslotsOfAllWeekdays(email, id);
        for (Weekday w : weekdaysNew) {
            if (w.getWeekdayIndex() == 2) {
                assertEquals(u.getTimeslots().size(), w.getTimeslots().size());
                for (int i = 0; i < u.getTimeslots().size(); i++) {
                    assertEquals(u.getTimeslots().get(i), w.getTimeslots().get(i));
                }
            } else {
                assertEquals(0, w.getTimeslots().size());
            }
        }
    }

    @Test
    @DisplayName("Try to get occupied timeslots from not registered user")
    public void testGetOccupiedTimeslotsOfAllWeekdays_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> userService.getOccupiedTimeslotsOfAllWeekdays("test@123.de", 17));
    }

    @Test
    @DisplayName("Try to get occupied timeslots for not existing dashboard")
    public void testGetOccupiedTimeslotsOfAllWeekdays_DashboardDoesntExists() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        assertThrows(DashboardDoesntExistException.class, () -> userService.getOccupiedTimeslotsOfAllWeekdays(email, 17));
    }

    @Test
    @DisplayName("Try to get occupied timeslots from dashboard of a other user")
    public void testGetOccupiedTimeslotsOfAllWeekdays_DashboardDoesNotBelongToTheUser() throws Exception {
        User u1 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u1);
        User u2 = new User("joghurta.biffel@hs-rm.de", "Joghurta", "Biffel", new HashSet<Dashboard>(), "p4(/dhjHJjb(897", Role.USER);
        userService.registerUser(u2);
        assertThrows(DashboardDoesNotBelongToTheUserException.class, () -> userService.getOccupiedTimeslotsOfAllWeekdays(email, userService.findUserByMail("joghurta.biffel@hs-rm.de").getDashboards().iterator().next().getId()));
    }
    
}
