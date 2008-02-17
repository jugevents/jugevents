//Copyright 2006-2007 The Parancoe Team
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

import it.jugpadova.util.RRStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.parancoe.persistence.po.hibernate.EntityBase;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

/**
 * Represents informations concerning reliability request.
 * for a specific jugger.
 * @author Enrico Giurin
 *
 */
@Entity
public class ReliabilityRequest extends EntityBase implements Serializable {
	
	
	
	private int status = RRStatus.NOT_REQUIRED.value;
	private String motivation;
	@NotBlank
	private String adminResponse;
	private Date dateRequest;
	
	private Date dateAdminResponse;
	
	public String getAdminResponse() {
		return adminResponse;
	}
	public void setAdminResponse(String adminResponse) {
		this.adminResponse = adminResponse;
	}
	@Temporal(value = TemporalType.DATE)
	public Date getDateAdminResponse() {
		return dateAdminResponse;
	}
	public void setDateAdminResponse(Date dateAdminResponse) {
		this.dateAdminResponse = dateAdminResponse;
	}
	@Temporal(value = TemporalType.DATE)
	public Date getDateRequest() {
		return dateRequest;
	}
	public void setDateRequest(Date dateRequest) {
		this.dateRequest = dateRequest;
	}
	public String getMotivation() {
		return motivation;
	}
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	private String statusDescription()
	{
		RRStatus[] list = RRStatus.values();
		for(RRStatus rrs:list)
		{
			if(rrs.value==status)
				return rrs.description;
		}
		return RRStatus.NOT_REQUIRED.description;
	}
	@Transient
	public String getDescription() {
		return statusDescription();
	}	
	
	
}
