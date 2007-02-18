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
package org.parancoe.persistence.dao.generic;

import org.parancoe.persistence.po.hibernate.VersionedEntityDataTC;
import org.parancoe.persistence.po.hibernate.VersionedEntityTC;
import org.springframework.transaction.annotation.Transactional;

/**
 * A BO to be used for the tests of the versioned entity.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @version $Revision: c43880848171 $
 */
public class VersionedEntityTCBO {
    private VersionedEntityTCDao dao;    
    
    /** Creates a new instance of VersionedEntityTCBO */
    public VersionedEntityTCBO() {
    }

    public VersionedEntityTCDao getDao() {
        return dao;
    }

    public void setDao(VersionedEntityTCDao dao) {
        this.dao = dao;
    }
    
    @Transactional()
    public Long createEntity(VersionedEntityTC entity) {
        return (Long)dao.create(entity);
    }
    
    @Transactional(readOnly=true)
    public VersionedEntityTC retrieveEntity(Long id) {
        VersionedEntityTC retrievedEntity = this.dao.read(id);
        retrievedEntity.getVersionedData().size(); // for initializing lazy collection
        return retrievedEntity;
    }
    
    @Transactional()
    public VersionedEntityTC updateVersionedData(Long id, VersionedEntityDataTC versionedData) {
        VersionedEntityTC retrievedEntity = this.dao.read(id);
        retrievedEntity.updateVersionedData(versionedData);
        this.dao.update(retrievedEntity);
        return retrievedEntity;
    }
    
}
