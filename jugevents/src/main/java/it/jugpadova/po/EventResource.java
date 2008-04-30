package it.jugpadova.po;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.parancoe.persistence.po.hibernate.EntityBase;

/**
 * Resource associated to an event.
 * 
 * @author lucio
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class EventResource extends EntityBase {
    private String description;
    private Event event;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    
}
