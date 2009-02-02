package it.jugpadova.bean;

import it.jugpadova.po.Event;

import java.util.Date;
import org.apache.commons.lang.StringUtils;

/**
 * A message in the news and upcomings list.
 *
 * @author Lucio Benfante
 */
public class NewsMessage {

    public static final int TITLE_MAX_LENGTH = 50;
    public static final String TYPE_NEWS = "NEWS";
    public static final String TYPE_NEW_EVENT = "NEW_EVENT";
    public static final String TYPE_UPCOMING_EVENT = "UPCOMING_EVENT";
    private String type;
    private Date date;
    private String message;
    private Event event;
    private String baseUrl;

    public NewsMessage() {
    }

    public NewsMessage(String type, Date date, String message, String baseUrl) {
        this.type = type;
        this.date = date;
        this.message = message;
        this.baseUrl = baseUrl;
    }

    public NewsMessage(String type, Date date, Event event, String baseUrl) {
        this.type = type;
        this.date = date;
        this.event = event;
        this.baseUrl = baseUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Object[] getArguments() {
        if (TYPE_UPCOMING_EVENT.equals(this.type) ||
                TYPE_NEW_EVENT.equals(this.type)) {
            if (event.getOwner() != null) {
                return new Object[]{getEventTitle(), getJugName(),
                            getJugUrl(), getEventUrl()
                        };
            } else {
                return new Object[]{getEventTitle(), "JUG Events",
                            baseUrl, getEventUrl()
                        };
            }
        }
        return new Object[]{};
    }

    private String getEventUrl(String baseUrl, Event event) {
        return baseUrl + "/event/"+event.getId();
    }

    public String getEventTitle() {
        return StringUtils.abbreviate(event.getTitle(), TITLE_MAX_LENGTH);
    }

    public String getJugName() {
        return event.getOwner().getJug().getName();
    }

    public String getJugUrl() {
        return event.getOwner().getJug().getWebSiteUrl();
    }

    public String getEventUrl() {
        return getEventUrl(baseUrl, event);
    }
}
