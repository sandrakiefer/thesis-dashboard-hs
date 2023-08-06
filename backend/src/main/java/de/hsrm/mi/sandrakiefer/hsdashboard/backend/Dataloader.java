package de.hsrm.mi.sandrakiefer.hsdashboard.backend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.Dashboard;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.DashboardService;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Weekday;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Widget;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.User;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.UserService;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Role;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;

/**
 * Component for loading test data into the in-memory database when the application is started.
 * 
 * User with credentials "ff@test.de" as email and "FFtestPW!?17" as password.
 * Editor with credentials "mm@test.de" as email and "MMtestPW!?17" as password.
 */
@Component
public class Dataloader {

    private final User u1 = new User("ff@test.de", "Friedfert", "Freundlich", new HashSet<Dashboard>(), "FFtestPW!?17", Role.USER);
    private final User u2 = new User("mm@test.de", "Max", "Mustermann", new HashSet<Dashboard>(), "MMtestPW!?17", Role.EDITOR);

    public Dataloader(UserService userService, DashboardService dashboardService) {
        userService.registerUser(u1);
        userService.registerUser(u2);
        Set<Widget> widgets = new HashSet<Widget>();
        widgets.add(new Widget("weather", 0, new String[]{"Wiesbaden"}));
        widgets.add(new Widget("corona", 1, new String[]{"Wiesbaden"}));
        widgets.add(new Widget("news", 2, new String[]{"most"}));
        widgets.add(new Widget("trafficStreet", 3, new String[]{"A3", "A5", "A66", "B42", "B455"}));
        widgets.add(new Widget("pollen", 4, new String[]{"Wiesbaden", "Hasel", "Birke", "Roggen", "Erle"}));
        widgets.add(new Widget("sport", 5, new String[]{"bl1", "Mainz"}));
        String weekdaysString[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
        List<Weekday> weekdays = new ArrayList<Weekday>();
        for (int i = 0; i < 7; i++) {
            if (i == 2) {
                List<Timeslot> timeslots = new ArrayList<Timeslot>();
                for (Timeslot t : u1.getTimeslots()) {
                    timeslots.add(new Timeslot(t.getStartTime(), t.getEndTime()));
                }
                weekdays.add(new Weekday(weekdaysString[i], timeslots, i));
            } else {
                weekdays.add(new Weekday(weekdaysString[i], new ArrayList<Timeslot>(), i));
            }
        }
        Dashboard d = dashboardService.newEmptyDashboard("ff@test.de", "Alltags Dashboard");
        d.setWidgets(widgets);
        d.setWeekdays(weekdays);
        dashboardService.editDashboard("ff@test.de", d);
    }
    
}
