// -------------------------------------------------------------------------
// Copyright (c) 2000-2009 Ufinity. All Rights Reserved.
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

import java.util.List;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.dao.FolderDao;

/**
 * 
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
public class FolderDaoImpl extends GenericDaoSupport<Folder, Long> implements
        FolderDao {

    /**
     * this method is find folder collection according user id
     * 
     * @param userId
     *            user's id
     * @return List<Folder> folder's collection
     * @author skyqiang
     */
    @SuppressWarnings("unchecked")
    public List<Folder> findFolderListByUserId(Long userId) {

        String sqlQuery = "SELECT DISTINCT a.FOLDER_ID,a.CREATE_TIME,a.DIRECTORY,a.FOLDER_NAME,"
                + "a.FOLDER_TYPE,a.MODIFIED_TIME,a.SHARE,a.USER_ID,a.PARENT_ID FROM U_FOLDER a "
                + "LEFT JOIN U_FOLDER b on a.PARENT_ID = b.PARENT_ID WHERE a.USER_ID = :userId"
                + " ORDER BY a.PARENT_ID , a.CREATE_TIME desc";

        return entityManager.createNativeQuery(sqlQuery, Folder.class)
                .setParameter("userId", userId).getResultList();
    }
}
