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
public class JPAEntityManagerFactory {
    public static JPAEntityManagerFactory instance = null;
    private static final String JPA_PERSISTENCE_NAME = "ubank";
    
    /**
     * 
     */
    private JPAEntityManagerFactory() {

    }

    /**
     * this method is get JPAEntityManagerFactory
     * 
     * @return JPAEntityManagerFactory manager factory
     * @author skyqiang
     */
    public static JPAEntityManagerFactory getInstance() {
        if (null == instance) {
            instance = new JPAEntityManagerFactory();
        }
        return instance;
    }

    /**
     * this method is get entity manager
     * 
     * @return EntityManager entity manager
     * @author skyqiang
     */
    public EntityManager getEntityManager() {
        EntityManagerFactory emFactory = Persistence
                .createEntityManagerFactory(JPA_PERSISTENCE_NAME);
        return emFactory.createEntityManager();
    }
}
