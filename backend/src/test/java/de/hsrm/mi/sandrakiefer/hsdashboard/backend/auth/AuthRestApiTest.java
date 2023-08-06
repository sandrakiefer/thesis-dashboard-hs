package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth;

import java.util.HashSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.RepoCleaner;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.Dashboard;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.security.jwt.JwtTokenProvider;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.User;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.UserRepo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.UserService;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Role;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthRestApiTest {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

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
    @DisplayName("POST /api/auth/signup successful")
    public void testSignUpUser() throws Exception {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("email", email);
        json.put("emailRepetition", email);
        json.put("firstName", firstname);
        json.put("lastName", lastname);
        json.put("password", password);
        json.put("passwordRepetition", password);
        mockmvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().is2xxSuccessful());
        assertNotNull(userRepo.findById(email));
    }

    @Test
    @DisplayName("POST /api/auth/signup invalid input - bad request")
    public void testSignUpUser_InvalidInput() throws Exception {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("email", email);
        json.put("emailRepetition", email);
        json.put("firstName", firstname);
        json.put("lastName", lastname);
        json.put("password", "test123");
        json.put("passwordRepetition", "test123");
        mockmvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup emails do not match - bad request")
    public void testSignUpUser_EmailsDoNotMatch() throws Exception {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("email", email);
        json.put("emailRepetition", "test@123.de");
        json.put("firstName", firstname);
        json.put("lastName", lastname);
        json.put("password", password);
        json.put("passwordRepetition", password);
        mockmvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/signup passwords do not match - bad request")
    public void testSignUpUser_PasswordsDoNotMatch() throws Exception {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("email", email);
        json.put("emailRepetition", email);
        json.put("firstName", firstname);
        json.put("lastName", lastname);
        json.put("password", password);
        json.put("passwordRepetition", "test123");
        mockmvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("POST /api/auth/signup user already exists - conflict")
    public void testSignUpUser_UserAlreadyExists() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("email", email);
        json.put("emailRepetition", email);
        json.put("firstName", firstname);
        json.put("lastName", lastname);
        json.put("password", password);
        json.put("passwordRepetition", password);
        mockmvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().isConflict());
    }
    
    @Test 
    @DisplayName("POST /api/auth/signin successful")
    public void testSignIn() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        userService.registerUser(u);
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("email", email);
        json.put("password", password);
        ResultActions actions = mockmvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().is2xxSuccessful());
        String response = actions.andReturn().getResponse().getContentAsString();
        assertTrue(jwtTokenProvider.validateToken(response.substring(10, response.length() - 2)));
    }

    @Test
    @DisplayName("POST /api/auth/signin invalid input - bad request")
    public void testSignInUser_InvalidInput() throws Exception {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("email", email);
        json.put("password", "test123");
        mockmvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test 
    @DisplayName("POST /api/auth/signin user not registered - unauthorized")
    public void testSignIn_UserNotRegistered() throws Exception {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("email", email);
        json.put("password", password);
        mockmvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
            .andExpect(status().isUnauthorized());
    }

}
