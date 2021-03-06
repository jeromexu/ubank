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
package com.ufinity.marchant.ubank.dao;

import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.ObjectRetrieve;

/**
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
public class DaoFactory {

    /**
     * Private constructor
     */
    private DaoFactory() {

    }

    /**
     * create Dao interface's impl instance
     * 
     * @param <T>
     *            Dao interface
     * @param clazz
     *            dao interface class
     * @return dao interface impl instance
     */
    public static <T> T createDao(Class<T> clazz) {
        String daoKey = "";

        if (UserDao.class.equals(clazz)) {
            daoKey = ConfigKeys.DAO_USER;
        } else if (FileDao.class.equals(clazz)) {
            daoKey = ConfigKeys.DAO_FILE;
        } else if (FolderDao.class.equals(clazz)) {
            daoKey = ConfigKeys.DAO_FOLDER;
        } else if(DownLoadLogDao.class.equals(clazz)) {
            daoKey = ConfigKeys.DAO_DOWNLOAD_LOG;
        }
        return ObjectRetrieve.retrieve(clazz, daoKey);
    }
}
