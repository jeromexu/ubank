// -------------------------------------------------------------------------
// Copyright (c) 2000-2009 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
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
package com.ufinity.marchant.ubank.common.preferences;

import com.ufinity.marchant.ubank.common.Logger;


/**
 * ObjectRetrieve
 * 
 * <p>
 *  ObjectRetrieve used to create a object instance from SystemGlobals.properties for loose coupling. 
 * </p>
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public final class ObjectRetrieve {
    private static final Logger LOG = Logger.getInstance(ObjectRetrieve.class);

    private ObjectRetrieve(){}

    /**
     * Retrieve object instance by its class 
     *
     * @param <T> object instance
     * @param objectClz object class obj
     * @param objectKey object key declared in the SystemGlobal.properties file
     * @return object instance
     * @author zdxue
     */
    public static <T> T retrieve(Class<T> objectClz, String objectKey) {
        try {
            Object obj = Class.forName(SystemGlobals.getString(objectKey)).newInstance();
            return objectClz.cast(obj);
        } catch (Exception e) {
            LOG.error("retrieve object exception. objectClz=" + objectClz + " , objectKey=" + objectKey, e);
            throw new RuntimeException("retrieve object exception. objectClz=" + objectClz + " , objectKey=" + objectKey);
        }
    }

}

