package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesNotBelongToTheUserException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesntExistException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Weekday;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserNotRegisteredException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;

/**
 * Rest Controller to manage the different requests for users, mapped on the path "/api/user".
 */
@RestController
@RequestMapping("/api/user")
public class UserRestApi {

    @Autowired
    private UserService userService;

    /**
     * Endpoint to get the timeslots from a user.
     * 
     * @param principal logged in user
     * @return list of timeslots from a user
     * @throws UserNotRegisteredException no user can be found in the database with this email (Bad Request 400)
     */
    @GetMapping(value = "/timeslots", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Timeslot> getTimeslotsOfUser(Principal principal) throws UserNotRegisteredException {
        return userService.getTimeslotsOfUser(principal.getName());
    }

    /**
     * Endpoint to change the timeslots from a user. 
     * 
     * @param timeslots list of new timeslots for morning, midday and evening
     * @param principal logged in user
     * @return list of timeslots from the user stored in the database
     * @throws UserNotRegisteredException no user can be found in the database with this email (Bad Request 400)
     */
    @PutMapping(value = "/timeslots", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Timeslot> changeTimeslots(@RequestBody List<Timeslot> timeslots, Principal principal) throws UserNotRegisteredException {
        return userService.changeTimeslots(principal.getName(), timeslots);
    }

    /**
     * Endpoint to get the occupied timeslots of all weekdays from a user with the current dashboard.
     * 
     * @param id id of the current dashboard, to be ignorred in calculation
     * @param principal logger in user
     * @return list of all weekdays containing the occupied timeslots
     * @throws UserNotRegisteredException no user can be found in the database with this email (Bad Request 400)
     * @throws DashboardDoesntExistException the dashboard with the given id does not exist in the database (Bad Request 400)
     * @throws DashboardDoesNotBelongToTheUserException the dashboard does not belong to the user (Bad Request 400)
     */
    @GetMapping(value = "/displaytime/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Weekday> getOccupiedTimeslotsOfAllWeekdays(@PathVariable long id, Principal principal) throws UserNotRegisteredException, DashboardDoesntExistException, DashboardDoesNotBelongToTheUserException {
        return userService.getOccupiedTimeslotsOfAllWeekdays(principal.getName(), id);
    }

    /**
     * Endpoint to get the name of the user.
     * 
     * @param principal logged in user
     * @return name as string of the user
     * @throws UserNotRegisteredException no user can be found in the database with this email (Bad Request 400)
     */
    @GetMapping(value="/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getNameOfUser(Principal principal) throws UserNotRegisteredException {
        return userService.findUserByMail(principal.getName()).getFullName();
    }

    /**
     * Endpoint to get the role of the user.
     * 
     * @param principal logged in user
     * @return role as string of the user
     * @throws UserNotRegisteredException no user can be found in the database with this email (Bad Request 400)
     */
    @GetMapping(value="/role", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getRoleOfUser(Principal principal) throws UserNotRegisteredException {
        return userService.findUserByMail(principal.getName()).getRole().toString();
    }
    
}
