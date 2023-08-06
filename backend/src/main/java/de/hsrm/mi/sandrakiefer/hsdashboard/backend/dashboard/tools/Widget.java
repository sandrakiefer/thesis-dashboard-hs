package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Widget class to manage the different widgets for a dashboard in the database.
 */
@Entity
public class Widget {

    @NotEmpty
    private String type;

    @NotNull
    private int pos;

    @NotNull
    private String[] settings;

    @JsonIgnore
    @Id
    @GeneratedValue
    private long id;

    @Version
    private int version;

    public Widget() { }

    public Widget(String type, int pos, String[] settings) {
        this.type = type;
        this.pos = pos;
        this.settings = settings;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String[] getSettings() {
        return settings;
    }

    public void setSettings(String[] settings) {
        this.settings = settings;
    }

    public long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + pos;
        result = prime * result + Arrays.hashCode(settings);
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Widget other = (Widget) obj;
        if (id != other.id)
            return false;
        if (pos != other.pos)
            return false;
        if (!Arrays.equals(settings, other.settings))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (version != other.version)
            return false;
        return true;
    }
    
}
