package it.jugpadova.po;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expression;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Min;

/**
 * Registration data of an event (not participants)
 * 
 * @author lucio
 */
@Embeddable
@Expression(errorCode = "startRegistrationAfterEndRegistration",
message = "?startRegistrationAfterEndRegistration?",
applyIf = "startRegistration is not null and endRegistration is not null",
value = "startRegistration <= endRegistration")
public class Registration implements Serializable {

    private Boolean enabled = Boolean.TRUE;
    private Date startRegistration;
    private Date endRegistration;
    @Min(value = 0, inclusive = false)
    private Long maxParticipants;
    private Boolean manualActivation;
    private Boolean showParticipants = Boolean.FALSE;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getManualActivation() {
        return manualActivation;
    }

    public void setManualActivation(Boolean manualActivation) {
        this.manualActivation = manualActivation;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getEndRegistration() {
        return endRegistration;
    }

    public void setEndRegistration(Date endRegistration) {
        this.endRegistration = endRegistration;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartRegistration() {
        return startRegistration;
    }

    public void setStartRegistration(Date startRegistration) {
        this.startRegistration = startRegistration;
    }

    public Long getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Long maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Boolean getShowParticipants() {
        return showParticipants;
    }

    public void setShowParticipants(Boolean showParticipants) {
        this.showParticipants = showParticipants;
    }
    
    @Transient
    public boolean getRegistrationRulesAreApplied() {
        return this.enabled && (this.manualActivation == null);
    }
}
