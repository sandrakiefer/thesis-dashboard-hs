
package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user;

import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesNotBelongToTheUserException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesntExistException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Weekday;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserAlreadyExistsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserNotRegisteredException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;

/**
 * A service to manage the different operations on users in the database.
 */
public interface UserService {

    /**
     * Searches for the user with the given email in the database.
     *
     * @param email email of the user
     * @return user from the database
     * @throws UserNotRegisteredException no user can be found in the database with this email (bad request)
     */
    User findUserByMail(String email) throws UserNotRegisteredException;

    /**
     * Stores a new user in the database.
     * 
     * @param newUser new user registering
     * @return stored and managed user object from the database
     * @throws UserAlreadyExistsException this user already has an account in the database (conflict)
     */
    User registerUser(User newUser) throws UserAlreadyExistsException;

    /**
     * Update a user in the database when one dashboard is editet.
     * 
     * @param user editet user object to be saved
     * @return stored and managed user object from the database
     */
    User saveUserWithEditedDashboard(User user);

    /**
     * Get the timeslots of the given user.
     * 
     * @param email email of the user
     * @return list of timeslots from the user
     * @throws UserNotRegisteredException no user can be found in the database with this email (bad request)
     */
    List<Timeslot> getTimeslotsOfUser(String email) throws UserNotRegisteredException;

    /**
     * Change the timeslots from a user. 
     * 
     * @param email email of the user
     * @param timeslots new timeslots for morning, midday and evening
     * @return stored and managed timeslot list of the user from the database
     * @throws UserNotRegisteredException no user can be found in the database with this email (bad request)
     */
    List<Timeslot> changeTimeslots(String email, List<Timeslot> timeslots) throws UserNotRegisteredException;

    /**
     * Get the occupied timeslots of a user for all weekdays.
     * 
     * @param email email of the user
     * @param dashboardId id of the dashboard, which should not be touched for calculating the occupied time
     * @return list of all days of the week and their occupied timeslots
     * @throws UserNotRegisteredException no user can be found in the database with this email (bad request)
     * @throws DashboardDoesntExistException the dashboard with the given id does not exist in the database (bad request)
     * @throws DashboardDoesNotBelongToTheUserException the dashboard does not belong to the user (bad request)
     */
    List<Weekday> getOccupiedTimeslotsOfAllWeekdays(String email, long dashboardId) throws UserNotRegisteredException, DashboardDoesntExistException, DashboardDoesNotBelongToTheUserException;

}
