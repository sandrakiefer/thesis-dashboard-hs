package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.RepoCleaner;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesNotBelongToTheUserException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesntExistException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardWithSameNameAlreadyExistsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.UserAlreadyEditetDashboardsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.UserMustHaveOneDashboardException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.DashboardInfo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Weekday;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Widget;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.User;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.UserService;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserNotRegisteredException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.WrongRoleException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Role;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;

@SpringBootTest
@Transactional
public class DashboardServiceTest {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private DashboardRepo dashboardRepo;

    @Autowired @Lazy
    private UserService userService;

    @Autowired
    private RepoCleaner repoCleaner;

    private String firstname = "Jöndhard";
    private String lastname = "Biffel";
    private String email = "joendhard.biffel@hs-rm.de";
    private String password = "Sich3re$PasSw0rD17";

    private final String weekdaysString[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};

    @BeforeEach
    @AfterEach
    public void cleanUp(){
        repoCleaner.clean();
    }

    @Test
    @DisplayName("Get start dashboard successfully")
    public void testGetStartDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Dashboard start = dashboardService.getStartDashboard(u.getEmail());
        assertNotNull(start);
        assertEquals("Hessenschau Start-Dashboard", start.getName());
        assertTrue(start.isSynchroActive());
        assertEquals("#D8E9F6", start.getBgcolor());
        assertEquals(3, start.getWidgets().size());
        assertTrue(start.isDefaultDashboard());
        assertFalse(start.isStartDashboard());
    }

    @Test
    @DisplayName("Edit start dashboard successfully")
    public void testEditStartDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.EDITOR);
        u = userService.registerUser(u);
        Set<Widget> widgets = new HashSet<Widget>();
        for (Widget w : u.getDashboards().iterator().next().getWidgets()) {
            widgets.add(new Widget(w.getType(), w.getPos(), w.getSettings()));
        }
        widgets.add(new Widget("corona", 3, new String[]{"Frankfurt am Main"}));
        Dashboard currentSartDashboard = dashboardService.getStartDashboard(u.getEmail());
        List<Weekday> weekdays = new ArrayList<Weekday>();
        for (int i = 0; i < 7; i++) {
            weekdays.add(new Weekday(weekdaysString[i], new ArrayList<Timeslot>(), i));
        }
        Dashboard d = dashboardService.editStartDashboard(u.getEmail(), new Dashboard(currentSartDashboard.getName(), false, "#00ff44", widgets, weekdays, currentSartDashboard.isDefaultDashboard(), currentSartDashboard.isStartDashboard()));
        assertNotNull(d);
        assertEquals("Hessenschau Start-Dashboard", d.getName());
        assertFalse(d.isSynchroActive());
        assertEquals("#00ff44", d.getBgcolor());
        assertEquals(4, d.getWidgets().size());
        assertTrue(d.isDefaultDashboard());
        assertTrue(d.isStartDashboard());
    }

    @Test
    @DisplayName("Try to edit start dashboard with wrong role")
    public void testEditStartDashboard_WrongRole() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Set<Widget> widgets = new HashSet<Widget>();
        for (Widget w : u.getDashboards().iterator().next().getWidgets()) {
            widgets.add(new Widget(w.getType(), w.getPos(), w.getSettings()));
        }
        widgets.add(new Widget("corona", 3, new String[]{"Frankfurt am Main"}));
        Dashboard currentSartDashboard = dashboardService.getStartDashboard(u.getEmail());
        List<Weekday> weekdays = new ArrayList<Weekday>();
        for (int i = 0; i < 7; i++) {
            weekdays.add(new Weekday(weekdaysString[i], new ArrayList<Timeslot>(), i));
        }
        Dashboard d = new Dashboard(currentSartDashboard.getName(), false, "#00ff44", widgets, weekdays, currentSartDashboard.isDefaultDashboard(), currentSartDashboard.isStartDashboard());
        assertThrows(WrongRoleException.class, () -> dashboardService.editStartDashboard(email, d));
    }

    @Test
    @DisplayName("Try to edit start dashboard with not registered user")
    public void testEditStartDashboard_UserNotRegistered() throws Exception {
       assertThrows(UserNotRegisteredException.class, () -> dashboardService.editStartDashboard("test@123.de", new Dashboard()));
    }

    @Test
    @DisplayName("Get dashboard from user with id successfully")
    public void testGetDashboardFromUserWithId() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        long id = u.getDashboards().iterator().next().getId();
        Dashboard d = dashboardService.getDashboardFromUserWithId(email, id);
        assertNotNull(d);
        assertEquals("Hessenschau Start-Dashboard", d.getName());
        assertTrue(d.isSynchroActive());
        assertEquals("#D8E9F6", d.getBgcolor());
        assertEquals(3, d.getWidgets().size());
        assertTrue(d.isDefaultDashboard());
        assertFalse(d.isStartDashboard());
        assertEquals(7, d.getWeekdays().size());
        for (Weekday w : d.getWeekdays()) {
            assertTrue(w.getTimeslots().size() >= 0 && w.getTimeslots().size() < 4);
            assertEquals(w.getTimeslots(), w.getTimeslots());
        }
    }

    @Test
    @DisplayName("Try to get dashboard with id from not registered user")
    public void testGetDashboardFromUserWithId_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> dashboardService.getDashboardFromUserWithId(email, 0));
    }

    @Test
    @DisplayName("Try to get not existing Dashboard from user with id")
    public void testGetDashboardFromUserWithId_DashboardDoesntExist() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        assertThrows(DashboardDoesntExistException.class, () -> dashboardService.getDashboardFromUserWithId(email, 17));
    }

    @Test
    @DisplayName("Try to get dashboard with id from other user")
    public void testGetDashboardFromUserWithId_DashboardDoesNotBelongToTheUser() throws Exception {
        User u1 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u1);
        User u2 = new User("joghurta.biffel@hs-rm.de", "Joghurta", "Biffel", new HashSet<Dashboard>(), "p4(/dhjHJjb(897", Role.USER);
        u2 = userService.registerUser(u2);
        long id = u2.getDashboards().iterator().next().getId();
        assertThrows(DashboardDoesNotBelongToTheUserException.class, () -> dashboardService.getDashboardFromUserWithId(email, id));
    }

    @Test
    @DisplayName("Get new empty dashboard successfully")
    public void testNewEmptyDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Dashboard d = dashboardService.newEmptyDashboard(email, "Testdashboard");
        assertNotNull(dashboardRepo.findById(d.getId()));
        assertNotNull(d);
        assertEquals("Testdashboard", d.getName());
        assertTrue(d.isSynchroActive());
        assertEquals("#D8E9F6", d.getBgcolor());
        assertEquals(0, d.getWidgets().size());
        assertFalse(d.isDefaultDashboard());
        assertFalse(d.isStartDashboard());
    }

    @Test
    @DisplayName("Try to get new empty dashboard from not registered user")
    public void testNewEmptyDashboard_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> dashboardService.newEmptyDashboard(email, "Testdashboard"));
    }

    @Test
    @DisplayName("Try to get new empty dashboard with already existing name")
    public void testNewEmptyDashboard_DashboardWithSameNameAlreadyExists() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        assertThrows(DashboardWithSameNameAlreadyExistsException.class, () -> dashboardService.newEmptyDashboard(email, "Hessenschau Start-Dashboard"));
    }

    @Test
    @DisplayName("Add start dashboard to user successfully")
    public void testAddStartDashboardToUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Dashboard d = u.getDashboards().iterator().next();
        assertNotNull(d);
        assertEquals("Hessenschau Start-Dashboard", d.getName());
        assertTrue(d.isSynchroActive());
        assertEquals("#D8E9F6", d.getBgcolor());
        assertEquals(3, d.getWidgets().size());
        assertTrue(d.isDefaultDashboard());
        assertFalse(d.isStartDashboard());
        assertEquals(7, d.getWeekdays().size());
        for (Weekday w : d.getWeekdays()) {
            assertNotNull(w.getName());
            assertNotNull(w.getWeekdayIndex());
            assertEquals(0, w.getTimeslots().size());
        }
    }
    
    @Test
    @DisplayName("Try to add start dashboard with not registered user")
    public void testAddStartDashboardToUser_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> dashboardService.addStartDashboardToUser(email));
    }

    @Test
    @DisplayName("Try to add start dashboard but user is not new registered")
    public void testAddStartDashboardToUser_UserAlreadyEditetDashboards() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        assertThrows(UserAlreadyEditetDashboardsException.class, () -> dashboardService.addStartDashboardToUser(email));
    }

    @Test
    @DisplayName("Edit dashboard successfully")
    public void testEditDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Dashboard edit = dashboardService.getCurrentDashboardFromUser(u.getEmail());
        List<Weekday> weekdays = new ArrayList<Weekday>();
        for (int i = 0; i < 7; i++) {
            if (i == 2) {
                weekdays.add(new Weekday(weekdaysString[i], u.getTimeslots(), i));
            } else {
                weekdays.add(new Weekday(weekdaysString[i], new ArrayList<Timeslot>(), i));
            }
        }
        edit.setBgcolor("#ff8800");
        edit.setSynchroActive(false);
        edit.setDefaultDashboard(false);
        edit.setWeekdays(weekdays);
        Dashboard d = dashboardService.editDashboard(email, edit);
        assertNotNull(d);
        assertNotNull(dashboardRepo.findById(d.getId()));
        assertEquals("Hessenschau Start-Dashboard", d.getName());
        assertFalse(d.isSynchroActive());
        assertEquals(d.getWeekdays(), weekdays);
        assertEquals(7, d.getWeekdays().size());
        for (Weekday w : d.getWeekdays()) {
            if (w.getWeekdayIndex() == 2) {
                assertEquals(3, w.getTimeslots().size());
                for (Timeslot t : w.getTimeslots()) {
                    assertTrue(u.getTimeslots().contains(t));
                }
            } else {
                assertEquals(0, w.getTimeslots().size());
            }
            assertTrue(w.getTimeslots().size() >= 0 && w.getTimeslots().size() < 4);
            assertEquals(w.getTimeslots(), weekdays.get(w.getWeekdayIndex()).getTimeslots());
        }
        assertEquals("#ff8800", d.getBgcolor());
        assertEquals(3, d.getWidgets().size());
        assertFalse(d.isDefaultDashboard());
        assertFalse(d.isStartDashboard());
    }

    @Test
    @DisplayName("Try to edit dashboard with not registered user")
    public void testEditDashboard_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> dashboardService.editDashboard(email, new Dashboard()));
    }

    @Test
    @DisplayName("Try to edit not existing dashboard")
    public void testEditDashboard_DashboardDoesntExist() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        assertThrows(DashboardDoesntExistException.class, () -> dashboardService.editDashboard(email, new Dashboard()));
    }

    @Test
    @DisplayName("Try to edit dashboard from other user")
    public void testEditDashboard_DashboardDoesNotBelongToTheUser() throws Exception {
        User u1 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u1);
        User u2 = new User("joghurta.biffel@hs-rm.de", "Joghurta", "Biffel", new HashSet<Dashboard>(), "p4(/dhjHJjb(897", Role.USER);
        userService.registerUser(u2);
        assertThrows(DashboardDoesNotBelongToTheUserException.class, () -> dashboardService.editDashboard(email, userService.findUserByMail("joghurta.biffel@hs-rm.de").getDashboards().iterator().next()));
    }

    @Test
    @DisplayName("Import dashboard successfully")
    public void testImportDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        dashboardService.newEmptyDashboard(email, "Testdashboard");
        Set<Widget> widgets = new HashSet<Widget>();
        widgets.add(new Widget("weather", 0, new String[]{"Wiesbaden"}));
        widgets.add(new Widget("corona", 1, new String[]{"Wiesbaden"}));
        List<Weekday> weekdays = new ArrayList<Weekday>();
        for (int i = 0; i < 7; i++) {
            if (i == 2) {
                weekdays.add(new Weekday(weekdaysString[i], u.getTimeslots(), i));
            } else {
                weekdays.add(new Weekday(weekdaysString[i], new ArrayList<Timeslot>(), i));
            }
        }
        Dashboard edit = new Dashboard("Testdashboard", true, "#f27e0a", widgets, weekdays, false, false);
        Dashboard d = dashboardService.importDashboard(email, edit);
        assertNotNull(d);
        assertNotNull(dashboardRepo.findById(d.getId()));
        assertEquals("Testdashboard", d.getName());
        assertTrue(d.isSynchroActive());
        assertEquals("#f27e0a", d.getBgcolor());
        assertEquals(2, d.getWidgets().size());
        assertFalse(d.isDefaultDashboard());
        assertFalse(d.isStartDashboard());
        assertEquals(7, d.getWeekdays().size());
        for (Weekday w : d.getWeekdays()) {
            assertNotNull(w.getName());
            assertNotNull(w.getWeekdayIndex());
            assertEquals(0, w.getTimeslots().size());
        }
        Dashboard dashboardDB = dashboardService.findDashboardById(d.getId());
        assertEquals(7, dashboardDB.getWeekdays().size());
        for (Weekday w : dashboardDB.getWeekdays()) {
            assertNotNull(w.getName());
            assertNotNull(w.getWeekdayIndex());
            assertEquals(0, w.getTimeslots().size());
        }
    }

    @Test
    @DisplayName("Try to import dashboard with not registered user")
    public void testImportDashboard_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> dashboardService.importDashboard(email, new Dashboard()));
    }

    @Test
    @DisplayName("Delete dashboard successfully")
    public void testDeleteDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Dashboard d = dashboardService.newEmptyDashboard(email, "Testdashboard");
        assertNotNull(dashboardRepo.findById(d.getId()));
        dashboardService.deleteDashboard(d.getId(), email);
        assertFalse(dashboardRepo.findById(d.getId()).isPresent());
    }

    @Test
    @DisplayName("Try to delete dashboard with not registered user")
    public void testDeleteDashboard_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> dashboardService.deleteDashboard(0, email));
    }

    @Test
    @DisplayName("Try to delete not existing dashboard")
    public void testDeleteDashboard_DashboardDoesntExis() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        dashboardService.newEmptyDashboard(email, "Testdashboard");
        assertThrows(DashboardDoesntExistException.class, () -> dashboardService.deleteDashboard(17, email));
    }

    @Test
    @DisplayName("Try to delete the last dashboard from a user")
    public void testDeleteDashboard_UserMustHaveOneDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        long id = u.getDashboards().iterator().next().getId();
        assertThrows(UserMustHaveOneDashboardException.class, () -> dashboardService.deleteDashboard(id, email));
    }

    @Test
    @DisplayName("Try to delete dashboard from other user")
    public void testDeleteDashboard_DashboardDoesNotBelongToTheUser() throws Exception {
        User u1 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u1 = userService.registerUser(u1);
        dashboardService.newEmptyDashboard(email, "Testdashboard");
        User u2 = new User("joghurta.biffel@hs-rm.de", "Joghurta", "Biffel", new HashSet<Dashboard>(), "p4(/dhjHJjb(897", Role.USER);
        u2 = userService.registerUser(u2);
        long id = u2.getDashboards().iterator().next().getId();
        assertThrows(DashboardDoesNotBelongToTheUserException.class, () -> dashboardService.deleteDashboard(id, email));
    }

    @Test
    @DisplayName("Get all dashboards from user successfully")
    public void testGetAllDashboardsFromUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        dashboardService.newEmptyDashboard(email, "Testdashboard");
        List<DashboardInfo> dashboards = dashboardService.getAllDashboardsFromUser(email);
        assertNotNull(dashboards);
        assertEquals(2, dashboards.size());
    }

    @Test
    @DisplayName("Try to get all dashboards from not registered user")
    public void testGetAllDashboardsFromUser_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> dashboardService.getAllDashboardsFromUser(email));
    }

    @Test
    @DisplayName("Get current dashboard from user successfully")
    public void testGetCurrentDashboardFromUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Dashboard d = dashboardService.newEmptyDashboard(email, "Testdashboard");
        Set<Widget> widgets = new HashSet<Widget>();
        widgets.add(new Widget("weather", 0, new String[]{"Wiesbaden"}));
        widgets.add(new Widget("corona", 1, new String[]{"Wiesbaden"}));
        d.setWidgets(widgets);
        d.setDefaultDashboard(false);
        List<Weekday> weekdays = new ArrayList<Weekday>();
        for (int i = 0; i < 7; i++) {
            List<Timeslot> timeslots = new ArrayList<Timeslot>();
            for (Timeslot t : u.getTimeslots()) {
                timeslots.add(new Timeslot(t.getStartTime(), t.getEndTime()));
            }
            weekdays.add(new Weekday(weekdaysString[i], timeslots, i));
        }
        d.setWeekdays(weekdays);
        dashboardService.editDashboard(email, d);
        Dashboard ret = dashboardService.getCurrentDashboardFromUser(email);
        assertNotNull(ret);
        LocalTime tc = LocalTime.now();
        if (tc.isAfter(u.getTimeslots().get(0).getStartTime()) && tc.isBefore(u.getTimeslots().get(2).getEndTime())) {
            assertEquals("Testdashboard", ret.getName());
        } else {
            assertEquals("Hessenschau Start-Dashboard", ret.getName());
        }
    }

    @Test
    @DisplayName("Get current dashboard from user successfully - no timeslot so default dashboard")
    public void testGetCurrentDashboardFromUser_Default() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Dashboard d = dashboardService.getCurrentDashboardFromUser(email);
        assertNotNull(d);
        assertEquals("Hessenschau Start-Dashboard", d.getName());
        assertTrue(d.isSynchroActive());
        assertEquals("#D8E9F6", d.getBgcolor());
        assertEquals(3, d.getWidgets().size());
        assertTrue(d.isDefaultDashboard());
        assertFalse(d.isStartDashboard());
    }

    @Test
    @DisplayName("Get current dashboard from user successfully - latest default dashboard")
    public void testGetCurrentDashboardFromUser_LatestDefault() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Dashboard d = dashboardService.newEmptyDashboard(email, "Testdashboard");
        Set<Widget> widgets = new HashSet<Widget>();
        widgets.add(new Widget("weather", 0, new String[]{"Wiesbaden"}));
        widgets.add(new Widget("corona", 1, new String[]{"Wiesbaden"}));
        d.setWidgets(widgets);
        d.setDefaultDashboard(true);
        dashboardService.editDashboard(email, d);
        Dashboard ret = dashboardService.getCurrentDashboardFromUser(email);
        assertNotNull(ret);
        assertEquals("Testdashboard", ret.getName());
    }

    @Test
    @DisplayName("Try to get current dashboards from not registered user")
    public void testGetCurrentDashboardFromUser_UserNotRegistered() throws Exception {
        assertThrows(UserNotRegisteredException.class, () -> dashboardService.getCurrentDashboardFromUser(email));
    }

}
