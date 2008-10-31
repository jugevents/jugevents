// Copyright 2006-2008 The JUG Events Team
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
/**
 * 
 */
package it.jugpadova.po;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.parancoe.persistence.po.hibernate.EntityBase;

/**
 * Represents information about the speaker of the event.
 * @author Enrico Giurin
 *
 */
@Entity
@NamedQueries(value = {@NamedQuery(name = "Speaker.findEventsByPartialSpeakerEmail", query =
"select e from Event e, Speaker s, SpeakerCoreAttributes sca where s.event=e and s.speakerCoreAttributes=sca and upper(sca.email) like upper(?)"),
@NamedQuery(name = "Speaker.findSpeakersByPartialEventTitle", query =
"select s from Event e, Speaker s, SpeakerCoreAttributes sca where s.event=e and s.speakerCoreAttributes=sca and upper(e.title) like upper(?)")})

public class Speaker extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpeakerCoreAttributes speakerCoreAttributes;
	private Event event;
	private byte[] picture;
	private String resume;
	@ManyToOne
	public SpeakerCoreAttributes getSpeakerCoreAttributes() {
		return speakerCoreAttributes;
	}
	public void setSpeakerCoreAttributes(SpeakerCoreAttributes speakerCoreAttributes) {
		this.speakerCoreAttributes = speakerCoreAttributes;
	}
	public byte[] getPicture() {
		return picture;
	}
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	@ManyToOne
	public Event getEvent() {
		return event;
	}

}
