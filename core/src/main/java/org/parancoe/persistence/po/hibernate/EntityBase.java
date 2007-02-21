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
package org.parancoe.persistence.po.hibernate;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * A base class for all persistent entities.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision: c468d0b3224e $
 */
@MappedSuperclass
public abstract class EntityBase implements Entity, Serializable {
    
    /**
     * Creates a new instance of EntityBase
     */
    public EntityBase() {
    }

    
    /**
     * The entity id
     */
    protected Long id;

    /**
     * The entity version for optimistic locking
     */
    protected Integer version;


    /**
     * Get the entity id.
     * 
     * @return the entity id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return this.id;
    }

    /**
     * Get the version for optimistic locking.
     *
     * @return The version fooptimistic locking
     */
    @Version
    public Integer getVersion() {
        return this.version;
    }


    /**
     * Set the entity id.
     *
     * @param id the entity id
     */
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Set the version for optimistic locking
     *
     * @param version The version for optimistic locking
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Default implementation of the equals method.
     * It return true if the objects are of the same type and have
     * the same id.
     *
     * @param obj The object to compare with this
     * @return true if the objects are equals
     */
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && this.getClass().getName().equals(obj.getClass().getName())) {            
            if (this.getId() != null && this.getId().equals(((EntityBase)obj).getId())) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Default implementation of the hashcode method for an Entity.
     * It return the hascode of the Id, if the Id is not null.
     * It return the hascode of the Object, if the Id is null.
     *
     * @param obj The object to compare with this
     * @return true if the objects are equals
     */
    @Override
    public int hashCode() {
        int result;
        if (this.getId() != null) {
            result = this.getId().hashCode();
        } else {
            result = super.hashCode();
        }
        return result;            
    }



}
