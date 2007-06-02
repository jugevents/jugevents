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
package org.parancoe.persistence.dao;

import org.parancoe.persistence.dao.DaoProvider;
import org.parancoe.persistence.dao.generic.EntityTCDao;
import org.parancoe.persistence.dao.generic.VersionedEntityTCDao;

/**
 * Interface for the DAO Provider. Doesn't require an implementation.
 * Simply add methods for the DAOs you need to use.
 * The convention for the methods is get<dao_bean_id>.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision: 8c19c37e30b9 $
 */
public interface Daos extends DaoProvider {
    public EntityTCDao getEntityTCDao();
    public VersionedEntityTCDao getVersionedEntityTCDao();
}