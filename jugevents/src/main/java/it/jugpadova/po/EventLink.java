package it.jugpadova.po;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author lucio
 */
@Entity
@DiscriminatorValue("Link")
public class EventLink extends EventResource {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }        

}
