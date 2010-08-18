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

    private static final EntityManagerFactory emFactoty;
    private static final ThreadLocal<EntityManager> threadLocal;
    private static final String JPA_PERSISTENCE_NAME = "ubank";

    static {
        emFactoty = Persistence
                .createEntityManagerFactory(JPA_PERSISTENCE_NAME);
        threadLocal = new ThreadLocal<EntityManager>();
    }
    
    /**
     * 
     * @return
     * @author skyqiang
     */
    public static EntityManager getEntityManager() {
        EntityManager entityManager = threadLocal.get();
        if (null != entityManager || !entityManager.isOpen()) {
            entityManager = emFactoty.createEntityManager();
            threadLocal.set(entityManager);
        }
        return entityManager;
    }
    
    /**
     * 
     * 
     * @author skyqiang
     */
    public static void closeEntityManager() {
        EntityManager entityManager = threadLocal.get();
        threadLocal.set(null);
        if (null != entityManager) {
            entityManager.close();
        }
    }
    
    /**
     * 
     * 
     * @author skyqiang
     */
    public static void closeFactory() {
        if (null != emFactoty && emFactoty.isOpen()) {
            emFactoty.close();
        }
    }
    
    /**
     * 
     * 
     * @author skyqiang
     */
    public static void begin() {
        getEntityManager().getTransaction().begin();
    }
    
    /**
     * 
     * 
     * @author skyqiang
     */
    public static void commit() {
        getEntityManager().getTransaction().commit();
    }
    
    /**
     * 
     * 
     * @author skyqiang
     */
    public static void rollback() {
        getEntityManager().getTransaction().rollback();
    }
}
