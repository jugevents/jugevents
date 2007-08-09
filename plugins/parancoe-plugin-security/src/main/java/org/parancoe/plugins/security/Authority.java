// Copyright 2006-2007 The Parancoe Team
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
package org.parancoe.plugins.security;

import java.util.List;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.parancoe.persistence.po.hibernate.EntityBase;

/**
 * A PO for Authority table.
 *
 * @author <a href="mailto:enrico.giurin@gmail.com">Enrico Giurin</a>
 * @version $Revision: 13dae6883de3 $
 */
@javax.persistence.Entity
public class Authority extends EntityBase {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String description = null;
    private String role = null;
    private List<UserAuthority> userAuthority;
    
    /**
     * Empty constructor
     *
     */
    public Authority()
    {
    	
    }
    /**
     * Constructor with role.
     * @param role
     */
    public Authority(String role)
    {
    	this.role = role;
    	
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy="authority")
    public List<UserAuthority> getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(List<UserAuthority> userAuthority) {
        this.userAuthority = userAuthority;
    }

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "role: "+role+" - description: "+description;
	}
    
}
