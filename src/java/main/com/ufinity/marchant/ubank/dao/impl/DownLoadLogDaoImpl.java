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

import com.ufinity.marchant.ubank.bean.DownLoadLog;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.DownLoadLogDao;

/**
 * 
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
public class DownLoadLogDaoImpl extends GenericDaoSupport<DownLoadLog, Long>
        implements DownLoadLogDao {

    /**
     * this method is find download log by user id and file id.
     * 
     * @param userId
     *            user's id
     * @param fileId
     *            file's id
     * @return DownLoadLog
     * @author skyqiang
     */
    @SuppressWarnings("unchecked")
    public DownLoadLog findDownLoadLog(Long userId, Long fileId) {
        logger.debug("Method: findDownLoadLog, Param:{userId: " + userId
                + " , fileId" + fileId + "}");

        List<DownLoadLog> list = EntityManagerUtil.getEntityManager()
                .createNamedQuery("DownLoadLog.findDownLoadLog").setParameter(
                        "userId", userId).setParameter("fileId", fileId)
                .getResultList();
        logger
                .debug("Execute find download log according user id and file id method is success!");

        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
