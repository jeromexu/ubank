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
package com.ufinity.marchant.ubank.service;

import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;

/**
 * Service Factory
 * 
 * <p>
 *  ServiceFactory used to get the implements of services-interfaces from SystemGlobals.properties for loose coupling. 
 * </p>
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public final class ServiceFactory {

    private static final ServiceFactory SF = new ServiceFactory();

    private ServiceFactory(){}

    /**
     * Get ServiceFactory instance 
     *
     * @return ServiceFactory obj
     * @author zdxue
     */
    public static ServiceFactory getInstance() {
        return SF;
    } 

    /**
     * Get UserService
     *  
     * @return UserService impl
     * @author zdxue
     */
    public UserService getUserService() {
        try{
            return (UserService)Class.forName(SystemGlobals.getString(ConfigKeys.SERVICE_USER)).newInstance();
        }catch(Exception e) {
            throw new RuntimeException("get UserService implements exception.");
        }
    }
    
    /**
     * Get FileService
     *  
     * @return FileService impl
     * @author zdxue
     */
    public FileService getFileService() {
        try{
            return (FileService)Class.forName(SystemGlobals.getString(ConfigKeys.SERVICE_FILE)).newInstance();
        }catch(Exception e) {
            throw new RuntimeException("get FileService implements exception.");
        }
    }

}

