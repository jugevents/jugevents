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

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of the generic DAO.
 *
 * Derived from http://www-128.ibm.com/developerworks/java/library/j-genericdao.html
 *
 * @author <a href="mailto:lucio.benfante@jugpadova.it">Lucio Benfante</a>
 * @version $Revision: bba072a12d06 $
 */
public class HibernateGenericDao <T, PK extends Serializable>
        extends HibernateDaoSupport
        implements GenericDao<T, PK> {
    private Class type;
            
    @SuppressWarnings("unchecked")
    public PK create(T o) {
        return (PK) getHibernateTemplate().save(o);
    }
    @SuppressWarnings("unchecked")
    public void createOrUpdate(T o) {
        getHibernateTemplate().saveOrUpdate(o);
    }
    
    @SuppressWarnings("unchecked")
    public T read(PK id) {
        return (T) getHibernateTemplate().get(getType(), id);
    }
    
    public void update(T o) {
        getHibernateTemplate().update(o);
    }
    
    public void delete(T o) {
        getHibernateTemplate().delete(o);
    }

    public List<T> findAll() {
        return getHibernateTemplate().find("from "+getType().getName()+" x");
    }

    public List<T> searchByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getType());
        for (Criterion c: criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    public List<T> searchByCriteria(DetachedCriteria criteria) {
        return getHibernateTemplate().findByCriteria(criteria);
    }

    public List<T> searchByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) {
        return getHibernateTemplate().
                findByCriteria(criteria, firstResult, maxResults);
    }    
    
    public int deleteAll() {
        return getHibernateTemplate().bulkUpdate("delete from "+getType().getName()+" x");
    }
    
    public long count() {
        // TODO IMPLEMENTARE IL METODO COUNT
        throw new RuntimeException("Implementare il metodo di contaggio");
    }
    
    public Class getType() {
        return type;
    }
    
    public void setType(Class type) {
        this.type = type;
    }
}
