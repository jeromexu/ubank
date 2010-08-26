//-------------------------------------------------------------------------
// Copyright (c) 2000-2009 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:
//
//-------------------------------------------------------------------------
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
//-------------------------------------------------------------------------
package com.ufinity.marchant.ubank.service.impl;

import java.io.File;
import java.util.Date;
import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageResource;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.service.UserService;

/**
 * 
 * user business logic implements class
 * 
 * @author jerome
 * @version 1.0
 * @since 2010-8-18
 */
public class UserServiceImpl implements UserService {

    // Logger for this class
    protected static final Logger LOG = Logger.getInstance(UserServiceImpl.class);
    private UserDao userDao;
    private FolderDao folderDao;

    /**
     * 
     * Constructor for UserServiceImpl
     */
    public UserServiceImpl() {
        userDao = DaoFactory.createDao(UserDao.class);
        folderDao = DaoFactory.createDao(FolderDao.class);
    }

    /**
     * 
     * do user register
     * 
     * @param user
     *            a person who do register
     * @return the number which register a user
     * @author jerome
     */
    public String doRegister(User user) {
    	 LOG.debug("doRegister:param[user]=" + user);
        try {
           
            String userName = null;
            if (user != null) {
                userName = user.getUserName();
            } else {
                return MessageKeys.REGISTER_FAILURE;
            }
            // query current user exist or not
            User queryUser = userDao.findUserByName(userName);
            if (queryUser == null) {
            	EntityManagerUtil.begin();
                userDao.add(user);
                // init user dir space
                boolean initFlag = createUserDir(user.getUserId());
                EntityManagerUtil.commit();
                if (initFlag) {
                	return MessageKeys.REGISTER_SUCCESS;
                } else {
                	return MessageKeys.REGISTER_FAILURE;
                }
            } else {
                // user exist,do not register
                return MessageKeys.REGISTER_FAILURE;
            }
            
        } catch (Exception e) {
            LOG.error("user register exception!", e);
        } finally {
            EntityManagerUtil.closeEntityManager();
        }
        LOG.debug("doRegister:-----complete--------");
        // register success
        return MessageKeys.REGISTER_SUCCESS;
    }

    /**
     * Get User by username and password
     * 
     * @param username
     *            user's name
     * @param password
     *            user's password
     * @return User obj
     * @throws UBankException
     *             if occur exception, then throw it
     * @author zdxue
     */
    public User getUser(String username, String password) throws UBankException {
        User user = null;
        try {
            EntityManagerUtil.begin();
            user = userDao.findUser(username, password);
            EntityManagerUtil.commit();
        } catch (Exception e) {
            //not need rollback
            LOG.error("get user error", e);
        } finally {
            EntityManagerUtil.closeEntityManager();
        }

        return user;
    }

    /**
     * 
     * get user by userName
     * 
     * @param userName
     *            user's name
     * @return user object
     */
    public User getUserByUserName(String userName) {
        return userDao.findUserByName(userName);
    }

    /**
     * 
     * create the user space by the user id
     * 
     * @param userId
     *            user id
     * @return true:success false：failure
     * @author jerome
     */
    private boolean createUserDir(Long userId) {

        LOG.debug("createUserDir:param[userId]=" + userId);
        try{
        	String serverPath = getApplicationPath();
            if (!Validity.isEmpty(serverPath)) {
                File baseFile = new File(serverPath);
                if (!baseFile.exists()) {
                    baseFile.mkdir();
                }
                StringBuffer sb = new StringBuffer(serverPath);
                baseFile = new File(sb.append(SystemGlobals.getString(ConfigKeys.USER_SPACE_ROOT_DIR)).toString());
                if (!baseFile.exists()) {
                    baseFile.mkdir();
                }
                // root folder for each user
                baseFile = new File(sb.append(File.separator).append(userId)
                        .toString());
                if (!baseFile.exists()) {
                    baseFile.mkdir();
                }
                Folder folder = new Folder();
                folder.setFolderName(String.valueOf(userId));
                folder.setCreateTime(new Date());
                folder.setDirectory(SystemGlobals.getString(ConfigKeys.USER_SPACE_ROOT_DIR));
                folder.setModifyTime(new Date());
                folder.setFolderType(Constant.ROOT);
                folder.setShare(false);

               /* Folder parent = new Folder();
                parent.setFolderId(0l);*/
                folder.setParent(null);

                User user = new User();
                user.setUserId(userId);
                folder.setUser(user);

                folderDao.add(folder);

                // my file folder for each user
                makeEachUserDir(baseFile, folder, userId, sb, SystemGlobals.getString(ConfigKeys.MY_File_NAME));
                // software folder for each user
                makeEachUserDir(baseFile, folder, userId, sb,
                		SystemGlobals.getString(ConfigKeys.SOFTWARE_NAME));

                // document folder for each user
                makeEachUserDir(baseFile, folder, userId, sb,
                		SystemGlobals.getString(ConfigKeys.DOCUMENT_NAME));

                // photo folder for each user
                makeEachUserDir(baseFile, folder, userId, sb, SystemGlobals.getString(ConfigKeys.PHOTO_NAME));
            } else {
                return false;
            }
        } catch(Exception e) {
        	LOG.error("create user space excepiton!", e);
        	return false;
        } 
        
        LOG.debug("createUserDir:-----complete--------");
        return true;
    }

    /**
     * 
     * get the server path
     * 
     * @return the server path
     * @author jerome
     */
    private String getApplicationPath() {
        return SystemGlobals.getString(ConfigKeys.SERVER_PATH);
    }

    /**
     * 
     * make user's dir for each user
     * 
     * @param baseFile
     *            file dir
     * @param parent
     *            folder parent object
     * @param userId
     *            user id
     * @param sb
     *            user dir path
     * @param folderName
     *            user folder name
     * @author jerome
     */
    private void makeEachUserDir(File baseFile, Folder parent, Long userId,
            StringBuffer sb, String folderName) {
        LOG.debug("makeEachUserDir:param[baseFile]=" + baseFile
                + ",param[parent]=" + parent + ",param[userId]=" + userId
                + ",param[sb]=" + sb.toString() + ",param[folderName]="
                + folderName);
       
        Folder folder = new Folder();
        folder.setFolderName(folderName);
        folder.setCreateTime(new Date());
        folder.setModifyTime(new Date());
        folder.setFolderType(Constant.INIT);
        folder.setShare(false);
        folder.setDirectory(SystemGlobals.getString(ConfigKeys.USER_SPACE_ROOT_DIR) + "/" + userId);

        Folder parentFolder = new Folder();
        parentFolder.setFolderId(parent.getFolderId());
        folder.setParent(parentFolder);

        User user = new User();
        user.setUserId(userId);
        folder.setUser(user);

        folderDao.add(folder);
        
        if (SystemGlobals.getString(ConfigKeys.MY_File_NAME).equals(folderName)) {
            baseFile = new File(sb.append(File.separator).append(String.valueOf(folder.getFolderId())).toString());
        } else {
            sb.replace(sb.lastIndexOf(File.separator) + 1, sb.length(), String.valueOf(folder.getFolderId()));
            baseFile = new File(sb.toString());
        }

        if (!baseFile.exists()) {
            baseFile.mkdir();
        }
        LOG.debug("makeEachUserDir:-----complete--------");
    }

	/**
	 * the getter method of userDao
	 * @return the userDao
	 */
	public UserDao getUserDao() {
		return userDao;
	}

	/**
	 * the setter method of the userDao
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * the getter method of folderDao
	 * @return the folderDao
	 */
	public FolderDao getFolderDao() {
		return folderDao;
	}

	/**
	 * the setter method of the folderDao
	 * @param folderDao the folderDao to set
	 */
	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
    
    
}
