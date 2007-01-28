// Copyright 2006 The Parancoe Team
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

import java.util.HashMap;
import java.util.Map;
import org.parancoe.persistence.dao.generic.Dao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * Utils for the DAO tools.
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision: 294faa0af621 $
 */
public class DaoUtils {
    
    /** Creates a new instance of DaoUtils */
    private DaoUtils() {
    }
    
    /**
     * Return a map  of DAOs from a bean container.
     *
     * @param beanFactory The bean container.
     * @return A map of daos. The key is the id of the bean in the container.
     */
    public static Map<String, Object> getDaos(ListableBeanFactory beanFactory) {
        Map<String, Object> result = new HashMap<String, Object>();
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (int i=0; i < beanNames.length; i++) {
            try {
                Object bean = beanFactory.getBean(beanNames[i]);
                if (DaoUtils.isDao(bean)) {
                    result.put(beanNames[i], bean);
                }
            } catch (BeanIsAbstractException ex) {
                // ignore it
            }
        }
        return result;
    }

    /**
     * Check if an object is a DAO.
     *
     * @return true if the object is recognized as a DAO.
     */
    public static boolean isDao(Object o) {
        Class[] objInterfaces = o.getClass().getInterfaces();
        for (int i = 0; i < objInterfaces.length; i++) {
            if (objInterfaces[i].getAnnotation(Dao.class) != null) {
                return true;
            }            
        }
        return false;
    }

    public static boolean isDaoFor(Object o, Class daoEntityType) {
        Class[] objInterfaces = o.getClass().getInterfaces();
        for (int i = 0; i < objInterfaces.length; i++) {
            Dao daoAnnotation = (Dao) objInterfaces[i].getAnnotation(Dao.class);
            if (daoAnnotation != null) {
                if (daoAnnotation.entity().getName().equals(daoEntityType.getName())) {
                    return true;
                }
            }            
        }
        return false;
    }
}
