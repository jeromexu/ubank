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

import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.UserDao;

/**
 * 
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
public class UserDaoImpl extends GenericDaoSupport<User, Long> implements
        UserDao {

    /**
     * this method is find user according user name and password
     * 
     * @param userName
     *            user name
     * @param password
     *            user's password
     * @return User Object
     * @author skyqiang
     */
    @SuppressWarnings("unchecked")
    public User findUser(String userName, String password) {
        logger.debug("Method: findUser, Param:{userName: " + userName
                + " , password: " + password + "}");

        List<User> list = EntityManagerUtil.getEntityManager()
                .createNamedQuery("User.findUserByNameAndPass").setParameter(
                        "name", userName).setParameter("pass", password)
                .getResultList();
        logger
                .debug("Execute find user according user name and password method is success!");

        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * this method is find user according name
     * 
     * @param userName
     *            user name
     * @return User object
     * @author skyqiang
     */
    @SuppressWarnings("unchecked")
    public User findUserByName(String userName) {
        logger.debug("Method: findUserByName, Param:{userName: " + userName
                + "}");

        List<User> list = EntityManagerUtil.getEntityManager()
                .createNamedQuery("User.findUserByName").setParameter("name",
                        userName).getResultList();
        logger
                .debug("Execute find user according user name method is success!");

        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * this method is modify user's point according user's id
     * 
     * @param userId
     *            user's id
     * @param point
     *            user's point
     * @author skyqiang
     */
    public void modifyPointByUserId(Long userId, Integer point) {
        logger.debug("Method: modifyPointByUserId, Param:{userId: " + userId
                + " , point" + point + "}");

        EntityManagerUtil.getEntityManager().createNamedQuery(
                "User.modifyPointByUserId").setParameter("point", point)
                .setParameter("userId", userId).executeUpdate();
        logger
                .debug("Execute find modify point according user id method is success!");
    }
}
