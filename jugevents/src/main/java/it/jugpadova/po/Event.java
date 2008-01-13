package it.jugpadova.po;

import it.jugpadova.blo.FilterBo;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.parancoe.persistence.po.hibernate.EntityBase;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

/**
 * An event.
 *
 * @author Lucio Benfante
 */
@Entity
@NamedQueries(value = {@NamedQuery(name = "Event.findCurrentEvents", query =
        "from Event e where e.startDate >= current_date()"),
        @NamedQuery(name = "Event.findEventByPartialLocation", query =
        "from Event e where lower(e.location) like lower(?) order by e.location"),
        @NamedQuery(name = "Event.findEventByPartialLocationAndOwner", query =
        "from Event e where lower(e.location) like lower(?) and e.owner.user.username = ? order by e.location"),
        @NamedQuery(name = "Event.findUpcomingEvents", query =
        "from Event e where e.startDate >= current_date() and e.startDate <= ? order by e.startDate"),
        @NamedQuery(name = "Event.findNewEvents", query =
        "from Event e where e.startDate >= current_date() and e.creationDate >= ? order by e.startDate")})
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

    @ManyToOne
    @JoinColumn(name="OWNER_ID", nullable=false)
    public Jugger getOwner() {
        return owner;
    }

    public void setOwner(Jugger owner) {
        this.owner = owner;
    }

    @Column(length = 1024)
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Temporal(value = TemporalType.DATE)
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(length = 4096)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "event", cascade =
            {CascadeType.ALL})
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
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Transient
    public int getNumberOfParticipants() {
        int result = 0;
        if (getParticipants() != null) {
            for (Participant p : getParticipants()) {
                if (p.getConfirmed().booleanValue()) {
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
}