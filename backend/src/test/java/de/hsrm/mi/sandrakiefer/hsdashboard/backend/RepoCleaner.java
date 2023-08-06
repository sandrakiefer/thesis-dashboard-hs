package de.hsrm.mi.sandrakiefer.hsdashboard.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.DashboardRepo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.WeekdayRepo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.WidgetRepo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.UserRepo;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.TimeslotRepo;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class RepoCleaner {

    private UserRepo userRepo;
    private TimeslotRepo timeslotRepo;
    private DashboardRepo dashboardRepo;
    private WidgetRepo widgetRepo;
    private WeekdayRepo weekdayRepo;

    public RepoCleaner(UserRepo userRepo, TimeslotRepo timeslotRepo, DashboardRepo dashboardRepo, WidgetRepo widgetRepo, WeekdayRepo weekdayRepo) {
        this.timeslotRepo = timeslotRepo;
        this.userRepo = userRepo;
        this.weekdayRepo = weekdayRepo;
        this.widgetRepo = widgetRepo;
        this.dashboardRepo = dashboardRepo;
    }

    @Transactional
    public void clean() {
        List<JpaRepository<?, ?>> repoOrder = List.of(userRepo, dashboardRepo, widgetRepo, weekdayRepo, timeslotRepo);
        for (JpaRepository<?, ?> repo : repoOrder) {
            if (repo.findAll().size() > 0) {
                repo.deleteAll();
            }
        }
    }
    
}
