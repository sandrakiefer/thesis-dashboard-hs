package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.RepoCleaner;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions.AuthenticationApiException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions.SignUpRequestInvalidException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.tools.SignInRequest;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.tools.SignUpRequest;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.Dashboard;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.security.jwt.JwtTokenProvider;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.User;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.UserService;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserAlreadyExistsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Role;

@SpringBootTest
public class AuthServiceTest {
    
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
    @DisplayName("Sign up user successfully")
    public void testSignUpUser() throws Exception {
        User u = authService.signUpUser(new SignUpRequest(email, email, firstname, lastname, password, password));
        assertNotNull(u);
        assertNotNull(userService.findUserByMail(email));
        assertEquals(1, u.getDashboards().size());
        assertEquals("Hessenschau Start-Dashboard", u.getDashboards().iterator().next().getName());
        assertFalse(u.getDashboards().iterator().next().isStartDashboard());
    }

    @Test
    @DisplayName("Try to sign up user with wrong email repetition")
    public void testSignUpUser_EmailRepetitionWrong() throws Exception {
        assertThrows(SignUpRequestInvalidException.class, () -> authService.signUpUser(new SignUpRequest(email, "test@123.de", firstname, lastname, password, password)));
    }

    @Test
    @DisplayName("Try to sign up user with wrong password repetition")
    public void testSignUpUser_PasswordRepetitionWrong() throws Exception {
        assertThrows(SignUpRequestInvalidException.class, () -> authService.signUpUser(new SignUpRequest(email, email, firstname, lastname, password, "p4(/dhjHJjb(897")));
    }

    @Test
    @DisplayName("Try tp sign up an already registered user")
    public void testSignUpUser_UserAlreadyExists() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        assertThrows(UserAlreadyExistsException.class, () -> authService.signUpUser(new SignUpRequest(email, email, firstname, lastname, password, password)));
    }

    @Test
    @DisplayName("Sign in user successfully")
    public void testSignInUser() throws Exception {
        User u = new User(email, firstname, lastname, new HashSet<Dashboard>(), password, Role.USER);
        u = userService.registerUser(u);
        String jwt = authService.signInUser(new SignInRequest(email, password));
        assertTrue(jwtTokenProvider.validateToken(jwt));
    }

    @Test
    @DisplayName("Try to sign in not registered user")
    public void testSignInUser_AuthenticationApiException() throws Exception {
        assertThrows(AuthenticationApiException.class, () -> authService.signInUser(new SignInRequest(email, password)));
    }
    
}
