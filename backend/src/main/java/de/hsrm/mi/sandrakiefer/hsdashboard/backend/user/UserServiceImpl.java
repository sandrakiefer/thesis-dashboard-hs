package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.Dashboard;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.DashboardService;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesNotBelongToTheUserException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesntExistException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Weekday;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserAlreadyExistsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserNotRegisteredException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.TimeslotRepo;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TimeslotRepo timeslotRepo;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public User findUserByMail(String email) throws UserNotRegisteredException {
        Optional<User> u = userRepo.findById(email);
        if (u.isPresent()) {
            return u.get();
        } else {
            String msg = String.format("User with email '%s' is not registered", email);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User registerUser(User newUser) throws UserAlreadyExistsException {
        if (userRepo.findById(newUser.getEmail()).isPresent()) {
            String msg = String.format("User with email '%s' already exists", newUser.getEmail());
            logger.error(msg);
            throw new UserAlreadyExistsException(msg);
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setTimeslots(timeslotRepo.saveAll(newUser.getTimeslots()));
        User u = userRepo.save(newUser);
        dashboardService.addStartDashboardToUser(newUser.getEmail());
        return u;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User saveUserWithEditedDashboard(User user) {
        return userRepo.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Timeslot> getTimeslotsOfUser(String email) throws UserNotRegisteredException {
        return findUserByMail(email).getTimeslots();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Timeslot> changeTimeslots(String email, List<Timeslot> timeslots) throws UserNotRegisteredException {
        User u = findUserByMail(email);
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        for (Dashboard d : u.getDashboards()) {
            List<Weekday> weekdays = new ArrayList<Weekday>();
            for (int i = 0; i < d.getWeekdays().size(); i++) {
                weekdays.add(d.getWeekdays().get(i));
                if (weekdays.get(i).getTimeslots().size() > 0) {
                    List<Timeslot> newTime = new ArrayList<Timeslot>();
                    for (Timeslot t : weekdays.get(i).getTimeslots()) {
                        for (int x = 0; x < u.getTimeslots().size(); x++) {
                            if (t.equals(u.getTimeslots().get(x))) newTime.add(new Timeslot(timeslots.get(x).getStartTime(), timeslots.get(x).getEndTime()));
                        }
                    }
                    weekdays.get(i).setTimeslots(newTime);
                }
            }
            dashboards.add(d);
        }
        for (Dashboard d : dashboards) {
            dashboardService.editDashboard(email, d);
        }
        timeslotRepo.deleteAll(u.getTimeslots());
        u.setTimeslots(timeslotRepo.saveAll(timeslots));
        u = userRepo.save(u);
        return u.getTimeslots();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Weekday> getOccupiedTimeslotsOfAllWeekdays(String email, long dashboardId) throws UserNotRegisteredException, DashboardDoesntExistException, DashboardDoesNotBelongToTheUserException {
        User u = findUserByMail(email);
        Dashboard request = dashboardService.getDashboardFromUserWithId(email, dashboardId);
        List<Weekday> occupiedTimeslotsOfAllWeekdays = new ArrayList<Weekday>();
        for (Dashboard d : u.getDashboards()) {
            if (!d.equals(request)) {
                if (occupiedTimeslotsOfAllWeekdays.isEmpty()) {
                    for (Weekday w : d.getWeekdays()) {
                        List<Timeslot> timeslots = new ArrayList<Timeslot>();
                        for (Timeslot t : w.getTimeslots()) {
                            timeslots.add(new Timeslot(t.getStartTime(), t.getEndTime()));
                        }
                        occupiedTimeslotsOfAllWeekdays.add(new Weekday(w.getName(), timeslots, w.getWeekdayIndex()));
                    }
                }
                for (Weekday w : d.getWeekdays()) {
                    for (Timeslot t : w.getTimeslots()) {
                        if (!occupiedTimeslotsOfAllWeekdays.get(w.getWeekdayIndex()).getTimeslots().contains(t)) occupiedTimeslotsOfAllWeekdays.get(w.getWeekdayIndex()).getTimeslots().add(new Timeslot(t.getStartTime(), t.getEndTime()));
                    }
                }
            }
        }
        if (occupiedTimeslotsOfAllWeekdays.isEmpty()) {
            String weekdaysString[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
            for (int i = 0; i < 7; i++) {
                occupiedTimeslotsOfAllWeekdays.add(new Weekday(weekdaysString[i], new ArrayList<Timeslot>(), i));
            }
        }
        return occupiedTimeslotsOfAllWeekdays;
    }
    
}
