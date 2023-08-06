package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardApiException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesNotBelongToTheUserException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesntExistException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardWithSameNameAlreadyExistsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.UserMustHaveOneDashboardException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.DashboardInfo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserNotRegisteredException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.WrongRoleException;

/**
 * Rest Controller to manage the different requests for dashboards, mapped on the path "/api/dashboard".
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestApi {

    @Autowired
    private DashboardService dashboardService;

    private Logger logger = LoggerFactory.getLogger(DashboardRestApi.class);

    /**
     * Endpoint to edit the current start dashboard and save it in the database.
     * 
     * @param dashboard dashboard object that contains all the necessary information for changing the start dashboard
     * @param principal logged in editor
     * @return the successfully saved new default dashboard in the database
     * @throws UserNotRegisteredException the user with the given email is not registered (Bad Request 400)
     * @throws WrongRoleException only editors are allowed to change the start dashboard (Forbidden 403)
     */
    @PutMapping(value = "/start",  produces = MediaType.APPLICATION_JSON_VALUE)
    public Dashboard editStartDashboard(@RequestBody Dashboard dashboard, Principal principal) throws UserNotRegisteredException, WrongRoleException {
        return dashboardService.editStartDashboard(principal.getName(), dashboard);
    }

    /**
     * Endpoint to get the dashboard of a user using the id of the dashboard.
     * 
     * @param id id of the dashboard
     * @param principal logged in user
     * @return stored and managed dashboard object from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (Bad Request 400)
     * @throws DashboardDoesntExistException the dashboard with the given id does not exist in the database (Bad Request 400)
     * @throws DashboardDoesNotBelongToTheUserException the dashboard does not belong to the user (Bad Request 400)
     */
    @GetMapping(value = "/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public Dashboard getDashboardFromId(@PathVariable long id, Principal principal) throws UserNotRegisteredException, DashboardDoesntExistException, DashboardDoesNotBelongToTheUserException {
        return dashboardService.getDashboardFromUserWithId(principal.getName(), id);
    }

    /**
     * Endpoint to create a new empty dashboard for the given user and add it to the user in the database.
     * 
     * @param name name of the new dashboard
     * @param principal logged in user
     * @return the successfully stored and managed new dashboard object from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (Bad Request 400)
     * @throws DashboardWithSameNameAlreadyExistsException dashboard with the same name already exists in the database for this user (Bad Request 400)
     */
    @PostMapping(value = "/new", params = "name", produces = MediaType.APPLICATION_JSON_VALUE)
    public Dashboard newEmptyDashboard(@RequestParam String name, Principal principal) throws UserNotRegisteredException, DashboardWithSameNameAlreadyExistsException {
        return dashboardService.newEmptyDashboard(principal.getName(), name);
    }

    /**
     * Endpoint to edit the current dashboard of the given user and update the object in the database.
     * 
     * @param dashboard dashboard object that contains all the necessary information for editing a dashboard
     * @param result validation result of the dashboard
     * @param principal logged in user
     * @return the successfully edited dashboard object from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (Bad Request 400)
     * @throws DashboardDoesntExistException the given dashboard does not exist in the database (Bad Request 400)
     * @throws DashboardDoesNotBelongToTheUserException the dashboard does not belong to the user (Bad Request 400)
     * @throws DashboardApiException invalid dashboard object in request (Bad Request 400)
     */
    @PutMapping(value = "/edit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public Dashboard editDashboard(@RequestBody @Valid Dashboard dashboard, BindingResult result, Principal principal) throws UserNotRegisteredException, DashboardDoesntExistException , DashboardDoesNotBelongToTheUserException, DashboardApiException {
        if (result.hasErrors()) {
            String msg = "Invalid Input";
            logger.error(msg);
            throw new DashboardApiException(msg);
        }
        return dashboardService.editDashboard(principal.getName(), dashboard);
    }

    /**
     * Endpoint to import an already created dashboard of another user and add it to the given user.
     * 
     * @param dashboard dashboard object that contains all the necessary information for importing a dashboard
     * @param result validation result of the dashboard
     * @param principal logged in user
     * @return the successfully imported dashboard object from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (Bad Request 400)
     * @throws DashboardApiException invalid dashboard object in request (Bad Request 400)
     */
    @PostMapping(value = "/import",  produces = MediaType.APPLICATION_JSON_VALUE)
    public Dashboard importDashboard(@RequestBody @Valid Dashboard dashboard, BindingResult result, Principal principal) throws UserNotRegisteredException, DashboardApiException {
        if (result.hasErrors()) {
            String msg = "Invalid Input";
            logger.error(msg);
            throw new DashboardApiException(msg);
        }
        return dashboardService.importDashboard(principal.getName(), dashboard);
    }

    /**
     * Endpoint to delete the dashboard of the given user.
     * 
     * @param id id of the dashboard to be deleted
     * @param principal logged in user
     * @throws UserNotRegisteredException the user with the given email is not registered (Bad Request 400)
     * @throws DashboardDoesntExistException the dashboard with the given id does not exist in the database (Bad Request 400)
     * @throws UserMustHaveOneDashboardException the user is not allowed to delete the last dashboard (Bad Request 400)
     * @throws DashboardDoesNotBelongToTheUserException the dashboard does not belong to the user (Bad Request 400)
     */
    @DeleteMapping(value = "/delete/{id}")
    public void deleteDashboard(@PathVariable long id, Principal principal) throws UserNotRegisteredException, DashboardDoesntExistException, UserMustHaveOneDashboardException, DashboardDoesNotBelongToTheUserException {
        dashboardService.deleteDashboard(id, principal.getName());
    }

    /**
     * Endpoint to get a list of all dashboards of the given user.
     * 
     * @param principal logged in user
     * @return list of all saved dashboards of the user from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (Bad Request 400)
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DashboardInfo> getAllDashboardsFromUser(Principal principal) throws UserNotRegisteredException {
        return dashboardService.getAllDashboardsFromUser(principal.getName());
    }

    /**
     * Endpoint to get the dashboard of a user to be currently displayed.
     * 
     * @param principal logged in user
     * @return the dashboard to be displayed at the current time from the database
     * @throws UserNotRegisteredException the user with the given email is not registered (Bad Request 400)
     */
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public Dashboard getCurrentDashboardFromUser(Principal principal) throws UserNotRegisteredException {
        return dashboardService.getCurrentDashboardFromUser(principal.getName());
    }
    
}
