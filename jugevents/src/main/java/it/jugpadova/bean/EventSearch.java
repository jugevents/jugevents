package it.jugpadova.bean;

import java.util.Date;

/**
 * Bean for the event search form.
 *
 * @author Lucio Benfante
 */
public class EventSearch {

    private String continent;
    private String country;
    private String jugName;
    private boolean pastEvents;
    private String orderByDate="asc";
    private Integer maxResults;
    private String friendlyName;
    private Date startDate;
    private Date endDate;

    public EventSearch() {
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getJugName() {
        return jugName;
    }

    public void setJugName(String jugName) {
        this.jugName = jugName;
    }

    public boolean isPastEvents() {
        return pastEvents;
    }

    public void setPastEvents(boolean pastEvents) {
        this.pastEvents = pastEvents;
    }

    public String getOrderByDate() {
        return orderByDate;
    }

    public void setOrderByDate(String orderByDate) {
        this.orderByDate = orderByDate;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

}
