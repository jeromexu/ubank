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
import org.apache.log4j.Logger;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.DocumentUtil;
import com.ufinity.marchant.ubank.common.FileOrFolderJsonEntity;
import com.ufinity.marchant.ubank.common.FolderNode;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;
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
    protected final Logger logger = Logger.getLogger(FolderServiceImpl.class);

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
     * @param FolderType
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
            String FolderType) throws UBankException {
        if (userId == null || 0l == userId) {
            throw new UBankException("userId can not be empty");
        }
        if (parentId == null || 0l == parentId
                || Validity.isNullAndEmpty(folderName)) {
            throw new UBankException(
                    "parentId can not be null and folderName can not be null or space String");
        }

        User user = userDao.find(userId);

        Folder parentfolder = folderDao.find(parentId);

        // create new foler object and set value
        Folder newFolder = new Folder();
        newFolder.setParent(parentfolder);
        newFolder.setFolderName(folderName);
        newFolder.setCreateTime(new Date());
        newFolder.setDirectory("-------");
        newFolder.setFolderType(Constant.CUSTOMER);
        if (!Validity.isNullAndEmpty(FolderType)) {
            newFolder.setFolderType(FolderType);
        }
        newFolder.setShare(false);
        newFolder.setUser(user);
        try {
            // create disk file
            DocumentUtil.addNewFolder(newFolder);
            // save to database
            folderDao.add(newFolder);
            logger.debug("folder object save to database success");
        }
        catch (Exception e) {
            logger.debug(
                    "create disk file error or save to database table error ",
                    e);
            throw new UBankException(
                    "create disk file error or save to database table error ");
        }
        return newFolder;

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
        List<Folder> folders = folderDao.findFolderListByUserId(userId);
        FolderNode rootNode = FolderNode.generateFolderTree(folders);
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
        Folder folder = folderDao.find(folderId);
        Set<FileBean> files = folder.getFiles();
        Set<Folder> chiFolders = folder.getChildren();

        List<FileOrFolderJsonEntity> jsonEntieys = new ArrayList<FileOrFolderJsonEntity>();

        // convert the FileBean to FileOrFolderJsonEntity
        if (!Validity.isEmpty(files)) {
            for (FileBean file : files) {
                FileOrFolderJsonEntity jsonEntity = new FileOrFolderJsonEntity();
                jsonEntity.setId(file.getFileId());
                jsonEntity.setName(file.getFileName());
                jsonEntity.setSize(file.getSize());
                jsonEntity.setModifyTime(file.getModifyTime());
                jsonEntity.setDirectory(file.getDirectory());
                jsonEntity.setType(FileOrFolderJsonEntity.TYPE_FILE);
                jsonEntieys.add(jsonEntity);
            }
        }

        // conver sub-folders Object to FileOrFolderJsonEntity
        if (Validity.isEmpty(chiFolders)) {
            for (Folder child : chiFolders) {
                FileOrFolderJsonEntity jsonEntity = new FileOrFolderJsonEntity();
                jsonEntity.setId(child.getFolderId());
                jsonEntity.setName(child.getFolderName());
                jsonEntity.setModifyTime(child.getModifyTime());
                jsonEntity.setDirectory(child.getDirectory());
                jsonEntity.setType(FileOrFolderJsonEntity.TYPE_FOLDER);
                jsonEntieys.add(jsonEntity);
            }
        }
        return jsonEntieys;
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
            return false;
        }
        Folder folder = folderDao.find(folderId);
        if (folder == null) {
            return false;
        }
        try {
            DocumentUtil.removeFolder(folder);
            folderDao.deleteById(folderId);
            logger.debug("delete folder operation success.");
        }
        catch (Exception e) {
            logger.debug("delete folder failed ", e);
            return false;
        }
        return true;
    }

    /**
     * {method description}
     * 
     * @return ddd
     */
    public boolean copyFolderTo(Long targetFolderId, Long sourceFolderId) {
        if (targetFolderId == null || 0l == targetFolderId
                || sourceFolderId == null || 0l == sourceFolderId) {
            logger.debug("Folder replication fails, "
                    + "'targetFolderId' and 'sourceFolderId' can not be null.");
            return false;
        }
        Folder source = folderDao.find(sourceFolderId);
        Folder target = folderDao.find(targetFolderId);
        boolean result = copyFolder(target, source);
        if (result) {
            DocumentUtil.copyFolder(source.getDirectory()
                    + source.getFolderName(), target.getDirectory()
                    + target.getFolderName());
            return true;
        }
        return false;
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
            logger.debug("Folder moved fails, "
                    + "'targetFolderId' and 'sourceFolderId' can not be null.");
            return false;
        }
        Folder source = folderDao.find(sourceFolderId);
        Folder target = folderDao.find(targetFolderId);
        target.getChildren().add(source);
        source.setParent(target);
        DocumentUtil.moveFolderTo(source, target);
        folderDao.modify(source);
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
    private boolean copyFolder(Folder targetFolder, Folder sourceFolder) {
        if (targetFolder == null || sourceFolder == null) {
            logger.debug("Folder replication fails, "
                    + "'targetFolder' and 'sourceFolder' can not be null.");
            return false;
        }

        Folder temp = new Folder();
        try {
            temp = (Folder) BeanUtils.cloneBean(sourceFolder);
        }
        catch (Exception e) {
        }
        temp.setParent(targetFolder);
        temp.setDirectory(temp.getParent().getDirectory()
                + temp.getFolderName());
        temp.setFiles(new HashSet<FileBean>());
        temp.setChildren(new HashSet<Folder>());
        targetFolder.getChildren().add(temp);
        folderDao.add(temp);
        folderDao.modify(targetFolder);

        Set<FileBean> files = sourceFolder.getFiles();
        Set<Folder> folders = sourceFolder.getChildren();

        // If there are files in the directory,
        // copy the documents and save to database
        if (files.size() > 0) {
            for (FileBean file : files) {
                try {
                    FileBean copy = (FileBean) BeanUtils.cloneBean(file);
                    copy.setFolder(temp);
                    fileDao.add(copy);
                }
                catch (Exception e) {
                }
            }
        }

        // If the directory has a subdirectory, copy the entire directory
        if (folders.size() > 0) {
            for (Folder folder : folders) {
                // Subfolders recursive copy
                copyFolder(temp, folder);
            }
        }
        return true;
    }
}
