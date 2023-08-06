package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions.AuthenticationApiException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions.SignInRequestInvalidException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions.SignUpRequestInvalidException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.tools.SignInRequest;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.tools.SignUpRequest;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.security.jwt.JwtTokenResponse;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.User;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserAlreadyExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Rest Controller to manage the authentication request for sign up and sign in, mapped on the path "/api/auth".
 */
@RestController
@RequestMapping("/api/auth")
public class AuthRestApi {

    @Autowired
    private AuthService authService;

    private Logger logger = LoggerFactory.getLogger(AuthRestApi.class);

    /**
     * Endpoint for registering a new user in the database.
     * 
     * @param signUpRequest request object that contains all the necessary information for the authentication sign up request
     * @param result validation result of the sign up request object
     * @return stored and managed user object from the database
     * @throws SignUpRequestInvalidException sign up request is invalid (Bad Request 400)
     * @throws UserAlreadyExistsException user is already registered in the database (Bad Request 409)
     */
    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public User register(@RequestBody @Valid SignUpRequest signUpRequest, BindingResult result) throws SignUpRequestInvalidException, UserAlreadyExistsException {
        if (result.hasErrors()) {
            String msg = "Invalid input in sign up request";
            logger.error(msg);
            throw new SignUpRequestInvalidException(msg);
        }
        return authService.signUpUser(signUpRequest);
    }

    /**
     * Endpoint for logging in a user, after checking whether this user has already been successfully registered.
     * 
     * @param signInRequest request object that contains all the necessary information for the authentication sign in request
     * @param result validation result of the sign in request object
     * @return newly generated jwt token as string belonging to the user of the request
     * @throws SignInRequestInvalidException sign in request is invalid (Bad Request 400)
     * @throws AuthenticationApiException generation of the jwt token failed for several reasons (Unauthorized 401)
     */
    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtTokenResponse login(@RequestBody @Valid SignInRequest signInRequest, BindingResult result) throws SignInRequestInvalidException, AuthenticationApiException {
        if (result.hasErrors()) {
            String msg = "Invalid input in sign in request";
            logger.error(msg);
            throw new SignInRequestInvalidException(msg);
        }
        return new JwtTokenResponse(authService.signInUser(signInRequest));
    }

}
