// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
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
package com.ufinity.marchant.ubank.common;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
public class EntityManagerUtil {

    private static final EntityManagerFactory EM_FACTOTY;
    private static final ThreadLocal<EntityManager> THREAD_LOCAL;
    private static final String JPA_PERSISTENCE_NAME = "ubank";

    static {
        EM_FACTOTY = Persistence
                .createEntityManagerFactory(JPA_PERSISTENCE_NAME);
        THREAD_LOCAL = new ThreadLocal<EntityManager>();
    }

    /**
     * this method is get entity manager
     * 
     * @return EntityManager EntityManager Ojbect
     * @author skyqiang
     */
    public static EntityManager getEntityManager() {
        EntityManager entityManager = THREAD_LOCAL.get();
        if (null == entityManager || !entityManager.isOpen()) {
            entityManager = EM_FACTOTY.createEntityManager();
            THREAD_LOCAL.set(entityManager);
        }
        return entityManager;
    }

    /**
     * 
     * this method is close entity manager
     * 
     * @author skyqiang
     */
    public static void closeEntityManager() {
        EntityManager entityManager = THREAD_LOCAL.get();
        THREAD_LOCAL.set(null);
        if (null != entityManager) {
            entityManager.close();
        }
    }

    /**
     * 
     * this method is close entity manager factory
     * 
     * @author skyqiang
     */
    public static void closeFactory() {
        if (null != EM_FACTOTY && EM_FACTOTY.isOpen()) {
            EM_FACTOTY.close();
        }
    }

    /**
     * 
     * this method is begin transaction
     * 
     * @author skyqiang
     */
    public static void begin() {
        getEntityManager().getTransaction().begin();
    }

    /**
     * 
     * this method is commit transaction
     * 
     * @author skyqiang
     */
    public static void commit() {
        getEntityManager().getTransaction().commit();
    }

    /**
     * this method is get transacton active or not.
     * 
     * @return boolean true is transacton is active, else is not.
     * @author skyqiang
     */
    public static boolean isActive() {
        if (getEntityManager().getTransaction().isActive()) {
            return true;
        }
        return false;
    }

    /**
     * 
     * this method is transaction roll back
     * 
     * @author skyqiang
     */
    public static void rollback() {
        getEntityManager().getTransaction().rollback();
    }
}
