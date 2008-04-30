package it.jugpadova.po;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.apache.commons.lang.StringUtils;

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

    @Transient
    public String getAbbreviatedUrl() {
        return StringUtils.abbreviate(url, 40);
    }
}
