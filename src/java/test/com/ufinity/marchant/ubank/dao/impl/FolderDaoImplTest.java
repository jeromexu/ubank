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
package com.ufinity.marchant.ubank.dao.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;

/**
 * @author WenQiang Wu
 * @version Aug 25, 2010
 */
public class FolderDaoImplTest {
    private FolderDao folderDao = null;
    private UserDao userDao = null;

    private Object[][] folderDatas = null;
    private List<Folder> folders = null;
    private User user = null;

    /**
     * this method is init right test data
     * 
     * @author skyqiang
     */
    private void initRightData() {
        // init user object
        user = new User();
        user.setUserName("testFolderName");
        user.setPassword("testFolderPass");
        user.setCreateTime(new Date());
        user.setOverSize(1);

        // folder rigth datas
        folderDatas = new Object[][] {
                { "folder1", new Date(), new Date(), "E:", true, "I", user },
                { "folder2", new Date(), new Date(), "F:", false, "I", user },
                { "folder3", new Date(), new Date(), "D:", false, "I", user },
                { "folder4", new Date(), new Date(), "C:", true, "I", user },
                { "folder5", new Date(), new Date(), "E:", true, "I", user } };

    }

    /**
     * this method is init error test data,execute this data will happend
     * exception
     * 
     * @author skyqiang
     */
    private void initErrorData() {
        // init user object
        user = new User();
        user.setUserName("testFolderName");
        user.setPassword("testFolderPass");
        user.setCreateTime(new Date());
        user.setOverSize(1);

        // folder error datas
        folderDatas = new Object[][] {
                { "folder1", null, new Date(), null, true, "I", user },
                { "folder2", new Date(), new Date(), "F:", false, "I", null },
                { "folder3", null, new Date(), "D:", false, "I", user },
                { "folder4", new Date(), new Date(), null, null, "I", null },
                { "folder5", new Date(), new Date(), "E:", null, null, null } };

    }

    /**
     * package data
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
        folders = new LinkedList<Folder>();
        for (int i = 0; i < folderDatas.length; i++) {
            Folder folder = new Folder();

            folder.setFolderName((String) folderDatas[i][0]);
            folder.setCreateTime((Date) folderDatas[i][1]);
            folder.setModifyTime((Date) folderDatas[i][2]);
            folder.setDirectory((String) folderDatas[i][3]);
            folder.setShare((Boolean) folderDatas[i][4]);
            folder.setFolderType((String) folderDatas[i][5]);

            folder.setUser(user);
            folders.add(folder);
        }
    }

    /**
     * @throws Exception
     *             java.lang.Exception
     * @author skyqiang
     */
    @Before
    public void setUp() throws Exception {
        userDao = DaoFactory.createDao(UserDao.class);
        folderDao = DaoFactory.createDao(FolderDao.class);
    }

    /**
     * @throws Exception
     *             java.lang.Exception
     * @author skyqiang
     */
    @After
    public void tearDown() throws Exception {
        folderDao = null;
        folderDatas = null;
        folders = null;
        userDao = null;
        user = null;
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
        this.userDao.add(user);

        for (int i = 0; i < folders.size(); i++) {
            this.folderDao.add(folders.get(i));
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
        try {
            // start transaction
            EntityManagerUtil.begin();

            for (int i = 0; i < folders.size(); i++) {
                this.folderDao.add(folders.get(i));
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
        List<Folder> list = this.folderDao.queryList(0L,
                (long) folderDatas.length);

        // execute find object method operation
        for (Folder folder : list) {
            Folder folderTemp = this.folderDao.find(folder.getFolderId());
            System.err.println(folderTemp.getFolderName());
            assertNotNull(folderTemp.getFolderName());
        }
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#queryList(java.io.Serializable, java.io.Serializable)}.
     */
    @Test
    public void testQueryListPKPK() {
        packageData(true);

        List<Folder> folderList = this.folderDao.queryList(0L,
                (long) folderDatas.length);
        assertTrue(folderList.size() > 0);
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#queryList(java.io.Serializable, java.io.Serializable)}.
     */
    @Test(expected = Exception.class)
    public void testQueryListPKPKException() {
        try {
            List<Folder> folderList = this.folderDao.queryList(0L,
                    (long) folderDatas.length);
            assertTrue(folderList.size() > 0);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.FolderDaoImpl#findFolderListByUserId(java.lang.Long)}.
     */
    @Test
    public void testFindFolderListByUserId() {
        packageData(true);

        User userTemp = this.userDao.findUserByName(user.getUserName());
        List<Folder> folderList = this.folderDao
                .findFolderListByUserId(userTemp.getUserId());
        assertTrue(folderList.size() > 0);
        assertTrue(folderList.size() == folderDatas.length);
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.FolderDaoImpl#findFolderListByUserId(java.lang.Long)}.
     */
    @Test(expected = Exception.class)
    public void testFindFolderListByUserIdException() {
        packageData(true);

        User userTemp = this.userDao.findUserByName("Folder_Error_Modify");
        List<Folder> folderList = this.folderDao
                .findFolderListByUserId(userTemp.getUserId());
        assertTrue(folderList.size() > 0);
        assertTrue(folderList.size() == folderDatas.length);
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#modify(java.lang.Object)}.
     */
    @Test
    public void testModify() {
        packageData(true);

        EntityManagerUtil.begin();
        List<Folder> folderList = this.folderDao.queryList(0L,
                (long) folderDatas.length);

        for (Folder folder : folderList) {
            Folder folderTemp = this.folderDao.find(folder.getFolderId());
            folderTemp.setDirectory("F:");
            this.folderDao.modify(folderTemp);
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

            Folder f = new Folder();
            f.setFolderId(1000L);

            this.folderDao.modify(f);
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
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#delete(java.lang.Object)}.
     */
    @Test
    public void testDelete() {
        packageData(true);

        EntityManagerUtil.begin();
        List<Folder> folderList = this.folderDao.queryList(0L,
                (long) folderDatas.length);

        for (Folder f : folderList) {
            this.folderDao.delete(f);
        }
    
        User findUser = this.userDao.findUserByName(user.getUserName());
        if (null != findUser) {
            this.userDao.delete(findUser);
        }

        EntityManagerUtil.commit();
        EntityManagerUtil.closeEntityManager();
    }

}
