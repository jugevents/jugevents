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

import java.util.Map;

import junit.framework.TestCase;

import org.parancoe.persistence.po.hibernate.EntityTC;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

/**
 * Tests for the DaoProvider classes.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision: e8e903776783 $
 */
public class DaoProviderTest extends TestCase {
    private BeanFactory beanFactory;
    
    public DaoProviderTest(String testName) {
        super(testName);
        BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance("beanRefFactory_test.xml");
        BeanFactoryReference bf = bfl.useBeanFactory("org.parancoe.persistence");
        this.beanFactory = bf.getFactory();
        Map daoMap = (Map) this.beanFactory.getBean("daoMap");
        Map daos = DaoUtils.getDaos((ListableBeanFactory) this.beanFactory);
        daoMap.putAll(daos);
    }
    
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test the retrieval of the DAO map.
     */
    public void testGetDaoMap() {
        Map daoMap = (Map) this.beanFactory.getBean("daoMap");
        assertNotNull(daoMap);
        assertTrue("Test DAO map shouldn't be empty", !daoMap.isEmpty());
    }

    /**
     * Test the retrieval of a DAO from the DAO map.
     */
    public void testGetDaoFromMap() {
        Map daoMap = (Map) this.beanFactory.getBean("daoMap");
        assertNotNull(daoMap);
        Object dao = daoMap.get("entityTCDao");
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

    /**
     * Test the retrieval of the DAO map from the generic dao provider.
     */
    public void testGetDaoMapFromProvider() {
        Daos baseDaoProvider = (Daos) this.beanFactory.getBean("daos");
        assertNotNull(baseDaoProvider);
        Map daoMap = baseDaoProvider.getDaoMap();
        assertNotNull(daoMap);
        assertTrue("Test DAO map shouldn't be empty", !daoMap.isEmpty());
    }

    /**
     * Test the retrieval of a DAO from the generic dao provider.
     */
    public void testGetDaoFromProvider() {
        Daos baseDaoProvider = (Daos) this.beanFactory.getBean("daos");
        assertNotNull(baseDaoProvider);
        Object dao = baseDaoProvider.getDao("entityTCDao");
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

    /**
     * Test the retrieval of a DAO for an entity from the generic dao provider.
     */
    public void testGetDaoByEntityFromProvider() {
        Daos baseDaoProvider = (Daos) this.beanFactory.getBean("daos");
        assertNotNull(baseDaoProvider);
        Object dao = baseDaoProvider.getDao(EntityTC.class);
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }

    /**
     * Test the retrieval of a DAO for an entity from the generic dao provider.
     */
    public void testGetDaoByMethodFromProvider() {
        Daos baseDaoProvider = (Daos) (Daos) this.beanFactory.getBean("daos");
        assertNotNull(baseDaoProvider);
        Object dao = baseDaoProvider.getEntityTCDao();
        assertNotNull(dao);
        assertTrue(DaoUtils.isDao(dao));
    }
    
}
