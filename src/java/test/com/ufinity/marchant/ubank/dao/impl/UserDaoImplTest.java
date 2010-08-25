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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.UserDao;

/**
 * 
 * @author WenQiang Wu
 * @version Aug 19, 2010
 */
public class UserDaoImplTest {
    private UserDao userDao = null;

    // user's test data
    private Object[][] userDatas = null;
    private List<User> users = null;

    /**
     * this method is init right data for user object, user object attribute
     * name's : userName,password eg. userName,password,date attribute values
     * aren't null.
     */
    private void initRightData() {

        // user rigth datas
        userDatas = new Object[][] {
                { "skyqiang", "123456789", new Date(), 2 },
                { "mary2008", "613164112", new Date(), 4 },
                { "statar120", "4654163497", new Date(), 5 },
                { "jack2005", "51349463", new Date(), 9 },
                { "simle1992", "49497979qqq", new Date(), 5 } };
    }

    /**
     * this method is init eror data for user object to test.use those data to
     * test, happend operation error and throws hibernate dao spport exception.
     */
    private void initErrorData() {
        // user error datas
        userDatas = new Object[][] { { null, "123456789", new Date(), 2 },
                { "mary2008", null, new Date(), 4 },
                { "statar120", "4654163497", null, 5 },
                { "jacky2005", "743794649", new Date(), 1 },
                { "jacky2005", "743794649", new Date(), 5 } };
    }

    /**
     * 
     * @param type
     */
    private void packageData(boolean type) {
        if (type) {
            initRightData();
        } else {
            initErrorData();
        }

        // pageage user data
        users = new LinkedList<User>();
        for (int i = 0; i < userDatas.length; i++) {
            User user = new User();
            user.setUserName((String) userDatas[i][0]);
            user.setPassword((String) userDatas[i][1]);
            user.setCreateTime((Date) userDatas[i][2]);
            user.setOverSize((Integer) userDatas[i][3]);
            users.add(user);
        }
    }

    /**
     * 
     * @throws Exception
     *             exception
     * @author skyqiang
     */
    @Before
    public void setUp() throws Exception {
        userDao = DaoFactory.createDao(UserDao.class);
    }

    /**
     * 
     * @throws Exception
     *             exception
     * @author skyqiang
     */
    @After
    public void tearDown() throws Exception {
        userDao = null;
        userDatas = null;
        users = null;
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#add(java.lang.Object)}.
     */
    @Test
    public void testAdd() {
        // init rigth data
        packageData(true);
        // start transaction
        EntityManagerUtil.begin();
        // add user
        for (int i = 0; i < users.size(); i++) {
            this.userDao.add(users.get(i));
        }
        // commit transaction
        EntityManagerUtil.commit();
        EntityManagerUtil.closeFactory();
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#add(java.lang.Object)}.
     */
    @Test(expected = Exception.class)
    public void testAddException() {
        // init rigth data
        packageData(false);
        // start transaction
        try {
            EntityManagerUtil.begin();
            // add user
            for (int i = 0; i < users.size(); i++) {
                this.userDao.add(users.get(i));
            }
            // commit transaction
            EntityManagerUtil.commit();

        } catch (RuntimeException e) {
            EntityManagerUtil.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            EntityManagerUtil.closeFactory();
        }
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#find(java.io.Serializable)}.
     */
    @Test
    public void testFind() {
        // init rigth data
        packageData(true);
        List<User> list = this.userDao.queryList(0L, (long) userDatas.length);

        // execute find object method operation
        for (User user : list) {
            User userTemp = this.userDao.find(user.getUserId());
            System.err.println(userTemp.getUserName());
            assertNotNull(userTemp.getUserName());
        }
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.FileDaoImpl#findUser(java.lang.String,java.lang.String)}.
     */
    @Test
    public void testfindUserByNameAndPass() {
        // init rigth data
        packageData(true);
        List<User> list = this.userDao.queryList(0L, (long) userDatas.length);

        // execute find object method operation
        for (User user : list) {
            User userTemp = this.userDao.findUser(user.getUserName(), user
                    .getPassword());
            System.err.println(userTemp.getUserName());
            assertNotNull(userTemp.getUserName());
            assertNotNull(userTemp.getPassword());
        }
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.FileDaoImpl#findUserByName(java.lang.String)}.
     */
    @Test
    public void findUserByName() {
        // init rigth data
        packageData(true);
        List<User> list = this.userDao.queryList(0L, (long) userDatas.length);

        // execute find object method operation
        for (User user : list) {
            User userTemp = this.userDao.findUserByName(user.getUserName());
            System.err.println(userTemp.getUserName());
            assertNotNull(userTemp.getUserName());
            assertNotNull(userTemp.getPassword());
        }
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#modify(java.lang.Object)}.
     */
    @Test
    public void testModify() {
        packageData(true);

        EntityManagerUtil.begin();
        List<User> list = this.userDao.queryList(0L, (long) userDatas.length);

        for (User user : list) {
            User userTemp = this.userDao.find(user.getUserId());
            userTemp.setOverSize(20);
            this.userDao.modify(userTemp);
        }
        EntityManagerUtil.commit();
        EntityManagerUtil.closeEntityManager();
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#modify(java.lang.Object)}.
     */
    @Test(expected = Exception.class)
    public void testModifyException() {
        try {
            EntityManagerUtil.begin();

            User u = new User();
            u.setUserId(100L);

            this.userDao.modify(u);
            EntityManagerUtil.commit();

        } catch (RuntimeException e) {
            EntityManagerUtil.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            EntityManagerUtil.closeEntityManager();
        }
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#queryList(java.io.Serializable, java.io.Serializable)}.
     */
    @Test
    public void testQueryListPKPK() {
        packageData(true);

        List<User> userList = this.userDao.queryList(0L,
                (long) userDatas.length);
        assertTrue(userList.size() > 0);
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#queryList(java.io.Serializable, java.io.Serializable)}.
     */
    @Test(expected = Exception.class)
    public void testQueryListPKPKException() {
        try {
            List<User> userList = this.userDao.queryList(0L,
                    (long) userDatas.length);
            assertTrue(userList.size() > 0);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#delete(java.lang.Object)}.
     */
    @Test
    public void testDelete() {
        packageData(true);

        EntityManagerUtil.begin();
        List<User> userList = this.userDao.queryList(0L,
                (long) userDatas.length);

        for (User u : userList) {
            this.userDao.delete(u);
        }
        EntityManagerUtil.commit();
        EntityManagerUtil.closeEntityManager();
    }

}
