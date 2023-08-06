package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.RepoCleaner;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Weekday;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Widget;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.User;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.UserService;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Role;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardRestApiTest {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private UserService userService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private RepoCleaner repoCleaner;

    private final String firstname = "Jöndhard";
    private final String lastname = "Biffel";
    private final String email = "joendhard.biffel@hs-rm.de";
    private final String password = "Sich3re$PasSw0rD17";

    private final String weekdaysString[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};

    private final ObjectWriter ow = new ObjectMapper().registerModule(new ParameterNamesModule()).registerModule(new Jdk8Module()).registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();

    @BeforeEach
    @AfterEach
    public void cleanUp(){
        repoCleaner.clean();
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/dashboard/start successful")
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
        mockmvc.perform(MockMvcRequestBuilders.put("/api/dashboard/start")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ow.writeValueAsString(new Dashboard(currentSartDashboard.getName(), false, "#00ff44", widgets, weekdays, currentSartDashboard.isDefaultDashboard(), currentSartDashboard.isStartDashboard()))))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/dashboard/start user not registered - bad request")
    public void testEditStartDashboard_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.put("/api/dashboard/start")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ow.writeValueAsString(new Dashboard())))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/dashboard/start wrong role - forbidden")
    public void testEditStartDashboard_WrongRole() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.put("/api/dashboard/start")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ow.writeValueAsString(new Dashboard())))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/dashboard/{id} successful")
    public void testGetDashboardFromId() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.get(String.format("/api/dashboard/%d", u.getDashboards().iterator().next().getId()))).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/dashboard/{id} user not registered - bad request")
    public void testGetDashboardFromId_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/dashboard/17")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/dashboard/{id} dashboard doesnt exist - bad request")
    public void testGetDashboardFromId_DashboardDoesntExist() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.get("/api/dashboard/17")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/dashboard/{id} dashboard does not belong to the user - bad request")
    public void testGetDashboardFromId_DashboardDoesNotBelongToTheUser() throws Exception {
        User u1 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u1);
        User u2 = new User("joghurta.biffel@hs-rm.de", "Joghurta", "Biffel", new HashSet<Dashboard>(), "p4(/dhjHJjb(897", Role.USER);
        u2 = userService.registerUser(u2);
        long id = u2.getDashboards().iterator().next().getId();
        mockmvc.perform(MockMvcRequestBuilders.get(String.format("/api/dashboard/%d", id))).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("POST /api/dashboard/new successful")
    public void testNewEmptyDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.post("/api/dashboard/new?name=Testdashboard")).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("POST /api/dashboard/new user not registered - bad request")
    public void testNewEmptyDashboard_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.post("/api/dashboard/new?name=test")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("POST /api/dashboard/new dashboard with same name already exists - bad request")
    public void testNewEmptyDashboard_DashboardWithSameNameAlreadyExists() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.post("/api/dashboard/new?name=Hessenschau Start-Dashboard")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/dashboard/edit successful")
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
        mockmvc.perform(MockMvcRequestBuilders.put("/api/dashboard/edit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ow.writeValueAsString(edit)))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/dashboard/edit user not registered - bad request")
    public void testEditDashboard_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.put("/api/dashboard/edit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/dashboard/edit dashboard doesnt exist - bad request")
    public void testEditDashboard_DashboardDoesntExist() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.put("/api/dashboard/edit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/dashboard/edit dashboard does not belong to the user - bad request")
    public void testEditDashboard_DashboardDoesNotBelongToTheUser() throws Exception {
        User u1 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u1);
        User u2 = new User("joghurta.biffel@hs-rm.de", "Joghurta", "Biffel", new HashSet<Dashboard>(), "p4(/dhjHJjb(897", Role.USER);
        u2 = userService.registerUser(u2);
        mockmvc.perform(MockMvcRequestBuilders.put("/api/dashboard/edit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ow.writeValueAsString(u2.getDashboards().iterator().next())))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/dashboard/edit invalid input - bad request")
    public void testEditDashboard_InvalidInput() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("hallo", "juhu");
        mockmvc.perform(MockMvcRequestBuilders.put("/api/dashboard/edit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("POST /api/dashboard/import successful")
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
        mockmvc.perform(MockMvcRequestBuilders.post("/api/dashboard/import")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ow.writeValueAsString(edit)))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("POST /api/dashboard/import user not registered - bad request")
    public void testImportDashboard_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.post("/api/dashboard/import")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ow.writeValueAsString(new Dashboard())))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("POST /api/dashboard/import invalid input - bad request")
    public void testImportDashboard_InvalidInput() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.post("/api/dashboard/import")
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("DELETE /api/dashboard/delete/{id} successful")
    public void testDeleteDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        Dashboard d = dashboardService.newEmptyDashboard(email, "Testdashboard");
        mockmvc.perform(MockMvcRequestBuilders.delete(String.format("/api/dashboard/delete/%d", d.getId()))).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("DELETE /api/dashboard/delete/{id} user not registered - bad request")
    public void testDeleteDashboar_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.delete("/api/dashboard/delete/17")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("DELETE /api/dashboard/delete/{id} dashboard doesnt exist - bad request")
    public void testDeleteDashboar_DashboardDoesntExist() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.delete("/api/dashboard/delete/17")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("DELETE /api/dashboard/delete/{id} user must have on dashboard - bad request")
    public void testDeleteDashboar_UserMustHaveOneDashboard() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.delete(String.format("/api/dashboard/delete/%d", u.getDashboards().iterator().next().getId()))).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("DELETE /api/dashboard/delete/{id} dashboard does not belong to the user - bad request")
    public void testDeleteDashboar_DashboardDoesNotBelongToTheUser() throws Exception {
        User u1 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u1);
        User u2 = new User("joghurta.biffel@hs-rm.de", "Joghurta", "Biffel", new HashSet<Dashboard>(), "p4(/dhjHJjb(897", Role.USER);
        u2 = userService.registerUser(u2);
        long id = u2.getDashboards().iterator().next().getId();
        mockmvc.perform(MockMvcRequestBuilders.delete(String.format("/api/dashboard/delete/%d", id))).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/dashboard/all successful")
    public void testGetAllDashboardsFromUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        dashboardService.newEmptyDashboard(email, "Testdashboard");
        mockmvc.perform(MockMvcRequestBuilders.get("/api/dashboard/all"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/dashboard/all user not registered - bad request")
    public void testGetAllDashboardsFromUser_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/dashboard/all")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/dashboard/current successful")
    public void testGetCurrentDashboardFromUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.get("/api/dashboard/current"))
            .andExpect(jsonPath("$.name").value("Hessenschau Start-Dashboard"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/dashboard/current user not registered - bad request")
    public void testGetCurrentDashboardFromUser_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/dashboard/current")).andExpect(status().isBadRequest());
    }
    
}
