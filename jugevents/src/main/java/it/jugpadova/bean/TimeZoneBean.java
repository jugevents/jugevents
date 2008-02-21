package it.jugpadova.bean;

/**
 * A bean representing a TimeZone in a list.
 * 
 * @author lucio
 */
public class TimeZoneBean {
    private String id;
    private String description;

    public TimeZoneBean(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
        
}
