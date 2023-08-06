package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;

@Entity
public class Weekday {

    private String name;

    private int weekdayIndex;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Timeslot> timeslots = new ArrayList<Timeslot>();

    @JsonIgnore
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @Version
    private int version;

    public Weekday() { }

    public Weekday(List<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public Weekday(String name, int weekdayIndex) {
        this.name = name;
        this.weekdayIndex = weekdayIndex;
    }

    public Weekday(String name, List<Timeslot> timeslots, int weekdayIndex) {
        this.name = name;
        this.timeslots = timeslots;
        this.weekdayIndex = weekdayIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public int getWeekdayIndex() {
        return weekdayIndex;
    }

    public void setWeekdayIndex(int weekdayIndex) {
        this.weekdayIndex = weekdayIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((timeslots == null) ? 0 : timeslots.hashCode());
        result = prime * result + version;
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
        Weekday other = (Weekday) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (timeslots == null) {
            if (other.timeslots != null)
                return false;
        } else if (!timeslots.equals(other.timeslots))
            return false;
        if (version != other.version)
            return false;
        return true;
    }
    
}
