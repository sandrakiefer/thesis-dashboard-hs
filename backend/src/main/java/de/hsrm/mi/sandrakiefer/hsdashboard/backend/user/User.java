package de.hsrm.mi.sandrakiefer.hsdashboard.backend.user;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.Dashboard;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Role;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.tools.Timeslot;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.user.vaildation.password.ValidPassword;

/**
 * User class to manage the different users in the database.
 */
@Entity
public class User {

    @Id
    @Email
    @NotEmpty
    @Column(unique=true)
    private String email;

    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;

    @OneToMany
    private List<Timeslot> timeslots = new ArrayList<Timeslot>();

    @OneToMany
    private Set<Dashboard> dashboards = new HashSet<Dashboard>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ValidPassword
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Version
    private int version;

    public User() { }

    public User(String email, String firstname, String lastname, Set<Dashboard> dashboards, String password, Role role) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dashboards = dashboards;
        this.password = password;
        this.role = role;
        this.timeslots.add(0, new Timeslot(LocalTime.of(6, 0), LocalTime.of(11, 59, 59)));
        this.timeslots.add(1, new Timeslot(LocalTime.of(12, 0), LocalTime.of(16, 59, 59)));
        this.timeslots.add(2, new Timeslot(LocalTime.of(17, 0), LocalTime.of(21, 59, 59)));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFullName() {
        return String.format("%s %s", this.getFirstname(), this.getLastname());
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public Set<Dashboard> getDashboards() {
        return dashboards;
    }

    public void setDashboards(Set<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }

    public void addDashboard(Dashboard d) {
        this.dashboards.add(d);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
        result = prime * result + ((dashboards == null) ? 0 : dashboards.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
        result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
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
        User other = (User) obj;
        if (dashboards == null) {
            if (other.dashboards != null)
                return false;
        } else if (!dashboards.equals(other.dashboards))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (firstname == null) {
            if (other.firstname != null)
                return false;
        } else if (!firstname.equals(other.firstname))
            return false;
        if (lastname == null) {
            if (other.lastname != null)
                return false;
        } else if (!lastname.equals(other.lastname))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (role != other.role)
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
