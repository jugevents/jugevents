package it.jugpadova.po;

import it.jugpadova.blo.FilterBo;
import it.jugpadova.util.NotPassedEventsFilterFactory;
import it.jugpadova.util.Utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.parancoe.persistence.po.hibernate.EntityBase;
import org.springmodules.validation.bean.conf.loader.annotation.handler.CascadeValidation;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

/**
 * An event.
 *
 * @author Lucio Benfante
 */
@Entity
@Indexed
@NamedQueries(value = {
    @NamedQuery(name = "Event.findCurrentEvents", query =
    "from Event e where e.startDate >= current_date()"),
    @NamedQuery(name = "Event.findEventByPartialLocation", query =
    "from Event e where lower(e.location) like lower(?) order by e.location"),
    @NamedQuery(name = "Event.findEventByPartialLocationAndOwner",
    query =
    "from Event e where lower(e.location) like lower(?) and e.owner.user.username = ? order by e.location"),
    @NamedQuery(name = "Event.findUpcomingEvents",
    query =
    "from Event e where e.startDate >= current_date() and e.startDate <= ? order by e.startDate"),
    @NamedQuery(name = "Event.findNewEvents",
    query =
    "from Event e where e.startDate >= current_date() and e.creationDate >= ? order by e.startDate")
})
@FullTextFilterDefs({
    @FullTextFilterDef(name = "notPassedEvents", impl =
    NotPassedEventsFilterFactory.class)
})
public class Event extends EntityBase {

    @NotBlank
    private String title;
    @org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull
    private Date startDate;
    private String startTime;
    private Date endDate;
    private String endTime;
    @NotBlank
    private String location;
    private String directions;
    private String description;
    private String filter = "Textile";
    private Jugger owner;
    private Date creationDate;
    @CascadeValidation
    private Registration registration;
    private List<EventResource> eventResources;
    private List<Speaker> speakers = new ArrayList<Speaker>();
    private byte[] badgeTemplate;
    private Date reminderDate;
    private boolean activeReminder = false;
    public final static int NUM_OF_DAYS_REMINDER_BEFORE_EVENT = 2;

    /**
     * Get the entity id.
     * 
     * @return the entity id.
     */
    @Id
    @DocumentId
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return this.id;
    }

    @ManyToOne
    public Jugger getOwner() {
        return owner;
    }

    public void setOwner(Jugger owner) {
        this.owner = owner;
    }

    @Column(length = 1024)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
    private List<Participant> participants;

    /** Creates a new instance of Event */
    public Event() {
    }

    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Temporal(value = TemporalType.DATE)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution=Resolution.DAY)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;        
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Temporal(value = TemporalType.DATE)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Lob
    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "event", cascade = {CascadeType.ALL})
    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipant(Participant participant) {
        if (this.participants == null) {
            this.participants =
                    new LinkedList<Participant>();
        }
        this.participants.add(participant);
        participant.setEvent(this);
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Embedded
    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    @OneToMany(mappedBy = "event", cascade = {CascadeType.ALL})
    public List<EventResource> getEventResources() {
        return eventResources;
    }

    public void setEventResources(List<EventResource> eventResources) {
        this.eventResources = eventResources;
    }

    @OneToMany(mappedBy = "event")
    @org.hibernate.annotations.Cascade({
        org.hibernate.annotations.CascadeType.ALL,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    public List<Speaker> getSpeakers() {
        if (this.speakers == null) {
            this.speakers = new ArrayList<Speaker>();
        }
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public byte[] getBadgeTemplate() {
        return badgeTemplate;
    }

    public void setBadgeTemplate(byte[] badgeTemplate) {
        this.badgeTemplate = badgeTemplate;
    }

    @Transient
    public int getNumberOfParticipants() {
        int result = 0;
        if (getParticipants() != null) {
            for (Participant p : getParticipants()) {
                if (p.hasValidRegistration()) {
                    result++;
                }
            }
        }
        return result;
    }

    @Transient
    public String getFilteredDirections() {
        String filteredDirections =
                FilterBo.filterText(this.getDirections(), this.getFilter(),
                false);
        return filteredDirections;
    }

    @Transient
    public String getFilteredDescription() {
        String filteredDescription =
                FilterBo.filterText(this.getDescription(), this.getFilter(),
                false);
        return filteredDescription;
    }

    /**
     * Decide if the registration is open or not, based on the event data
     * 
     * @param event The event
     * @return true if the registration is open. False, otherwise.
     */
    @Transient
    public boolean getRegistrationOpen() {
        boolean result = false;
        final Registration reg = this.getRegistration();
        if (thereAreNoRegistrationRules(reg)) { // old way
            result = todayIsBeforeTheEndOfTheStartDay();
        } else {
            if (reg.getEnabled().booleanValue()) {
                if (reg.getManualActivation() != null) {
                    result = reg.getManualActivation();
                } else {
                    // TODO complete with other rules
                    if (partecipantsAreUnderTheLimit(reg) && todayIsInTheRegistrationInterval(
                            reg)) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Return the name of the hosting organization.
     * 
     * @return The name of the hosting organization. Or "" if there is no hosting organization.
     */
    @Transient
    public String getHostingOrganizationName() {
        String result = "";
        if (this.getOwner() != null) {
            result = this.getOwner().getJug().getName();
        }
        return result;
    }

    /**
     * Return the URL of the hosting organization.
     * 
     * @return The URL of the hosting organization. Or "" if there is no hosting organization URL/Web site.
     */
    @Transient
    public String getHostingOrganizationUrl() {
        String result = "";
        if (this.getOwner() != null) {
            result = this.getOwner().getJug().getWebSiteUrl();
        }
        return result;
    }

    private boolean partecipantsAreUnderTheLimit(final Registration reg) {
        return reg.getMaxParticipants() == null || this.getNumberOfParticipants() < reg.
                getMaxParticipants().longValue();
    }

    private boolean todayIsBeforeTheEndOfTheStartDay() {
    	return Utilities.todayIsBeforeDate(this.startDate);        
    }

    private boolean thereAreNoRegistrationRules(final Registration reg) {
        return reg == null || (reg != null && reg.getEnabled() && reg.
                getManualActivation() == null && reg.getStartRegistration()
                == null && reg.getEndRegistration() == null && reg.
                getMaxParticipants() == null);
    }

    private boolean todayIsInTheRegistrationInterval(Registration reg) {
        boolean result = true;
        Date now = new Date();
        if ((reg.getStartRegistration() != null && reg.getStartRegistration().
                compareTo(now) > 0) || (reg.getEndRegistration() != null && reg.
                getEndRegistration().compareTo(now) < 0) || (reg.
                getEndRegistration() == null
                && !todayIsBeforeTheEndOfTheStartDay())) {
            result = false;
        }
        return result;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getReminderDate() {
        return reminderDate;
    }

    
    
    /**
     * Returns a convenient subject for the mail relating to the event itself.
     * Possible usages  are for jug contact's email and speakers.
     * The value is encoded.
     * @return
     */
    @Transient
    public String getSubjectForEmailContact() throws UnsupportedEncodingException
    {
    	return URLEncoder.encode(this.getTitle(), "UTF-8");
    }
  
    
	
	
	/**
	 * This method is called from the EventEditController to calculate 
	 * the date of reminder date.
	 * Currently reminderDate corresponds to 2 days earlier the event 
	 * start date.
	 */
	public void updateReminderDate()
	{
		if(this.getActiveReminder())
		{
			GregorianCalendar gc = new GregorianCalendar();
	        gc.setTime(this.getStartDate());
	        gc.add(GregorianCalendar.DAY_OF_YEAR, -NUM_OF_DAYS_REMINDER_BEFORE_EVENT);
	        this.setReminderDate(gc.getTime());
		}
		else
		{
			this.setReminderDate(null);
		}
			
	}
	@Transient
	public boolean getActiveReminder() {
		return activeReminder;
	}

	public void setActiveReminder(boolean activeReminder) {
		this.activeReminder = activeReminder;
	}
	
	@Transient
	public boolean isEventInThePast() {
        return !Utilities.todayIsBeforeDate(this.endDate);
    }
}
