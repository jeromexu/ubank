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
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;

import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.dao.GenericDao;
import com.ufinity.marchant.ubank.vo.Pager;

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
    protected final Logger logger = Logger.getInstance(GenericDaoSupport.class);

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
        logger.debug("Method: Add, Param:{Entity: " + entity + "}");
        // check this parameter is invalidate or not,if invalidate
        if (null == entity) {
            logger.error("Parameter value is invalidate!");
            return;
        }
        EntityManagerUtil.getEntityManager().persist(entity);
        logger.debug("Execute persist method is successful!");
    }

    /**
     * The <code>delete(T entity)</code> method is delete object to database.
     * 
     * @param entity
     *            if you want to delete entity.
     * 
     */
    public void delete(T entity) {
        logger.debug("Method: delete, Param:{Entity: " + entity + "}");
        if (null == entity) {
            logger.error("Parameter value is invalidate!");
            return;
        }
        EntityManagerUtil.getEntityManager().remove(entity);
        logger.debug("Execute remove method is successful!");

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
        logger.debug("Method: deleteById, Param:{PK: " + id + "}");
        if (null == id) {
            logger.error("Parameter value is invalidate!");
            return;
        }
        EntityManagerUtil.getEntityManager().remove(
                EntityManagerUtil.getEntityManager()
                        .getReference(this.type, id));
        logger.debug("Execute deleteById method is successful!");
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
        logger.debug("Method: find, Param:{PK: " + id + "}");
        if (null == id) {
            logger.error("Parameter value is invalidate!");
            return null;
        }
        T entity = (T) EntityManagerUtil.getEntityManager().find(this.type, id);
        logger.debug("Execute find method is successful!");
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
        String jpal = "SELECT COUNT(*) FROM " + this.type.getName();
        Long count = null;

        count = ((Long) EntityManagerUtil.getEntityManager().createQuery(jpal)
                .getSingleResult());
        logger.debug("Execute getRecordCount method is successful!");

        return (PK) count;
    }

    /**
     * The <code>modify(T entity)</code> method is update object to database.
     * 
     * @param entity
     *            if you want to update entity.
     */
    public void modify(T entity) {
        logger.debug("Method: modify, Param:{Entity: " + entity + "}");
        // check this parameter is invalidate or not,if invalidate
        if (null == entity) {
            logger.error("Parameter value is invalidate!");
            return;
        }
        EntityManagerUtil.getEntityManager().merge(entity);
        logger.debug("Execute entity method is successful!");

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
        logger.debug("Method: queryList, Param:{startRecord: " + startRecord
                + " , pageSize: " + pageSize + "}");
        String jpal = "SELECT A FROM " + this.getType().getName() + " AS A";
        List<T> lists = null;
        if (null == startRecord || null == pageSize) {
            logger.error("Parameter value is invalidate!");
            return null;
        }

        lists = (List<T>) EntityManagerUtil.getEntityManager()
                .createQuery(jpal).setFirstResult(
                        ((Long) startRecord).intValue()).setMaxResults(
                        ((Long) pageSize).intValue()).getResultList();
        logger.debug("Execute queryList method is successful!");
        return lists;
    }

    /**
     * find page object's connection with hql and param map
     * 
     * @param hql
     *            according hql if class param is null
     * @param currentPage
     *            current page
     * @param pageSize
     *            the number of records per page
     * @param properties
     *            according param map
     * @return Object's connection
     */
    public Pager<T> findPager(String hql, int currentPage, int pageSize,
            Map<String, Object> properties) {
        return findPager(hql, currentPage, pageSize, null, properties);
    }

    /**
     * find page object's connection with hql class and param map
     * 
     * @param hql
     *            according hql
     * @param currentPage
     *            current page
     * @param pageSize
     *            the number of records per page
     * @param clazz
     *            according class
     * @param properties
     *            according param map
     * @return Object's connection
     */
    @SuppressWarnings("unused")
    private Pager<T> findPager(String hql, int currentPage, int pageSize,
            Class<T> clazz, Map<String, Object> properties) {
        logger.debug("Method: findPager, Param:{currentPage : " + currentPage
                + " ,pageSize : " + pageSize + " ,properties:" + properties
                + "}");
        if (currentPage <= 0) {
            logger.error("current page's value isn't less than zero!");
            return null;
        }
        int totalRecords = 0;
        if (clazz != null) {
            totalRecords = (int) this.getRecordCount(clazz);
            logger.debug("Param:{clazz : " + clazz + "}");
        } else {
            totalRecords = this.getRecordCount(hql, properties);
            logger.debug("Param:{clazz : " + clazz + "}");
        }
        Pager<T> page = new Pager<T>();
        List<T> list = null;
        page.setTotalRecords(totalRecords);
        page.setPageSize(pageSize);
        page.setCurrentPage(currentPage);

        list = this.queryList(hql, (currentPage - 1) * pageSize, pageSize,
                clazz, properties);

        page.setPageRecords(list);
        return page;
    }

    /**
     * find object's connection with hql class and param map
     * 
     * @param hql
     *            according hql if class param is null
     * @param startRecord
     *            Where from the beginning to show this record
     * @param pageSize
     *            the number of records per page
     * @param clazz
     *            according class
     * @param properties
     *            according param map
     * @return List<T> object connection
     */
    @SuppressWarnings("unchecked")
    public List<T> queryList(String hql, int startRecord, int pageSize,
            Class<T> clazz, Map<String, Object> properties) {
        logger.debug("Method: queryList, Param:{startRecord : " + startRecord
                + " ,pageSize : " + pageSize + " ,properties:" + properties
                + "}");
        if (hql == null && clazz == null) {
            logger.error("Parameter is invalidate!Param Value{hql :" + hql
                    + " ,clazz: " + clazz + "}");
            return null;
        }
        if (clazz != null) {
            hql = "FROM " + clazz.getName();
        }
        String queryHql = hql;
        Query query = ((Session) EntityManagerUtil.getEntityManager()
                .getDelegate()).createQuery(queryHql);

        if (!Validity.isEmpty(properties)) {
            query.setProperties(properties);
        }
        if (startRecord >= 0 && pageSize >= 0) {
            query.setFirstResult(startRecord).setMaxResults(pageSize);
        }
        return query.list();

    }

    /**
     * The <code>getRecordCount(Class<T> clazz)</code> method is used for
     * getting the total count of records.
     * 
     * @return PK return total of record counts
     */
    public int getRecordCount(Class<T> clazz) {
        String jpaQuery = "SELECT COUNT(*) FROM " + clazz.getName();
        Integer count = null;

        count = ((Integer) EntityManagerUtil.getEntityManager().createQuery(
                jpaQuery).getSingleResult());
        logger.debug("Execute getRecordCount method is successful!");
        return count;
    }

    /**
     * get count with select hql and param map
     * 
     * @param selectHql
     *            according select hql
     * @param properties
     *            according param map
     * @return count of hql
     */
    public int getRecordCount(String selectHql, Map<String, Object> properties) {
        String countHql = getCountHql(selectHql);
        return ((Long) getUniqueBeanResult(countHql, properties)).intValue();
    }

    /**
     * get count hql with select hql
     * 
     * @param hql
     *            select hql
     * @return count hql
     */
    protected String getCountHql(String hql) {
        if (Validity.isEmpty(hql)) {
            logger.error("Error getHqlBean because hql is empty");
            return "";
        }
        return "SELECT COUNT(*) "
                + hql
                        .substring(hql.indexOf("FROM"))
                        .replace("FETCH", "")
                        .replaceAll(
                                "\\s((?mi)(LEFT|RIGHT|INNER)?\\s+JOIN)\\s+[^\\s]*SET\\b",
                                " ").split("ORDER BY")[0];
    }

    /**
     * find object with hql and param map
     * 
     * @param hql
     *            according hql
     * @param properties
     *            according param map
     * @return Object which find
     */
    public Object getUniqueBeanResult(String hql, Map<String, Object> properties) {
        logger.debug("Method: getUniqueBeanResult, Param:{hql : " + hql + "}");

        Query query = ((Session) EntityManagerUtil.getEntityManager()
                .getDelegate()).createQuery(hql);

        if (!Validity.isEmpty(properties)) {
            query.setProperties(properties);
        }
        Object object = query.uniqueResult();
        return object;
    }

    /**
     * this method is query entity by criteria with pager
     * 
     * @param currentPage
     *            current page number
     * @param pageSize
     *            page size number
     * @param criterion
     *            hibernate condition
     * @return Pager<T> pager object
     * @author skyqiang
     */
    @SuppressWarnings("unchecked")
    public Pager<T> queryEntitiesByCriteriaWithPager(int currentPage,
            int pageSize, Criterion... criterion) {
        logger
                .debug("Method: queryEntitiesByCriteriaWithPager, Param:{currentPage : "
                        + currentPage + " ,pageSize : " + pageSize + "}");

        List<T> list = null;
        Criteria criteria = ((Session) EntityManagerUtil.getEntityManager()
                .getDelegate()).createCriteria(this.type);

        // add query's condition
        if (null != criterion) {
            for (Criterion c : criterion) {
                criteria.add(c);
            }
        }

        // query total records according search's condition
        int totalRecords = (Integer) criteria.setProjection(
                Projections.rowCount()).uniqueResult();

        // query result
        list = criteria.setProjection(null).setFirstResult(
                (currentPage - 1) * pageSize).setMaxResults(pageSize).list();

        // encapsulation Pager object
        Pager<T> page = new Pager<T>();
        page.setTotalRecords(totalRecords);
        page.setPageSize(pageSize);
        page.setCurrentPage(currentPage);
        page.setPageRecords(list);

        return page;
    }
}
