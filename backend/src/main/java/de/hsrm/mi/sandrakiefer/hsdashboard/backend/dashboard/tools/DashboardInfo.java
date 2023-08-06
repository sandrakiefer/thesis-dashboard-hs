package de.hsrm.mi.sandrakiefer.hsdashboard.backend.dashboard.tools;

/**
 * Response object to return a list of dashboards, but only with th required information.
 */
public class DashboardInfo {

    private String name;
    private long id;

    public DashboardInfo(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
}
