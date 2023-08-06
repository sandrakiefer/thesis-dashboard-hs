package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

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

/**
 * {@inheritDoc}
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public User signUpUser(SignUpRequest signUpRequest) throws SignUpRequestInvalidException, UserAlreadyExistsException {
        if (!signUpRequest.getEmail().equals(signUpRequest.getEmailRepetition())) {
            String msg = "Email and repetition in the sign up request do not match";
            logger.error(msg);
            throw new SignUpRequestInvalidException(msg);   
        }
        if (!signUpRequest.getPassword().equals(signUpRequest.getPasswordRepetition())) {
            String msg = "Password and repetition in the sign up request do not match";
            logger.error(msg);
            throw new SignUpRequestInvalidException(msg);
        }
        User u = new User(signUpRequest.getEmail(), signUpRequest.getFirstName(), signUpRequest.getLastName(), new HashSet<Dashboard>(), signUpRequest.getPassword(), Role.USER);
        return userService.registerUser(u);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String signInUser(SignInRequest signInRequest) throws AuthenticationApiException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            return jwtTokenProvider.generateToken(authentication);
        } catch(AuthenticationException e) {
            String msg = "Jwt authentication failed";
            logger.error(msg);
            throw new AuthenticationApiException(msg);
        }
    }
    
}
