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

import javax.persistence.Query;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.FolderDao;

/**
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
public class FolderDaoImpl extends GenericDaoSupport<Folder, Long> implements
        FolderDao {

    /**
     * this method is find folder collection according user id and share, if
     * share is null, query all folder, else according share's value.
     * 
     * @param userId
     *            user's id
     * @param share
     *            folder share or not
     * @return List<Folder> folder's collection
     * @author skyqiang
     */
    @SuppressWarnings("unchecked")
    public List<Folder> findFolderListByUserId(Long userId, Boolean share) {
        logger.debug("Method: findFolderListByUserId, Param:{userId: " + userId
                + " , share:" + share + "}");
        StringBuffer sqlQuery = new StringBuffer();
        String sqlQueryStart = "SELECT DISTINCT a.FOLDER_ID,a.CREATE_TIME,a.DIRECTORY,a.FOLDER_NAME,"
                + "a.FOLDER_TYPE,a.MODIFIED_TIME,a.SHARE,a.USER_ID,a.PARENT_ID,a.REPEAT_COUNT FROM U_FOLDER a "
                + "LEFT JOIN U_FOLDER b on a.PARENT_ID = b.PARENT_ID WHERE a.USER_ID = :userId ";

        String sqlQueryEnd = "ORDER BY a.PARENT_ID , a.CREATE_TIME DESC";
        String sqlQueryAppend = " AND a.share = :share ";

        Query query = null;
        sqlQuery.append(sqlQueryStart);
        if (null == share) {
            sqlQuery.append(sqlQueryEnd);
            query = EntityManagerUtil.getEntityManager().createNativeQuery(
                    sqlQuery.toString(), Folder.class).setParameter("userId",
                    userId);
        }
        else {
            sqlQuery.append(sqlQueryAppend).append(sqlQueryEnd);
            query = EntityManagerUtil.getEntityManager().createNativeQuery(
                    sqlQuery.toString(), Folder.class).setParameter("userId",
                    userId).setParameter("share", share);
        }

        logger.debug("Method: findFolderListByUserId, SQL:{"
                + sqlQuery.toString() + "}");
        return query.getResultList();
    }

    /**
     * @param userId
     * @return Folder
     * @author skyqiang
     */
    public Folder findRootRolderByUserId(Long userId) {
        logger.debug("Method: findRootRolderByUserId, Param:{userId: " + userId
                + "}");

        return (Folder) EntityManagerUtil.getEntityManager().createNamedQuery(
                "Folder.findRootRolderByUserId").setParameter("userId", userId)
                .getSingleResult();
    }
}
