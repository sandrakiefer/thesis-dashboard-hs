package de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions.AuthenticationApiException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.exceptions.SignUpRequestInvalidException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.tools.SignInRequest;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.auth.tools.SignUpRequest;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.User;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserAlreadyExistsException;

/**
 * A service to manage the authentication requests sign in and sign up in the database.
 */
public interface AuthService {

    /**
     * Service to register a new user in the database.
     * 
     * @param signUpRequest request object that contains all the necessary information for the authentication sign up request
     * @return stored and managed user object from the database
     * @throws SignUpRequestInvalidException sign up request is invalid (bad request)
     * @throws UserAlreadyExistsException user is already registered in the database (conflict)
     */
    User signUpUser(SignUpRequest signUpRequest) throws SignUpRequestInvalidException, UserAlreadyExistsException;

    /**
     * Service for logging in a user, after checking whether this user has already been successfully registered.
     * 
     * @param signInRequest request object that contains all the necessary information for the authentication sign in request
     * @return newly generated jwt token as string belonging to the user of the request
     * @throws AuthenticationApiException generation of the jwt token failed for several reasons (unauthorized)
     */
    String signInUser(SignInRequest signInRequest) throws AuthenticationApiException;
    
}
