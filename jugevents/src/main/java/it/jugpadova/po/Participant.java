// Copyright 2007 The Parancoe Team
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package it.jugpadova.po;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.parancoe.persistence.po.hibernate.EntityBase;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MaxLength;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

/**
 * The participant of an event.
 *
 * @author Lucio Benfante (<a href="lucio.benfante@jugpadova.it">lucio.benfante@jugpadova.it</a>)
 * @version $Revision: ec7c0560752e $
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Participant.findParticipantByEmailAndEventId",
        query="from Participant p where p.email = ? and p.event.id = ?"),
    @NamedQuery(name="Participant.findConfirmedParticipantsByEventId",
        query="from Participant p where p.event.id = ? and p.confirmed = true and p.cancelled != true order by p.creationDate, p.id"),
    @NamedQuery(name="Participant.findNotConfirmedParticipantsByEventId",
        query="from Participant p where p.event.id = ? and p.confirmed = false order by p.creationDate, p.id"),
    @NamedQuery(name="Participant.findCancelledParticipantsByEventId",
        query="from Participant p where p.event.id = ? and p.cancelled = true order by p.creationDate, p.id"),
    @NamedQuery(name="Participant.findPresentParticipantsByEventId",
        query="from Participant p where p.event.id = ? and p.attended = true"),
    @NamedQuery(name="Participant.findWinningParticipantsByEventId",
        query="from Participant p where p.event.id = ? and p.winner = true"),
    @NamedQuery(name="Participant.findNonwinningParticipantsByEventId",
        query="from Participant p where p.event.id = ? and (p.winner = null or p.winner = false)")
})
public class Participant extends EntityBase {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    private String confirmationCode;
    private Boolean confirmed;
    private Event event;
    private Date creationDate;
    private Boolean attended;
    private Date lastCertificateSentDate;
    private Date confirmationDate;
    private Boolean winner;
    @MaxLength(value=255)
    private String note;
    private Boolean cancelled = Boolean.FALSE;
    private Date cancellationDate;
    

    /** Creates a new instance of Participant */
    public Participant() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @ManyToOne
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getLastCertificateSentDate() {
        return lastCertificateSentDate;
    }

    public void setLastCertificateSentDate(Date lastCertificateSentDate) {
        this.lastCertificateSentDate = lastCertificateSentDate;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

	public Boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Date getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(Date cancellationDate) {
		this.cancellationDate = cancellationDate;
	}        
	
	/**
	 * Evaluate the confirmation to the event.
	 * @return
	 */
	public boolean canBeConfirmed()
	{
		return true;
		/*
		if((!cancelled&&!confirmed))
			return true;
		return false;
		*/
	}
	/**
	 * Evaluate the cancellation to the event.
	 * @return
	 */
	public boolean canBeCancelled()
	{
		return confirmed;
	}

}
