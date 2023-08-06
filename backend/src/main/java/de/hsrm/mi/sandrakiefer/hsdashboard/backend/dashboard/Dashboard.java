package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Weekday;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools.Widget;

/**
 * Dashboard class to manage the different dashboards of a user in the database.
 */
@Entity
public class Dashboard {

    @NotEmpty
    private String name;

    @NotNull
    private boolean synchroActive;

    @NotEmpty
    private String bgcolor;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Widget> widgets = new HashSet<Widget>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Weekday> weekdays = new ArrayList<Weekday>();

    @NotNull
    private boolean defaultDashboard;

    @PastOrPresent
    @DateTimeFormat(pattern = "dd.MM.yyyy - hh:mm:ss")
    private LocalDateTime lastChange;

    @NotNull
    private boolean startDashboard;

    @Id
    @GeneratedValue
    private long id;
    
    @Version
    private int version;

    public Dashboard() { }
    
    public Dashboard(String name, boolean synchroActive, String bgcolor, Set<Widget> widgets, List<Weekday> weekdays, boolean defaultDashboard,  boolean startDashboard) {
        this.name = name;
        this.synchroActive = synchroActive;
        this.bgcolor = bgcolor;
        this.widgets = widgets;
        this.weekdays = weekdays;
        this.defaultDashboard = defaultDashboard;
        this.lastChange = LocalDateTime.now();
        this.startDashboard = startDashboard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSynchroActive() {
        return synchroActive;
    }

    public void setSynchroActive(boolean synchroActive) {
        this.synchroActive = synchroActive;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public Set<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(Set<Widget> widgets) {
        this.widgets = widgets;
    }

    public List<Weekday> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<Weekday> weekdays) {
        this.weekdays = weekdays;
    }

    public boolean isDefaultDashboard() {
        return defaultDashboard;
    }

    public void setDefaultDashboard(boolean defaultDashboard) {
        this.defaultDashboard = defaultDashboard;
    }

    public LocalDateTime getLastChange() {
        return lastChange;
    }

    public void setLastChange(LocalDateTime lastChange) {
        this.lastChange = lastChange;
    }

    public boolean isStartDashboard() {
        return startDashboard;
    }

    public void setStartDashboard(boolean startDashboard) {
        this.startDashboard = startDashboard;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bgcolor == null) ? 0 : bgcolor.hashCode());
        result = prime * result + (defaultDashboard ? 1231 : 1237);
        result = prime * result + ((weekdays == null) ? 0 : weekdays.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((lastChange == null) ? 0 : lastChange.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (startDashboard ? 1231 : 1237);
        result = prime * result + (synchroActive ? 1231 : 1237);
        result = prime * result + version;
        result = prime * result + ((widgets == null) ? 0 : widgets.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dashboard other = (Dashboard) obj;
        if (bgcolor == null) {
            if (other.bgcolor != null)
                return false;
        } else if (!bgcolor.equals(other.bgcolor))
            return false;
        if (defaultDashboard != other.defaultDashboard)
            return false;
        if (weekdays == null) {
            if (other.weekdays != null)
                return false;
        } else if (!weekdays.equals(other.weekdays))
            return false;
        if (id != other.id)
            return false;
        if (lastChange == null) {
            if (other.lastChange != null)
                return false;
        } else if (!lastChange.equals(other.lastChange))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (startDashboard != other.startDashboard)
            return false;
        if (synchroActive != other.synchroActive)
            return false;
        if (version != other.version)
            return false;
        if (widgets == null) {
            if (other.widgets != null)
                return false;
        } else if (!widgets.equals(other.widgets))
            return false;
        return true;
    }

}
