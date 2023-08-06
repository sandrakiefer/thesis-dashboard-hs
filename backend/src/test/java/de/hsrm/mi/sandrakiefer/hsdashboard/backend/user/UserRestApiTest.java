package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user;

import java.util.HashSet;

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

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.RepoCleaner;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.Dashboard;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Role;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRestApiTest {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RepoCleaner repoCleaner;

    private final String firstname = "Jöndhard";
    private final String lastname = "Biffel";
    private final String email = "joendhard.biffel@hs-rm.de";
    private final String password = "Sich3re$PasSw0rD17";

    @BeforeEach
    @AfterEach
    public void cleanUp(){
        repoCleaner.clean();
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/timeslots successful")
    public void testGetTimeslotsOfUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.get("/api/user/timeslots"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/timeslots user not registered - bad request")
    public void testGetTimeslotsOfUser_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/user/timeslots")).andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/user/timeslots successful")
    public void testChangeTimeslots() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        ObjectNode json1 = JsonNodeFactory.instance.objectNode();
        json1.put("startTime", "00:00:00");
        json1.put("endTime", "11:59:59");
        ObjectNode json2 = JsonNodeFactory.instance.objectNode();
        json2.put("startTime", "12:00:00");
        json2.put("endTime", "16:59:59");
        ObjectNode json3 = JsonNodeFactory.instance.objectNode();
        json3.put("startTime", "17:00:00");
        json3.put("endTime", "23:59:59");
        String s = String.format("[%s,%s,%s]", json1.toString(), json2.toString(), json3.toString());
        mockmvc.perform(MockMvcRequestBuilders.put("/api/user/timeslots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(s))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("PUT /api/user/timeslots user not registered - bad request")
    public void testChangeTimeslots_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.put("/api/user/timeslots")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/displaytime successful")
    public void testGetOccupiedDisplayTime() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.get(String.format("/api/user/displaytime/%d", u.getDashboards().iterator().next().getId()))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/displaytime user not registered - bad request")
    public void testGetOccupiedDisplayTime_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/user/displaytime/17")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/displaytime dashboard doesnt exist - bad request")
    public void testGetOccupiedDisplayTime_DashboardDoesntExists() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.get("/api/user/displaytime/17")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/displaytime dashboard doesnt belong to the user - bad request")
    public void testGetOccupiedDisplayTime_DashboardDoesNotBelongToTheUser() throws Exception {
        User u1 = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u1);
        User u2 = new User("joghurta.biffel@hs-rm.de", "Joghurta", "Biffel", new HashSet<Dashboard>(), "p4(/dhjHJjb(897", Role.USER);
        u2 = userService.registerUser(u2);
        mockmvc.perform(MockMvcRequestBuilders.get(String.format("/api/user/displaytime/%d", u2.getDashboards().iterator().next().getId()))).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/name successful")
    public void testGetNameOfUser() throws Exception {
        User u = new User(email, "Joendhard", lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.get("/api/user/name")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().string("Joendhard Biffel"));
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/name user not registered - bad request")
    public void testGetNameOfUser_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/user/name")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/role successful")
    public void testGetRoleOfUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        mockmvc.perform(MockMvcRequestBuilders.get("/api/user/role")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().string(Role.USER.toString()));
    }

    @Test
    @WithMockUser(username = email)
    @DisplayName("GET /api/user/role user not registered - bad request")
    public void testGetRoleOfUser_UserNotRegistered() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders.get("/api/user/role")).andExpect(status().isBadRequest());
    }

}
