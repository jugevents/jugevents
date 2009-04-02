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

import it.jugpadova.blo.FilterBo;
import it.jugpadova.controllers.BinController;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.parancoe.persistence.po.hibernate.EntityBase;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

/**
 * Represents information about the speaker of the event.
 * @author Enrico Giurin
 *
 */
@Entity
@Indexed
@NamedQueries(value = {@NamedQuery(name = "Speaker.findEventsByPartialSpeakerEmail", query =
"select e from Event e, Speaker s  where s.event=e and upper(s.email) like upper(?)"),
@NamedQuery(name = "Speaker.allByEvent", query =
"select s from Event e, Speaker s where s.event=e and e.id=?"),
@NamedQuery(name = "Speaker.findSpeakersByPartialEventTitle", query =
"select s from Event e, Speaker s where s.event=e  and upper(e.title) like upper(?)")})



public class Speaker extends EntityBase {
	
	private static final long serialVersionUID = 1L;
	  private static final Logger logger =
          Logger.getLogger(Speaker.class);
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;		
	@NotBlank
	@Email
	private String email;
	@RegExp(value="[A-Za-z0-9:/.-]*")
	private String url;
	//@RegExp(value="[A-Za-z0-9]*")
	private String skypeId;	
	private byte[] picture;
	@NotBlank
	private String resume;	
	private Event event;
	private String filter = "Textile";
	private Long indexSpeaker;
	
	@Transient
	public Long getIndexSpeaker() {
		return indexSpeaker;
	}


	public void setIndexSpeaker(Long indexSpeaker) {
		this.indexSpeaker = indexSpeaker;
	}


	@Transient
	public String getFilter() {
		return filter;
	}


	public void setFilter(String filter) {
		this.filter = filter;
	}


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
	
	
	//set size to 1MB because BLOB of MySQL is not enough to store
	//binary data bigger than 64k
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=1048576)    
	public byte[] getPicture() {
		return picture;
	}
	public void setPicture(byte[] picture) {
		if((picture==null)||(picture.length==0))
		{
			//TODO manage in a clean way when picture is null
			logger.debug("Trying to set null picture has failed");
			return;
		}
		this.picture = picture;
	}
	@Column(length = 4096)
	@Field(index = Index.TOKENIZED, store = Store.NO)    
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
	
	
	
	public String getSkypeId() {
		return skypeId;
	}
	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Field(index = Index.TOKENIZED, store = Store.NO)    
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@Field(index = Index.TOKENIZED, store = Store.NO)    
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "firstName: "+firstName+" - lastName: "+lastName+" - email: "+email;
	}
	
	@Transient
    public String getFilteredPreview() {
        String filteredPreview =
                FilterBo.filterText(this.getResume(), this.getFilter(),
                false);
        return filteredPreview;
    }


	

}
