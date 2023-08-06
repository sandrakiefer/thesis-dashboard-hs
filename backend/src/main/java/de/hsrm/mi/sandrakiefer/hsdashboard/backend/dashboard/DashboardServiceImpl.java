package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesNotBelongToTheUserException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardDoesntExistException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.DashboardWithSameNameAlreadyExistsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.UserAlreadyEditetDashboardsException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.exceptions.UserMustHaveOneDashboardException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.DashboardInfo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Weekday;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.WeekdayRepo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Widget;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.WidgetRepo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.User;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.UserService;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.UserNotRegisteredException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.exceptions.WrongRoleException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Role;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.TimeslotRepo;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepo dashboardRepo;

    @Autowired @Lazy
    private UserService userService;

    @Autowired
    private WidgetRepo widgetRepo;

    @Autowired
    private WeekdayRepo weekdayRepo;

    @Autowired
    private TimeslotRepo timeslotRepo;

    private Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Dashboard findDashboardById(long id) throws DashboardDoesntExistException {
        Optional<Dashboard> d = dashboardRepo.findById(id);
        if (d.isPresent()) {
            return d.get();
        } else {
            String msg = String.format("Dashboard with id '%d' doe not exist", id);
            logger.error(msg);
            throw new DashboardDoesntExistException(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dashboard getStartDashboard(String email) throws UserNotRegisteredException {
        try {
            Dashboard d = dashboardRepo.findByStartDashboardTrue().iterator().next();
            d.setStartDashboard(false);
            return d;
        } catch (NoSuchElementException e) {
            Set<Widget> defaultWidgets = new HashSet<Widget>();
            defaultWidgets.add(widgetRepo.save(new Widget("info", 0, new String[0])));
            defaultWidgets.add(widgetRepo.save(new Widget("weather", 1, new String[]{"Frankfurt am Main"})));
            defaultWidgets.add(widgetRepo.save(new Widget("news", 2, new String[]{"most"})));
            Dashboard d = new Dashboard("Hessenschau Start-Dashboard", true, "#D8E9F6", defaultWidgets, getEmptyTimeslotsForAllWeekdays(email), true, true);
            d.setWeekdays(weekdayRepo.saveAll(d.getWeekdays()));
            return dashboardRepo.save(d);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dashboard editStartDashboard(String email, Dashboard dashboard) throws UserNotRegisteredException, WrongRoleException {
        User u = userService.findUserByMail(email);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", email);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
        if (u.getRole() == Role.USER) {
            String msg = "Only editors are allowed to change the start dashboard";
            logger.error(msg);
            throw new WrongRoleException(msg);
        }
        Dashboard d = getStartDashboard(email);
        d.setStartDashboard(true);
        d.setSynchroActive(dashboard.isSynchroActive());
        d.setBgcolor(dashboard.getBgcolor());
        widgetRepo.deleteAll(d.getWidgets());
        Set<Widget> widgets = new HashSet<Widget>();
        widgets.addAll(dashboard.getWidgets());
        if (!dashboard.getWidgets().stream().anyMatch(w -> w.getType().equals("info"))) {
            for (Widget w : widgets) w.setPos(w.getPos() + 1);
            widgets.add(new Widget("info", 0, new String[0]));
        }
        widgetRepo.saveAll(widgets);
        d.setWidgets(widgets);
        d.setLastChange(LocalDateTime.now());
        return dashboardRepo.save(d);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Dashboard getDashboardFromUserWithId(String userEmail, long dashboardId) throws UserNotRegisteredException, DashboardDoesntExistException, DashboardDoesNotBelongToTheUserException {
        User u = userService.findUserByMail(userEmail);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", userEmail);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
        Dashboard d = findDashboardById(dashboardId);
        if (!u.getDashboards().stream().anyMatch(dash -> dash.getId() == d.getId())) {
            String msg = String.format("Dashboard with id '%d' does not belong to the user with email '%s'", d.getId(), userEmail);
            logger.error(msg);
            throw new DashboardDoesNotBelongToTheUserException(msg);
        }
        return d;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dashboard newEmptyDashboard(String userEmail, String dashboardName) throws UserNotRegisteredException, DashboardWithSameNameAlreadyExistsException {
        User u = userService.findUserByMail(userEmail);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", userEmail);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
        for (Dashboard d : u.getDashboards()) {
            if (d.getName().equals(dashboardName)) {
                String msg = String.format("User already has a dashboard saved with the name '%s'", dashboardName);
                logger.error(msg);
                throw new DashboardWithSameNameAlreadyExistsException(msg);
            }
        }
        Dashboard d = new Dashboard(dashboardName, true, "#D8E9F6", new HashSet<Widget>(), getEmptyTimeslotsForAllWeekdays(userEmail), false, false);
        d.setWeekdays(weekdayRepo.saveAll(d.getWeekdays()));
        d.setLastChange(LocalDateTime.now());
        d = dashboardRepo.save(d);
        u.addDashboard(d);
        userService.saveUserWithEditedDashboard(u);
        return d;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dashboard addStartDashboardToUser(String userEmail) throws UserNotRegisteredException, UserAlreadyEditetDashboardsException {
        User u = userService.findUserByMail(userEmail);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", userEmail);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
        if (u.getDashboards().size() > 0) {
            String msg = String.format("User with email '%s' is not newly registered and already has dashboards", userEmail);
            logger.error(msg);
            throw new UserAlreadyEditetDashboardsException(msg);
        }
        Dashboard defaultD = getStartDashboard(userEmail);
        Set<Widget> widgets = new HashSet<Widget>();
        for (Widget w : defaultD.getWidgets()) {
            widgets.add(widgetRepo.save(new Widget(w.getType(), w.getPos(), w.getSettings())));
        }
        Dashboard d = new Dashboard(defaultD.getName(), defaultD.isSynchroActive(), defaultD.getBgcolor(), widgets, getEmptyTimeslotsForAllWeekdays(userEmail), defaultD.isDefaultDashboard(), false);
        d.setWeekdays(weekdayRepo.saveAll(d.getWeekdays()));
        d = dashboardRepo.save(d);
        u.addDashboard(d);
        userService.saveUserWithEditedDashboard(u);
        return d;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dashboard editDashboard(String userEmail, Dashboard dashboard) throws UserNotRegisteredException, DashboardDoesntExistException, DashboardDoesNotBelongToTheUserException {
        User u = userService.findUserByMail(userEmail);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", userEmail);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
        Dashboard d = findDashboardById(dashboard.getId());
        if (!u.getDashboards().stream().anyMatch(dash -> dash.getId() == d.getId())) {
            String msg = String.format("Dashboard with id '%d' does not belong to the user with email '%s'", d.getId(), userEmail);
            logger.error(msg);
            throw new DashboardDoesNotBelongToTheUserException(msg);
        }
        if (!d.getWidgets().equals(dashboard.getWidgets())) {
            widgetRepo.deleteAll(d.getWidgets());
            widgetRepo.flush();
            dashboard.setWidgets(new HashSet<Widget>(widgetRepo.saveAll(dashboard.getWidgets())));
        }
        List<Weekday> weekdays = new ArrayList<Weekday>();
        if (!d.getWeekdays().equals(dashboard.getWeekdays())) {
            for (Weekday w : dashboard.getWeekdays()) {
                Weekday newW = new Weekday(w.getName(), new ArrayList<Timeslot>(), w.getWeekdayIndex());
                if (w.getTimeslots().size() > 0) {
                    List<Timeslot> newT = new ArrayList<Timeslot>();
                    for (Timeslot t : w.getTimeslots()) {
                        newT.add(new Timeslot(t.getStartTime(), t.getEndTime()));
                    }
                    timeslotRepo.deleteAll(w.getTimeslots());
                    newW.setTimeslots(timeslotRepo.saveAll(newT));
                }
                weekdayRepo.delete(w);
                weekdays.add(weekdayRepo.save(newW));
            }
            dashboard.setWeekdays(weekdays);
        }
        dashboard.setLastChange(LocalDateTime.now());
        dashboard = dashboardRepo.save(dashboard);
        u.addDashboard(dashboard);
        userService.saveUserWithEditedDashboard(u);
        return dashboard;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dashboard importDashboard(String userEmail, Dashboard dashboard) throws UserNotRegisteredException {
        User u = userService.findUserByMail(userEmail);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", userEmail);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
        Set<Widget> widgets = new HashSet<Widget>();
        for (Widget w : dashboard.getWidgets()) {
            widgets.add(widgetRepo.save(new Widget(w.getType(), w.getPos(), w.getSettings())));
        }
        Dashboard dImp = new Dashboard(dashboard.getName(), dashboard.isSynchroActive(), dashboard.getBgcolor(), widgets, getEmptyTimeslotsForAllWeekdays(userEmail), false, false);
        dImp.setWeekdays(weekdayRepo.saveAll(dImp.getWeekdays()));
        dImp.setLastChange(LocalDateTime.now());
        dImp = dashboardRepo.save(dImp);
        u.addDashboard(dImp);
        userService.saveUserWithEditedDashboard(u);
        return dImp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDashboard(long dashboardId, String userEmail) throws UserNotRegisteredException, DashboardDoesntExistException, UserMustHaveOneDashboardException, DashboardDoesNotBelongToTheUserException {
        User u = userService.findUserByMail(userEmail);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", userEmail);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
        if (u.getDashboards().size() < 2) {
            String msg = String.format("The user with email '%s' cannot delete their last dashboard", userEmail);
            logger.error(msg);
            throw new UserMustHaveOneDashboardException(msg);
        }
        Dashboard d = findDashboardById(dashboardId);
        if (!u.getDashboards().stream().anyMatch(dash -> dash.getId() == dashboardId)) {
            String msg = String.format("Dashboard with id '%d' does not belong to the user with email '%s'", dashboardId, userEmail);
            logger.error(msg);
            throw new DashboardDoesNotBelongToTheUserException(msg);
        }
        u.getDashboards().remove(d);
        for (Weekday w : d.getWeekdays()) {
            timeslotRepo.deleteAll(w.getTimeslots());
        }
        weekdayRepo.deleteAll(d.getWeekdays());
        widgetRepo.deleteAll(d.getWidgets());
        dashboardRepo.delete(d);
        userService.saveUserWithEditedDashboard(u);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DashboardInfo> getAllDashboardsFromUser(String userEmail) throws UserNotRegisteredException {
        User u = userService.findUserByMail(userEmail);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", userEmail);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
        List<DashboardInfo> ret = new ArrayList<DashboardInfo>();
        for (Dashboard d : u.getDashboards()) {
            ret.add(new DashboardInfo(d.getName(), d.getId()));
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dashboard getCurrentDashboardFromUser(String userEmail) throws UserNotRegisteredException {
        User u = userService.findUserByMail(userEmail);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", userEmail);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        }
        if (u.getDashboards().size() == 1) return u.getDashboards().iterator().next();
        for (Dashboard d : u.getDashboards()) {
            Weekday w = d.getWeekdays().stream().filter(o -> o.getWeekdayIndex() == (LocalDate.now().getDayOfWeek().getValue() - 1)).findFirst().get();
            for (int i = 0; i < u.getTimeslots().size(); i++) {
                LocalTime currentTime = LocalTime.now();
                Boolean targetInZone = (currentTime.isAfter(u.getTimeslots().get(i).getStartTime()) && currentTime.isBefore(u.getTimeslots().get(i).getEndTime()));
                if (targetInZone && w.getTimeslots().contains(u.getTimeslots().get(i))) return d;
            }
        }
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        for (Dashboard d : u.getDashboards()) if (d.isDefaultDashboard()) dashboards.add(d);
        dashboards.sort((d1, d2) -> d2.getLastChange().compareTo(d1.getLastChange()));
        if (dashboards.size() > 1) {
            List<Dashboard> all = new ArrayList<Dashboard>(u.getDashboards());
            all.sort((d1, d2) -> d2.getLastChange().compareTo(d1.getLastChange()));
            return all.get(0);
        } else {
            return dashboards.get(0);
        }
    }

    /**
     * Help function to generate empty timeslots for all weekdays of the given user.
     * 
     * @param userEmail email of the user
     * @return list of all days of the week with empty timeslots
     * @throws UserNotRegisteredException the user with the given email is not registered (bad request)
     */
    private List<Weekday> getEmptyTimeslotsForAllWeekdays(String userEmail) throws UserNotRegisteredException {
        User u = userService.findUserByMail(userEmail);
        if (u == null) {
            String msg = String.format("No user with email '%s' registered", userEmail);
            logger.error(msg);
            throw new UserNotRegisteredException(msg);
        } 
        String weekdaysString[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
        List<Weekday> weekdays = new ArrayList<Weekday>();
        for (int i = 0; i < 7; i++) {
            weekdays.add(new Weekday(weekdaysString[i], new ArrayList<Timeslot>(), i));
        }
        return weekdays;
    }
    
}
