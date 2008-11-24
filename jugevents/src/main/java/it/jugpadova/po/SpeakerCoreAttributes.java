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
package it.jugpadova.po;

import javax.persistence.Entity;

import org.parancoe.persistence.po.hibernate.EntityBase;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

/**
 * Represents core attributes of the speaker, not bound to a particular event.
 * @author Enrico Giurin
 *
 */
@Entity
public class SpeakerCoreAttributes extends EntityBase {
	
	

	public SpeakerCoreAttributes(String firstName, String lastName, String email) {
		super();
		setEmail(email);
		setFirstName(firstName);
		setLastName(lastName);
	}
	public SpeakerCoreAttributes() {
		super();
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	
	
	@Email
	private String email;
	private String url;
	private String skypeId;
	public String getSkypeId() {
		return skypeId;
	}
	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
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
	
	/**
	 * Converts firstName to lowercase before inserting.
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName.toLowerCase();
	}
    /**
     * Converts lastName to lowercase before inserting.
     * @param lastName
     */
	public void setLastName(String lastName) {
		this.lastName = lastName.toLowerCase();
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "firstName: "+firstName+" - lastName: "+lastName+" - email: "+email;
	}

	
	

}