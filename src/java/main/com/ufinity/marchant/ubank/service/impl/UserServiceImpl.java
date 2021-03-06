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
import com.ufinity.marchant.ubank.common.DocumentUtil;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;
import com.ufinity.marchant.ubank.exception.UBankServiceException;
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
	protected final Logger logger = Logger.getInstance(UserServiceImpl.class);
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
	 * @throws UBankServiceException
	 *             if occur exception, then throw it
	 * @author jerome
	 */
	public String doRegister(User user) throws UBankServiceException {
		logger.debug("doRegister:param[user]=" + user);
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

				user.setCreateTime(new Date());
				user.setOverSize(SystemGlobals
						.getInt(ConfigKeys.DEFAULT_USER_SPACE_SIZE));
				user.setPoint(SystemGlobals
						.getInt(ConfigKeys.USER_DEFAULT_POINT));
				EntityManagerUtil.begin();
				userDao.add(user);
				logger.debug("add user success!" + user);
				// init user dir space
				boolean initFlag = initDirSpace(user.getUserId());
				logger.debug("init user space "
						+ (initFlag ? "success" : "failue") + "!");
				EntityManagerUtil.commit();

				return initFlag ? MessageKeys.REGISTER_SUCCESS
						: MessageKeys.REGISTER_FAILURE;
			} else {
				return MessageKeys.REGISTER_USER_EXIST;
			}

		} catch (Exception e) {
			logger.error("user register exception!", e);
			if (EntityManagerUtil.isActive()) {
				EntityManagerUtil.rollback();
			}
			throw new UBankServiceException("user regiter service exception!");
		} finally {
			EntityManagerUtil.closeEntityManager();
		}

	}

	/**
	 * Get User by username and password
	 * 
	 * @param username
	 *            user's name
	 * @param password
	 *            user's password
	 * @return User obj
	 * @throws UBankServiceException
	 *             if occur exception, then throw it
	 * @author zdxue
	 */
	public User getUser(String username, String password)
			throws UBankServiceException {
		logger.debug("username=" + username + " , password=" + password);

		User user = null;
		try {
			EntityManagerUtil.begin();

			user = userDao.findUser(username, password);
			logger.debug("userDao find user " + user);

			EntityManagerUtil.commit();
		} catch (Exception e) {
			// not need rollback
			logger.error("get user exception", e);
			throw new UBankServiceException("get user exception");
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
	 * init the user space by the user id
	 * 
	 * @param userId
	 *            user id
	 * @return true:success false：failure
	 * @author jerome
	 */
	private boolean initDirSpace(Long userId) {

		logger.debug("initUserDir:param[userId]=" + userId);
		try {
			String serverPath = DocumentUtil.getApplicationPath();
			logger.debug("get the server path: " + serverPath);
			if (!Validity.isEmpty(serverPath)) {
				File baseFile = new File(serverPath);
				if (!baseFile.exists()) {
					baseFile.mkdir();
				}
				StringBuffer sb = new StringBuffer(serverPath);
				baseFile = new File(
						sb
								.append(Constant.FILE_SYSTEM_SEPARATOR)
								.append(
										SystemGlobals
												.getString(ConfigKeys.USER_SPACE_ROOT_DIR))
								.toString());
				if (!baseFile.exists()) {
					baseFile.mkdir();
				}
				// root folder for each user
				baseFile = new File(sb.append(Constant.FILE_SYSTEM_SEPARATOR)
						.append(userId).toString());
				if (!baseFile.exists()) {
					baseFile.mkdir();
				}

				Folder folder = new Folder();
				folder.setFolderName(String.valueOf(userId));
				Date date = new Date();
				folder.setCreateTime(date);
				folder.setDirectory(Constant.FILE_SYSTEM_SEPARATOR
						+ SystemGlobals
								.getString(ConfigKeys.USER_SPACE_ROOT_DIR));
				folder.setModifyTime(date);
				folder.setFolderType(Constant.FOLDER_TYPE_ROOT);
				folder.setShare(false);
				folder.setParent(null);

				User user = new User();
				user.setUserId(userId);
				folder.setUser(user);
				folderDao.add(folder);
				logger.debug("add root folder success during initialization!");
				String[] initFileName = new String[] {
						SystemGlobals.getString(ConfigKeys.MY_FILE_NAME),
						SystemGlobals.getString(ConfigKeys.SOFTWARE_NAME),
						SystemGlobals.getString(ConfigKeys.DOCUMENT_NAME),
						SystemGlobals.getString(ConfigKeys.PHOTO_NAME) };
				boolean result = true;
				for (int i = 0; i < initFileName.length; i++) {
					boolean eachDir = makeEachUserDir(baseFile, folder, userId,
							sb, initFileName[i]);
					result = result && eachDir;
				}

				logger.debug("init dir space "
						+ (result ? "success!" : "failure!"));
				return result;
			}

		} catch (Exception e) {
			logger.error("init user space excepiton!", e);
		}

		return false;
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
	private boolean makeEachUserDir(File baseFile, Folder parent, Long userId,
			StringBuffer sb, String folderName) {
		logger.debug("makeEachUserDir:param[baseFile]=" + baseFile
				+ ",param[parent]=" + parent + ",param[userId]=" + userId
				+ ",param[sb]=" + sb.toString() + ",param[folderName]="
				+ folderName);
		try {

			Folder folder = new Folder();
			folder.setFolderName(folderName);
			Date date = new Date();
			folder.setCreateTime(date);
			folder.setModifyTime(date);
			folder.setFolderType(Constant.FOLDER_TYPE_INIT);
			folder.setShare(false);
			folder.setDirectory(Constant.FILE_SYSTEM_SEPARATOR
					+ SystemGlobals.getString(ConfigKeys.USER_SPACE_ROOT_DIR)
					+ Constant.FILE_SYSTEM_SEPARATOR + userId);

			Folder parentFolder = new Folder();
			parentFolder.setFolderId(parent.getFolderId());
			folder.setParent(parentFolder);

			User user = new User();
			user.setUserId(userId);
			folder.setUser(user);

			folderDao.add(folder);
			logger.debug("add " + folderName
					+ " folder success during initialization!");

			if (SystemGlobals.getString(ConfigKeys.MY_FILE_NAME).equals(
					folderName)) {
				baseFile = new File(sb.append(Constant.FILE_SYSTEM_SEPARATOR)
						.append(String.valueOf(folder.getFolderId()))
						.toString());
			} else {
				sb.replace(sb.lastIndexOf(Constant.FILE_SYSTEM_SEPARATOR) + 1,
						sb.length(), String.valueOf(folder.getFolderId()));
				baseFile = new File(sb.toString());
			}
			boolean result = true;
			if (!baseFile.exists()) {
				result = baseFile.mkdir();
			}
			logger.debug("make each user dir "
					+ (result ? "success!" : "failure!"));
			return result;
		} catch (Exception e) {
			logger.error("make each user dir expcetion!", e);
		}
		return false;
	}

	/**
	 * the getter method of userDao
	 * 
	 * @return the userDao
	 */
	public UserDao getUserDao() {
		return userDao;
	}

	/**
	 * the setter method of the userDao
	 * 
	 * @param userDao
	 *            the userDao to set
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * the getter method of folderDao
	 * 
	 * @return the folderDao
	 */
	public FolderDao getFolderDao() {
		return folderDao;
	}

	/**
	 * the setter method of the folderDao
	 * 
	 * @param folderDao
	 *            the folderDao to set
	 */
	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}

}
