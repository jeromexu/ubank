// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:
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
package com.ufinity.marchant.ubank.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.DateUtil;
import com.ufinity.marchant.ubank.common.DocumentUtil;
import com.ufinity.marchant.ubank.common.FileOrFolderJsonEntity;
import com.ufinity.marchant.ubank.common.FolderNode;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;
import com.ufinity.marchant.ubank.exception.DbException;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.service.FolderService;

/**
 * FolderService Implement
 * 
 * @author jibixiang
 * @version 2010-8-20
 */
public class FolderServiceImpl implements FolderService {
	private FileDao fileDao;
	private FolderDao folderDao;
	private UserDao userDao;

	// Logger for this class
	protected final Logger LOG = Logger.getInstance(FolderServiceImpl.class);

	/**
	 * Constructor for FolderServiceImpl
	 */
	public FolderServiceImpl() {
		folderDao = DaoFactory.createDao(FolderDao.class);
		fileDao = DaoFactory.createDao(FileDao.class);
		userDao = DaoFactory.createDao(UserDao.class);
	}

	/**
	 * create a new folder on disk and save to database
	 * 
	 * @param folderName
	 *            folderName
	 * @param folderType
	 *            default folder for the user directory
	 * @param parentId
	 *            parent foler 'folderId'
	 * @param userId
	 *            user ID identification
	 * @return return a folder object
	 * @throws UBankException
	 *             throw Possible exception
	 * @author bxji
	 */
	public Folder addFolder(Long userId, Long parentId, String folderName,
			String folderType) throws UBankException {
		if (userId == null || 0l == userId) {
			throw new UBankException("userId can not be empty");
		}
		if (parentId == null || 0l == parentId
				|| Validity.isNullAndEmpty(folderName)) {
			throw new UBankException(
					"parentId can not be null and folderName can not be null or space String");
		}
		try {
			User user = userDao.find(userId);
			Folder parentfolder = folderDao.find(parentId);
			if (parentfolder.getParent() == null) {
				LOG.debug("can not create new folder under the root directory");
				throw new UBankException(
						"can not create new folder under the root directory");
			}

			// create new folder object and set value
			Folder newFolder = new Folder();
			newFolder.setParent(parentfolder);
			newFolder.setCreateTime(new Date());
			newFolder.setModifyTime(new Date());
			newFolder.setDirectory(getDiskPath(parentfolder));
			newFolder.setShare(false);
			newFolder.setUser(user);
			parentfolder.getChildren().add(newFolder);

			// If there is a same name folder in the target directory
			if (isSameName(parentfolder, folderName)) {
				newFolder.setFolderName(folderName + Constant.FOLDER_COPY);
			} else {
				newFolder.setFolderName(folderName);
			}

			// set Folder type ,default is user directory
			newFolder.setFolderType(Constant.FOLDER_TYPE_CUSTOMER);
			if (!Validity.isNullAndEmpty(folderType)) {
				newFolder.setFolderType(folderType);
			}

			// create disk file
			int result = DocumentUtil.addNewFolder(newFolder);
			if (result != 1) {
				LOG.debug("Create disk directory fail.");
				return null;
			}
			// save to database
			folderDao.add(newFolder);
			return newFolder;
		} catch (Exception e) {
			LOG.debug("The database throw an  exception "
					+ "when create a folder. ", e);
			return null;
		}
	}

	/**
	 * this method get a user directory tree
	 * 
	 * @param userId
	 *            User id identification
	 * @return return this user directory tree Struct
	 * @author bxji
	 * @throws UBankException
	 *             throw Possible exception
	 */
	public FolderNode getTreeRoot(Long userId) throws UBankException {
		if (userId == null || 0l == userId) {
			throw new UBankException("userId can not be empty");
		}
		FolderNode rootNode = null;
		try {
			List<Folder> folders = folderDao.findFolderListByUserId(userId);
			rootNode = FolderNode.generateFolderTree(folders);
		} catch (Exception e) {
			LOG.debug("When try get user directory tree "
					+ "happened to thd database an exception", e);
		}
		return rootNode;
	}

	/**
	 * Get all files and sub-folders under specified directory,return
	 * FileOrFolderJsonEntity class list {method description}
	 * 
	 * @param folderId
	 *            specified folder id
	 * @return FileOrFolderJsonEntity list
	 * @author bxji
	 */
	public List<FileOrFolderJsonEntity> getAllByFolder(Long folderId) {
		if (folderId == null || 0l == folderId) {
			return null;
		}
		Folder folder = null;
		try {
			folder = folderDao.find(folderId);
		} catch (Exception e) {
			LOG.debug("happened to thd database an exception", e);
			return null;
		}
		List<FileOrFolderJsonEntity> jsonEntitys = new ArrayList<FileOrFolderJsonEntity>();
		if (folder != null) {
			// convert the FileBean to FileOrFolderJsonEntity
			Set<FileBean> files = folder.getFiles();
			if (!Validity.isEmpty(files)) {
				for (FileBean file : files) {
					FileOrFolderJsonEntity jsonEntity = new FileOrFolderJsonEntity();
					jsonEntity.setId(file.getFileId());
					jsonEntity.setPid(folder.getFolderId());
					jsonEntity.setName(file.getFileName());
					jsonEntity.setSize(file.getSize());
					jsonEntity.setModTime(DateUtil.format(file.getModifyTime(),
							DateUtil.YYYY_MM_DD_HH_MM_SS));
					jsonEntity.setDir(file.getDirectory());
					jsonEntity.setType(getDocType(file));
					jsonEntity.setInit(false);
					jsonEntitys.add(jsonEntity);
				}
			}
			// conver sub-folders Object to FileOrFolderJsonEntity
			Set<Folder> chiFolders = folder.getChildren();
			if (!Validity.isEmpty(chiFolders)) {
				for (Folder child : chiFolders) {
					FileOrFolderJsonEntity jsonEntity = new FileOrFolderJsonEntity();
					jsonEntity.setId(child.getFolderId());
					jsonEntity.setPid(folder.getFolderId());
					jsonEntity.setName(child.getFolderName());
					jsonEntity.setModTime(DateUtil
							.format(child.getModifyTime(),
									DateUtil.YYYY_MM_DD_HH_MM_SS));
					jsonEntity.setDir(child.getDirectory());
					jsonEntity.setType(SystemGlobals
							.getString(ConfigKeys.DOCUMENT_TYPE_FOLDER));
					if (Constant.FOLDER_TYPE_CUSTOMER.equals(child
							.getFolderType())) {
						jsonEntity.setInit(false);
					} else {
						jsonEntity.setInit(true);
					}
					jsonEntitys.add(jsonEntity);
				}
			}
		}
		return jsonEntitys;
	}

	/**
	 * this method is removed 'Folder' from the disk and database
	 * 
	 * @param folderId
	 *            folder identification
	 * @return success return true else return false
	 * @author bxji
	 */
	public boolean delFolder(Long folderId) {
		if (folderId == null || 0l == folderId) {
			LOG.debug("delete fail ,target folder id can not is null.");
			return false;
		}
		try {
			Folder folder = folderDao.find(folderId);
			if (!Constant.FOLDER_TYPE_CUSTOMER.equals(folder.getFolderType())) {
				LOG.debug("system Initial directoy can not delete");
				throw new UBankException(
						"system Initial directoy can not delete");
			}
			return delFolder(folder);
		} catch (Exception e) {
			LOG.debug("delete folder failed ", e);
			return false;
		}
	}

	/**
	 * Copy all the contents of the source directory to the destination folder,
	 * This method is the physical disk copy and database copy
	 * 
	 * @param targetFolderId
	 *            target Folder object identification
	 * @param sourceFolderId
	 *            source Folder object identification
	 * @return success return true else return false
	 * @author bxji
	 */
	public boolean copyFolderTo(Long targetFolderId, Long sourceFolderId) {
		if (targetFolderId == null || 0l == targetFolderId
				|| sourceFolderId == null || 0l == sourceFolderId) {
			LOG.debug("Folder replication fails, "
					+ "'targetFolderId' and 'sourceFolderId' can not be null.");
			return false;
		}
		Folder source = folderDao.find(sourceFolderId);
		Folder target = folderDao.find(targetFolderId);

		String sourcePath = source.getDirectory() + source.getFolderName();
		String targetPath = target.getDirectory() + target.getFolderName();
		DocumentUtil.copyFolder(sourcePath, targetPath);
		if (target.getShare()) {
			return copyFolder(target, source, true);
		} else {
			return copyFolder(target, source, false);
		}
	}

	/**
	 * Move all the contents of the source directory to the destination folder,
	 * This method is the physical disk move and database move
	 * 
	 * @param targetFolderId
	 *            target Folder object identification
	 * @param sourceFolderId
	 *            source Folder object identification
	 * @return success return true else return false
	 * @author bxji
	 */
	public boolean moveFolderTo(Long targetFolderId, Long sourceFolderId) {
		if (targetFolderId == null || 0l == targetFolderId
				|| sourceFolderId == null || 0l == sourceFolderId) {
			LOG.debug("Folder moved fails, "
					+ "'targetFolderId' and 'sourceFolderId' can not be null.");
			return false;
		}
		try {
			Folder source = folderDao.find(sourceFolderId);
			Folder target = folderDao.find(targetFolderId);
			target.getChildren().add(source);
			source.setParent(target);
			int result = DocumentUtil.moveFolderTo(source, target);
			if (result != 1) {
				LOG.debug("move disk file fail");
				return false;
			}
			folderDao.modify(target);
			folderDao.modify(source);
			// update all files Shared state and reset disk path (directory
			// property)
			if (target.getShare()) {
				resetChildrenDiskPathAndShare(source, true);
			} else {
				resetChildrenDiskPathAndShare(source, false);
			}
		} catch (Exception e) {
			LOG.debug("When moving folder to specified directory ,"
					+ " the database throw an exception.", e);
			return false;
		}
		return true;
	}

	/**
	 * Copy all the contents of the source directory to the destination folder
	 * 
	 * @param targetFolder
	 *            target Folder
	 * @param sourceFolder
	 *            source folder
	 * @return success return true else return false
	 * @author bxji
	 */
	private boolean copyFolder(Folder targetFolder, Folder sourceFolder,
			boolean share) {
		if (targetFolder == null || sourceFolder == null) {
			LOG.debug("Folder replication fails, "
					+ "'targetFolder' and 'sourceFolder' can not be null.");
			return false;
		}
		Folder temp = null;
		try {
			temp = (Folder) BeanUtils.cloneBean(sourceFolder);
			// If there is a same name folder in the target directory
			if (isSameName(targetFolder, temp.getFolderName())) {
				temp.setFolderName(temp.getFolderName() + Constant.FOLDER_COPY);
			}
		} catch (Exception e) {
			LOG.debug("An exception when copying a 'Folder' bean object", e);
			return false;
		}
		temp.setParent(targetFolder);
		temp.setDirectory(getDiskPath(targetFolder));
		temp.setFiles(new HashSet<FileBean>());
		temp.setChildren(new HashSet<Folder>());
		temp.setModifyTime(new Date());
		targetFolder.getChildren().add(temp);

		// update database
		try {
			folderDao.add(temp);
			folderDao.modify(targetFolder);
		} catch (Exception e) {
			LOG.debug("An exception when copying a directory "
					+ "to update the database. ", e);
			return false;
		}

		// If there are files in the directory,
		// copy the documents and save to database
		Set<FileBean> files = sourceFolder.getFiles();
		if (files.size() > 0) {
			for (FileBean file : files) {
				try {
					FileBean copy = (FileBean) BeanUtils.cloneBean(file);
					copy.setFolder(temp);
					copy.setDirectory(getDiskPath(temp));
					copy.setModifyTime(new Date());
					copy.setShare(share);
					fileDao.add(copy);
				} catch (Exception e) {
					LOG.debug("An exception when copying a file"
							+ " add to the database", e);
					return false;
				}
			}
		}
		// If the directory has a subdirectory, copy the entire directory
		Set<Folder> folders = sourceFolder.getChildren();
		if (folders.size() > 0) {
			for (Folder folder : folders) {
				// Subfolders recursive copy
				copyFolder(temp, folder, share);
			}
		}
		return true;
	}

	/**
	 * This method is used to rename a folder
	 * 
	 * @param folderId
	 *            target folder object id
	 * @param newName
	 *            new name
	 * @return success return true else return false
	 * @author bxji
	 */
	public boolean renameFolder(Long folderId, String newName) {
		if (folderId == null || 0l == folderId
				|| Validity.isNullAndEmpty(newName)) {
			LOG.debug("rename folder fail,"
					+ "target folder id can not be null"
					+ " and new name can not be null or space string");
			return false;
		}
		try {
			Folder folder = folderDao.find(folderId);
			// if old name equals new name
			if (newName.equals(folder.getFolderName())) {
				return true;
			}

			// If there is a same name folder in the target directory
			if (isSameName(folder.getParent(), newName)) {
				folder.setFolderName(newName + Constant.FOLDER_COPY);
			} else {
				folder.setFolderName(newName);
			}
			int result = DocumentUtil.renameFolder(folder, newName);
			if (result != 1) {
				LOG.debug("disk folder rename fail");
				return false;
			}
			folderDao.modify(folder);
			return true;
		} catch (Exception e) {
			LOG.debug("database exception ,when folder rename .", e);
			return false;
		}
	}

	/**
	 * Remove all the contents of the specified directory from the disk and the
	 * database
	 * 
	 * @param targetFolder
	 *            the specified directory
	 * @return success return true else return false
	 * @author bxji
	 */
	private boolean delFolder(Folder targetFolder) {
		if (targetFolder == null) {
			return true;
		}
		Set<FileBean> files = targetFolder.getFiles();
		Set<Folder> folders = targetFolder.getChildren();
		// delete all files
		for (FileBean file : files) {
			try {
				int result = DocumentUtil.removeFile(file);
				// if disk file delete fail, return 'false'
				if (result != 1) {
					LOG.debug("delete disk file fail, file name:"
							+ file.getDirectory() + file.getFileName());
					return false;
				}
				fileDao.delete(file);
			} catch (Exception e) {
				LOG.debug("update the database exception "
						+ "when delete a disk file .", e);
				return false;
			}
		}
		// delete all folders
		for (Folder folder : folders) {
			delFolder(folder);
		}
		try {
			int result = DocumentUtil.removeFolder(targetFolder);
			// if disk file delete fail, return 'false'
			if (result != 1) {
				LOG.debug("delete disk folder fail, file name:"
						+ targetFolder.getDirectory()
						+ targetFolder.getFolderId());
				return false;
			}
			folderDao.delete(targetFolder);
		} catch (Exception e) {
			LOG.debug("delete disk directory fail.", e);
			return false;
		}
		return true;
	}

	/**
	 * this method can Sharing a directory
	 * 
	 * @param folderId
	 *            target folder object id
	 * @return success return true else return false
	 * @author bxji
	 */
	public boolean shareFolder(Long folderId) {
		if (folderId == null || 0l == folderId) {
			return false;
		}
		try {
			Folder folder = folderDao.find(folderId);
			folder.setShare(true);
			folderDao.modify(folder);
			return shareOrCanceAllFiles(folder, true);
		} catch (Exception e) {
			LOG.debug("When sharing a folder,database"
					+ " throw an exception .", e);
			return false;
		}
	}

	/**
	 * this method is cancel share a directory operation
	 * 
	 * @param folderId
	 *            target folder object id
	 * @return success return true else return false
	 * @author bxji
	 */
	public boolean cancelShareFolder(Long folderId) {
		if (folderId == null || 0l == folderId) {
			return false;
		}
		try {
			Folder folder = folderDao.find(folderId);
			return cancelShareFolder(folder);
		} catch (Exception e) {
			LOG.debug("When cancel share a folder "
					+ ",database throw an exception", e);
			return false;
		}
	};

	/**
	 * This method share all files under the target directory, including the
	 * subdirectories of all files
	 * 
	 * @param folder
	 *            target directory
	 * @param share
	 *            Shared state
	 * @return success return true else return false
	 * @author bxji
	 */
	private boolean shareOrCanceAllFiles(Folder folder, boolean share) {
		if (folder == null) {
			return false;
		}
		Set<Folder> children = folder.getChildren();
		Set<FileBean> files = folder.getFiles();
		for (FileBean file : files) {
			file.setShare(share);
			fileDao.modify(file);
		}
		for (Folder child : children) {
			shareOrCanceAllFiles(child, share);
		}
		return true;
	}

	/**
	 * this method is set all files share status is 'false' under specified
	 * directory
	 * 
	 * @param folder
	 *            target folder
	 * @return success return true else return false
	 * @author bxji
	 */
	private boolean cancelShareFolder(Folder folder) {
		if (folder == null) {
			return false;
		}
		try {
			folder.setShare(false);
			folderDao.modify(folder);
			Set<FileBean> files = folder.getFiles();
			for (FileBean file : files) {
				file.setModifyTime(new Date());
				file.setShare(false);
				fileDao.modify(file);
			}
			Set<Folder> children = folder.getChildren();
			for (Folder child : children) {
				cancelShareFolder(child);
			}
		} catch (Exception e) {
			LOG.debug("update database exception", e);
			return false;
		}

		return true;
	}

	/**
	 * get current directory disk path
	 * 
	 * @param parentFolder
	 *            parent directory of current directory
	 * @return disk path String
	 * @author bxji
	 */
	private String getDiskPath(Folder parentFolder) {
		String currentPath = "";
		if (parentFolder == null) {
			return currentPath;
		}
		String parentPath = parentFolder.getDirectory();
		if (parentPath.length() > 0) {
			char c = parentPath.charAt(parentPath.length() - 1);
			if ('\\' == c) {
				currentPath = parentPath + parentFolder.getFolderId() + "\\";
			} else {
				currentPath = parentPath + "\\" + parentFolder.getFolderId()
						+ "\\a";
			}
		}
		return currentPath;
	}

	/**
	 * Reset the subfolders and files disk path and reset all files shared
	 * status
	 * 
	 * @param folder
	 *            target directory
	 * @param share
	 *            share status
	 * @return success return true else return false
	 * @author bxji
	 */
	private boolean resetChildrenDiskPathAndShare(Folder folder, boolean share) {
		if (folder == null) {
			return true;
		}
		final Date DATE = new Date();
		final String PATH = getDiskPath(folder);
		try {
			Set<Folder> children = folder.getChildren();
			for (Folder child : children) {
				child.setModifyTime(DATE);
				child.setDirectory(PATH);
				folderDao.modify(child);
				resetChildrenDiskPathAndShare(child, share);
			}
			Set<FileBean> files = folder.getFiles();
			for (FileBean file : files) {
				file.setModifyTime(DATE);
				file.setShare(share);
				file.setDirectory(PATH);
				fileDao.modify(file);
			}
		} catch (Exception e) {
			LOG.debug("update database exception", e);
			return false;
		}
		return true;
	}

	/**
	 * Whether the same name with the current directory folders
	 * 
	 * @param folder
	 *            current directory
	 * @param name
	 *            folder name
	 * @return have same name return 'true' else return 'false'
	 * @throws DbException
	 *             if folder is null or name is nul throw exception
	 * @author bxji
	 */
	private boolean isSameName(Folder folder, String name) throws DbException {
		if (folder == null || Validity.isNullAndEmpty(name)) {
			LOG.debug("target folder can not be null");
			throw new DbException("target folder and name can not be null.");
		}
		Set<Folder> children = folder.getChildren();
		for (Folder child : children) {
			if (name.equals(child.getFolderName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get file type by the file suffix
	 * 
	 * @param file
	 *            fileBean object
	 * @return file type string
	 * @author bxji
	 */
	private String getDocType(FileBean file) {
		if (file == null) {
			return "";
		}
		String name = file.getFileName();
		int index = name.lastIndexOf('.');
		if (index != -1) {
			String typeName = name.substring(index);
			if (Validity.isNullAndEmpty(typeName)) {
				return typeName.trim().toUpperCase();
			}
		}
		return SystemGlobals.getString(ConfigKeys.DOCUMENT_TYPE_UNKNOWN);
	}

	/**
	 * get user root directory
	 * 
	 * @param userId
	 *            user id
	 * @return return user root directory
	 * @author bxji
	 */
	public Folder getRootFolder(Long userId) {
		if (userId == null || 0l == userId) {
			return null;
		}
		Folder folder = null;
		try {
			folder = folderDao.findRootRolderByUserId(userId);
		} catch (Exception e) {
			LOG.debug("get user root folder the database exception ", e);
		}
		return folder;
	}
}
