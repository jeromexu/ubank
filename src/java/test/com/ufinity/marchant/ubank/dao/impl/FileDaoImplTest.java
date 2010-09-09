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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.DateUtil;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;
import com.ufinity.marchant.ubank.vo.Pager;

/**
 * @author WenQiang Wu
 * @version Aug 26, 2010
 */
public class FileDaoImplTest {
    private FileDao fileDao = null;
    private FolderDao folderDao = null;
    private UserDao userDao = null;

    private List<FileBean> files = null;
    private Folder folder = null;
    private User user = null;

    private Object[][] fileDatas = null;
    private Map<String, Object> condition = null;

    /**
     * @throws Exception
     *             java.lang.Exception
     * @author skyqiang
     */
    @Before
    public void setUp() throws Exception {
        userDao = DaoFactory.createDao(UserDao.class);
        folderDao = DaoFactory.createDao(FolderDao.class);
        fileDao = DaoFactory.createDao(FileDao.class);
    }

    /**
     * @throws Exception
     *             java.lang.Exception
     * @author skyqiang
     */
    @After
    public void tearDown() throws Exception {
        folderDao = null;
        fileDao = null;
        files = null;
        folder = null;
        fileDatas = null;
        user = null;
        userDao = null;
    }

    /**
     * this method is init right test data
     * 
     * @author skyqiang
     */
    private void initRightData() {
        // init user object
        user = new User();
        user.setUserName("testFileName");
        user.setPassword("testFilePass");
        user.setCreateTime(new Date());
        user.setOverSize(1);

        // init folder object
        folder = new Folder();
        folder.setFolderName("testFileFolder");
        folder.setCreateTime(new Date());
        folder.setModifyTime(new Date());
        folder.setDirectory("E:");
        folder.setShare(true);
        folder.setFolderType("I");
        folder.setUser(user);

        // file rigth datas
        fileDatas = new Object[][] {
                { "file1", new Date(), new Date(), "E:", true, "I", 10, folder },
                { "file2", new Date(), new Date(), "F:", false, "I", 20, folder },
                { "file3", new Date(), new Date(), "D:", false, "I", 5, folder },
                { "file4", new Date(), new Date(), "C:", true, "I", 9, folder },
                { "file5", new Date(), new Date(), "E:", true, "I", 12, folder } };

        // init search condition data
        condition = new HashMap<String, Object>();
        condition.put("fileName", "file");
        condition.put("minFileSize", 1L);
        condition.put("maxFileSize", 100L);

        Calendar cal = Calendar.getInstance();
        String datePattern = DateUtil.YYYY_MM_DD_HH_MM_SS;
        cal.add(Calendar.DATE, -1);
        Date minDate = cal.getTime();
        condition.put("minModifyTime", DateUtil.parse(DateUtil.format(minDate,
                datePattern), datePattern));
        condition.put("maxModifyTime", DateUtil.parse(DateUtil.format(
                new Date(), datePattern), datePattern));
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
        user.setUserName("testFileName");
        user.setPassword("testFilePass");
        user.setCreateTime(new Date());
        user.setOverSize(1);

        // init user object
        folder = new Folder();
        folder.setFolderName("testFileFolder");
        folder.setCreateTime(new Date());
        folder.setModifyTime(new Date());
        folder.setDirectory("E:");
        folder.setShare(true);
        folder.setFolderType("I");
        folder.setUser(user);

        // file rigth datas
        fileDatas = new Object[][] {
                { null, new Date(), null, "E:", true, "I", 10, folder },
                { "file2", new Date(), new Date(), "F:", false, "I", 20, folder },
                { "file3", new Date(), new Date(), "D:", null, "I", 5, null },
                { "file4", null, new Date(), "C:", true, "I", 9, folder },
                { null, new Date(), new Date(), "E:", true, null, 12, folder } };

        // init search condition data
        condition = new HashMap<String, Object>();
        condition.put("fileName", "ddd");
        condition.put("minFileSize", 1000L);
        condition.put("maxFileSize", 2000L);

        Calendar cal = Calendar.getInstance();
        String datePattern = DateUtil.YYYY_MM_DD_HH_MM_SS;
        cal.add(Calendar.DATE, -1);
        Date minDate = cal.getTime();
        condition.put("minModifyTime", DateUtil.parse(DateUtil.format(minDate,
                datePattern), datePattern));
        condition.put("maxModifyTime", DateUtil.parse(DateUtil.format(
                new Date(), datePattern), datePattern));
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
        files = new LinkedList<FileBean>();
        for (int i = 0; i < fileDatas.length; i++) {
            FileBean file = new FileBean();

            file.setFileName((String) fileDatas[i][0]);
            file.setCreateTime((Date) fileDatas[i][1]);
            file.setModifyTime((Date) fileDatas[i][2]);
            file.setDirectory((String) fileDatas[i][3]);
            file.setShare((Boolean) fileDatas[i][4]);
            file.setFileType((String) fileDatas[i][5]);
            file.setSize(((Integer) fileDatas[i][6]).longValue());

            file.setFolder(folder);
            files.add(file);
        }
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
        // add folder
        this.folderDao.add(folder);
        for (int i = 0; i < files.size(); i++) {
            this.fileDao.add(files.get(i));
        }
        // commit transaction
        EntityManagerUtil.commit();
        EntityManagerUtil.closeFactory();
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.FileDaoImpl#findTotalSizeWithFileByUser(java.lang.Long)}.
     */
    @Test
    public void testFindTotalSizeWithFileByUser() {

        User findUser = this.userDao.findUserByName(user.getUserName());
        Long size = this.fileDao.findTotalSizeWithFileByUser(findUser
                .getUserId());
        System.out.println("---------size----------" + size);
        assertTrue(size > 0);
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#add(java.lang.Object)}.
     */
    @Test(expected = Exception.class)
    public void testAddException() {
        // init error data
        packageData(false);
        try {
            // start transaction
            EntityManagerUtil.begin();
            // add user
            this.userDao.add(user);
            // add folder
            this.folderDao.add(folder);

            for (int i = 0; i < files.size(); i++) {
                this.fileDao.add(files.get(i));
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
     * {@link com.ufinity.marchant.ubank.dao.impl.FileDaoImpl#searchPaginatedForFile(int, int, java.util.Map)}.
     */
    @Test
    public void testSearchPaginatedForFile() {
        // init rigth data
        packageData(true);

        Pager<FileBean> pager = this.fileDao.searchPaginatedForFile(1, 20,
                condition);
        System.out.println("total-----------" + pager.getTotalRecords());
        assertTrue(pager.getTotalRecords() > 0);
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#find(java.io.Serializable)}.
     */
    @Test
    public void testFind() {
        // init rigth data
        packageData(true);
        List<FileBean> list = this.fileDao.queryList(0L,
                (long) fileDatas.length);

        // execute find object method operation
        for (FileBean fileBean : list) {
            FileBean fileTemp = this.fileDao.find(fileBean.getFileId());
            System.err.println(fileTemp.getFileName());
            assertNotNull(fileTemp.getFileName());
        }
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#queryList(java.io.Serializable, java.io.Serializable)}.
     */
    @Test
    public void testQueryListPKPK() {
        packageData(true);

        List<FileBean> list = this.fileDao.queryList(0L,
                (long) fileDatas.length);
        assertTrue(list.size() > 0);
    }

    /**
     * Test method for
     * {@link com.ufinity.marchant.ubank.dao.impl.GenericDaoSupport#queryList(java.io.Serializable, java.io.Serializable)}.
     */
    @Test(expected = Exception.class)
    public void testQueryListPKPKException() {
        try {
            List<FileBean> list = this.fileDao.queryList(0L,
                    (long) fileDatas.length);
            assertTrue(list.size() > 0);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
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
        List<FileBean> list = this.fileDao.queryList(0L,
                (long) fileDatas.length);

        for (FileBean file : list) {
            FileBean fileTemp = this.fileDao.find(file.getFileId());
            fileTemp.setDirectory("F:");
            this.fileDao.modify(fileTemp);
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

            FileBean file = new FileBean();
            file.setFileId(10000L);

            this.fileDao.modify(file);
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

        // delete file bean
        List<FileBean> list = this.fileDao.queryList(0L,
                (long) fileDatas.length);

        for (FileBean f : list) {
            this.fileDao.delete(f);
        }

        // delete folder
        List<Folder> folderList = this.folderDao.queryList(0L, 20L);
        if (null != folderList && folderList.size() > 0) {
            for (Folder folderTemp : folderList) {
                if (folderTemp.getFolderName().equals(folder.getFolderName())) {
                    this.folderDao.delete(folderTemp);
                }
            }
        }

        // delete user
        User findUser = this.userDao.findUserByName(user.getUserName());
        if (null != findUser) {
            this.userDao.delete(findUser);
        }

        EntityManagerUtil.commit();
        EntityManagerUtil.closeEntityManager();
    }

}
