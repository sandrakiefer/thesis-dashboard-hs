package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard;

import java.util.List;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesNotBelongToTheUserException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesntExistException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardWithSameNameAlreadyExistsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.UserAlreadyEditetDashboardsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.UserMustHaveOneDashboardException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.DashboardInfo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserNotRegisteredException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.WrongRoleException;

/**
 * A service to manage the different operations on dashboards in the database.
 */
public interface DashboardService {

    /**
     * Searches for the dashboard with the given id in the database.
     *
     * @param id id of the dashboard
     * @return dashboard from the database
     * @throws DashboardDoesntExistException the dashboard with the given id does not exist in the database (bad request)
     */
    Dashboard findDashboardById(long id) throws DashboardDoesntExistException;
    
    /**
     * Get the current start dashboard from the database.
     * 
     * @param email email of the user
     * @return start dashboard from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     */
    Dashboard getStartDashboard(String email) throws UserNotRegisteredException;

    /**
     * Edit the current start dashboard and save it in the database.
     * 
     * @param email email of the editor
     * @param dashboard dashboard object that contains all the necessary information for changing the start dashboard
     * @return the successfully saved new default dashboard in the database
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     * @throws WrongRoleException only editors are allowed to change the start dashboard (forbidden)
     */
    Dashboard editStartDashboard(String email, Dashboard dashboard) throws UserNotRegisteredException, WrongRoleException;

    /**
     * Get the dashboard of a user using the id of the dashboard.
     * 
     * @param userEmail email of the user
     * @param dashboardId id of the dashboard
     * @return stored and managed dashboard object from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     * @throws DashboardDoesntExistException the dashboard with the given id does not exist in the database (bad request)
     * @throws DashboardDoesNotBelongToTheUserException the dashboard does not belong to the user (bad request)
     */
    Dashboard getDashboardFromUserWithId(String userEmail, long dashboardId) throws UserNotRegisteredException, DashboardDoesntExistException, DashboardDoesNotBelongToTheUserException;

    /**
     * Creates a new empty dashboard for the given user and adds it to the user in the database.
     * 
     * @param userEmail email of the user
     * @param dashboardName name of the new dashboard
     * @return the successfully stored and managed new dashboard object from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     * @throws DashboardWithSameNameAlreadyExistsException dashboard with the same name already exists in the database for this user (bad request)
     */
    Dashboard newEmptyDashboard(String userEmail, String dashboardName) throws UserNotRegisteredException, DashboardWithSameNameAlreadyExistsException;

    /**
     * Adds the start dashboard from the database to the given user in the database.
     * 
     * @param userEmail email of the user
     * @return stored and managed start dashboard object from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     * @throws UserAlreadyEditetDashboardsException start dashboard has already been added to the user (bad request)
     */
    Dashboard addStartDashboardToUser(String userEmail) throws UserNotRegisteredException, UserAlreadyEditetDashboardsException;

    /**
     * Edits the current dashboard of the given user and updates the object in the database.
     * 
     * @param userEmail email of the user
     * @param dashboard dashboard object that contains all the necessary information for editing a dashboard
     * @return the successfully edited dashboard object from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     * @throws DashboardDoesntExistException the given dashboard does not exist in the database (bad request)
     * @throws DashboardDoesNotBelongToTheUserException the dashboard does not belong to the user (bad request)
     */
    Dashboard editDashboard(String userEmail, Dashboard dashboard) throws UserNotRegisteredException, DashboardDoesntExistException, DashboardDoesNotBelongToTheUserException;

    /**
     * Import an already created dashboard of another user and add it to the given user.
     * 
     * @param userEmail email of the user
     * @param dashboard dashboard object that contains all the necessary information for importing a dashboard
     * @return the successfully imported dashboard object from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     */
    Dashboard importDashboard(String userEmail, Dashboard dashboard) throws UserNotRegisteredException;

    /**
     * Deletes the dashboard of the given user.
     * 
     * @param dashboardId id of the dashboard to be deleted
     * @param userEmail email of the user
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     * @throws DashboardDoesntExistException the dashboard with the given id does not exist in the database (bad request)
     * @throws UserMustHaveOneDashboardException the user is not allowed to delete the last dashboard (bad request)
     * @throws DashboardDoesNotBelongToTheUserException the dashboard does not belong to the user (bad request)
     */
    void deleteDashboard(long dashboardId, String userEmail) throws UserNotRegisteredException, DashboardDoesntExistException, UserMustHaveOneDashboardException, DashboardDoesNotBelongToTheUserException;

    /**
     * Lists all dashboards of the given user.
     * 
     * @param userEmail email of the user
     * @return list of all saved dashboards of the user from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     */
    List<DashboardInfo> getAllDashboardsFromUser(String userEmail) throws UserNotRegisteredException;

    /**
     * Dashboard of a user to be currently displayed.
     * 
     * @param userEmail email of the user
     * @return the dashboard to be displayed at the current time from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     */
    Dashboard getCurrentDashboardFromUser(String userEmail) throws UserNotRegisteredException;

}
