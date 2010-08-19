// -------------------------------------------------------------------------
// Copyright (c) 2000-2004 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:WenQiang Wu
//
// -------------------------------------------------------------------------
// UFINITY MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
// THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
// TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. UFINITY SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
// MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
//
// THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
// CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
// PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
// NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
// SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
// SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
// PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). UFINITY
// SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
// HIGH RISK ACTIVITIES.
// -------------------------------------------------------------------------
package com.ufinity.marchant.ubank.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.GenericDao;

/**
 * 
 * @author WenQiang Wu
 * @version Aug 19, 2010
 * @param <T>
 *            your inject object's type
 * @param <PK>
 *            prime key
 */
public abstract class GenericDaoSupport<T, PK extends Serializable> implements
        GenericDao<T, PK> {
    // get logger method
    protected Logger log = LoggerFactory.getLogger(GenericDaoSupport.class);

    protected EntityManager entityManager = EntityManagerUtil
            .getEntityManager();

    // save parameter class type
    private Class<T> type;

    /**
     * get super class's type
     */
    @SuppressWarnings("unchecked")
    public GenericDaoSupport() {
        this.type = (Class<T>) ((ParameterizedType) (this.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0];
    }

    public Class<T> getType() {
        return this.type;
    }

    /**
     * The <code>add(T entity)</code> method is add object to database.
     * 
     * @param entity
     *            if you want to add entity.
     * 
     */
    public void add(T entity) {
        String method = "add";

        // check this parameter is invalidate or not,if invalidate
        if (null == entity) {
            log.error(method, "", "Parameter value is invalidate!");
            return;
        }
        // this.getHibernateTemplate().save(entity);
        entityManager.persist(entity);
        log.debug(method, "", "Execute persist method is successful!");

    }

    /**
     * The <code>delete(T entity)</code> method is delete object to database.
     * 
     * @param entity
     *            if you want to delete entity.
     * 
     */
    public void delete(T entity) {
        String method = "delete";

        // check this parameter is invalidate or not,if invalidate
        if (null == entity) {
            log.error(method, "", "Parameter value is invalidate!");
            return;
        }
        entityManager.remove(entity);
        log.debug(method, "", "Execute remove method is successful!");

    }

    /**
     * The <code>delete(PK id)</code> method is delete object by id to
     * database.
     * 
     * @param id
     *            if you want to delete object's condition.
     * 
     */
    public void deleteById(PK id) {
        String method = "deleteById";

        // check this parameter is invalidate or not,if invalidate
        if (null == id) {
            log.error(method, "", "Parameter value is invalidate!");
            return;
        }
        entityManager.remove(entityManager.getReference(this.type, id));
        log.debug(method, "", "Execute deleteById method is successful!");

    }

    /**
     * The <code>find(PK id)</code> method is find object according primary
     * key.
     * 
     * @param id
     *            if you want to find object's primary key
     * @return T insject object @ when accessing and manipulating database
     *         happen exception.
     */
    public T find(PK id) {
        String method = "find";

        // check this parameter is invalidate or not,if invalidate
        if (null == id) {
            log.error(method, "", "Parameter value is invalidate!");
            return null;
        }
        T entity = (T) entityManager.find(this.type, id);
        log.debug(method, "", "Execute find method is successful!");

        return entity;
    }

    /**
     * The <code>getRecordCount()</code> method is used for getting the total
     * count of records.
     * 
     * @return PK return total of record counts
     */
    @SuppressWarnings("unchecked")
    public PK getRecordCount() {
        String method = "getRecordCount";

        String jpal = "SELECT COUNT(*) FROM " + this.type.getName();
        Long count = null;

        count = ((Long) entityManager.createQuery(jpal).getSingleResult());
        log.debug(method, "", "Execute getRecordCount method is successful!");

        return (PK) count;
    }

    /**
     * The <code>modify(T entity)</code> method is update object to database.
     * 
     * @param entity
     *            if you want to update entity.
     */
    public void modify(T entity) {
        String method = "modifty";

        // check this parameter is invalidate or not,if invalidate
        if (null == entity) {
            log.error(method, "", "Parameter value is invalidate!");
            return;
        }
        entityManager.merge(entity);
        log.debug(method, "", "Execute entity method is successful!");

    }

    /**
     * The <code>queryList(PK startRecord, PK pageSize)</code> method is query
     * objects according startRecord and pagesize're number, object type is
     * according your implements this method's define type, and implements this
     * interface abstract class must be override all method and inject entity
     * type.
     * 
     * @param startRecord
     *            Where from the beginning to show this record
     * @param pageSize
     *            The number of records per page
     * @return List<T> T is your inject object's type, List is query all object
     *         connection
     * 
     */
    @SuppressWarnings("unchecked")
    public List<T> queryList(PK startRecord, PK pageSize) {
        String method = "queryList";

        String jpal = "SELECT A FROM " + this.getType().getName() + " AS A";
        List<T> lists = null;
        if (null == startRecord || null == pageSize) {
            log.error(method, "", "Parameter value is invalidate!");
            return null;
        }

        lists = (List<T>) entityManager.createQuery(jpal).setFirstResult(
                ((Long) startRecord).intValue()).setMaxResults(
                ((Long) pageSize).intValue()).getResultList();
        log.debug(method, "", "Execute queryList method is successful!");
        return lists;
    }

}
