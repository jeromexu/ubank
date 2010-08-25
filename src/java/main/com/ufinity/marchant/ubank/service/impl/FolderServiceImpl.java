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
        try {
            User user = userDao.find(userId);
            Folder parentfolder = folderDao.find(parentId);

            // create new foler object and set value
            Folder newFolder = new Folder();
            newFolder.setParent(parentfolder);
            newFolder.setCreateTime(new Date());
            newFolder.setFolderName(folderName);

            // If there is a the same name folder in current directory
            Set<Folder> children = parentfolder.getChildren();
            for (Folder child : children) {
                if (folderName.equals(child.getFolderName())) {
                    newFolder.setFolderName(folderName + Constant.FOLDER_COPY);
                }
            }
            newFolder.setDirectory(getDiskPath(parentfolder));

            // set Folder type ,default is user directory
            newFolder.setFolderType(Constant.CUSTOMER);
            if (!Validity.isNullAndEmpty(FolderType)) {
                newFolder.setFolderType(FolderType);
            }
            newFolder.setShare(false);
            newFolder.setUser(user);

            // create disk file
            int result = DocumentUtil.addNewFolder(newFolder);
            if (result != 1) {
                logger.debug("Create disk directory fail.");
                return null;
            }

            // save to database
            folderDao.add(newFolder);
            logger.debug("folder object save to database success");
            return newFolder;
        }
        catch (Exception e) {
            logger.debug(
                    "When create a folder, the database throw an  exception. ",
                    e);
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
        }
        catch (Exception e) {
            logger.debug("When try get user directory tree "
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
        }
        catch (Exception e) {
            logger.debug("happened to thd database an exception", e);
            return null;
        }
        List<FileOrFolderJsonEntity> jsonEntieys = new ArrayList<FileOrFolderJsonEntity>();
        if (folder != null) {
            Set<FileBean> files = folder.getFiles();
            Set<Folder> chiFolders = folder.getChildren();

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
            logger.debug("delete fail ,target folder id can not is null.");
            return false;
        }
        try {
            Folder folder = folderDao.find(folderId);
            return delFolder(folder);
        }
        catch (Exception e) {
            logger.debug("delete folder failed ", e);
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
            logger.debug("Folder replication fails, "
                    + "'targetFolderId' and 'sourceFolderId' can not be null.");
            return false;
        }
        Folder source = folderDao.find(sourceFolderId);
        Folder target = folderDao.find(targetFolderId);

        String sourcePath = source.getDirectory() + source.getFolderName();
        String targetPath = target.getDirectory() + target.getFolderName();
        DocumentUtil.copyFolder(sourcePath, targetPath);
        return copyFolder(target, source);
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
        try {
            Folder source = folderDao.find(sourceFolderId);
            Folder target = folderDao.find(targetFolderId);
            target.getChildren().add(source);
            source.setParent(target);
            DocumentUtil.moveFolderTo(source, target);
            folderDao.modify(target);
            folderDao.modify(source);
            
            // set all files Shared state
            if (target.getShare()) {
                shareOrCanceAllFiles(source, true);
            }
            else {
                shareOrCanceAllFiles(source, false);
            }
        }
        catch (Exception e) {
            logger.debug("When moving folder to specified directory ,"
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
    private boolean copyFolder(Folder targetFolder, Folder sourceFolder) {
        if (targetFolder == null || sourceFolder == null) {
            logger.debug("Folder replication fails, "
                    + "'targetFolder' and 'sourceFolder' can not be null.");
            return false;
        }
        Folder temp = null;
        try {
            temp = (Folder) BeanUtils.cloneBean(sourceFolder);
        }
        catch (Exception e) {
            logger.debug("An exception when copying a 'Folder' bean object", e);
        }
        // if Copy to the current directory
        if (sourceFolder.getParent().equals(targetFolder)) {
            temp.setFolderName(sourceFolder.getFolderName()
                    + Constant.FOLDER_COPY);
        }
        temp.setParent(targetFolder);
        temp.setDirectory(temp.getParent().getDirectory()
                + temp.getParent().getFolderId());
        temp.setFiles(new HashSet<FileBean>());
        temp.setChildren(new HashSet<Folder>());
        targetFolder.getChildren().add(temp);
        // update database
        try {
            folderDao.add(temp);
            folderDao.modify(targetFolder);
        }
        catch (Exception e) {
            logger.debug("An exception when copying a directory "
                    + "to update the database. ", e);
            return false;
        }

        Set<FileBean> files = sourceFolder.getFiles();
        Set<Folder> folders = sourceFolder.getChildren();

        // If there are files in the directory,
        // copy the documents and save to database
        if (files.size() > 0) {
            for (FileBean file : files) {
                try {
                    FileBean copy = (FileBean) BeanUtils.cloneBean(file);
                    copy.setFolder(temp);
                    copy.setDirectory(targetFolder.getDirectory()
                            + targetFolder.getFolderId());
                    // is target floder is shared directory
                    if (targetFolder.getShare()) {
                        copy.setShare(true);
                    }
                    else {
                        copy.setShare(false);
                    }
                    fileDao.add(copy);
                }
                catch (Exception e) {
                    logger.debug("An exception when copying a file"
                            + " add to the database", e);
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
            logger.debug("rename folder fail,"
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
            Set<Folder> brothers = folder.getParent().getChildren();
            // If there is a the same name folder
            for (Folder brother : brothers) {
                if (newName.equals(brother.getFolderName())) {
                    newName = newName + Constant.FOLDER_COPY;
                }
            }
            folder.setFolderName(newName);
            int result = DocumentUtil.renameFolder(folder, newName);
            if (result != 1) {
                logger.debug("disk folder rename fail");
                return false;
            }
            folderDao.modify(folder);
            return true;
        }
        catch (Exception e) {
            logger.debug("database exception ,when folder rename .", e);
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
                    logger.debug("delete disk file fail, file name:"
                            + file.getDirectory() + file.getFileName());
                    return false;
                }
                fileDao.delete(file);
            }
            catch (Exception e) {
                logger.debug("update the database exception "
                        + "when delete a disk file .", e);
                return false;
            }
        }
        // delete all folders
        for (Folder folder : folders) {
            try {
                delFolder(folder);
                int result = DocumentUtil.removeFolder(folder);
                // if disk file delete fail, return 'false'
                if (result != 1) {
                    logger.debug("delete disk folder fail, file name:"
                            + folder.getDirectory() + folder.getFolderId());
                    return false;
                }
                folderDao.delete(folder);
            }
            catch (Exception e) {
                logger.debug("delete disk directory fail.", e);
                return false;
            }
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
        }
        catch (Exception e) {
            logger.debug("When sharing a folder,database"
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
        }
        catch (Exception e) {
            logger.debug("When cancel share a folder "
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
        folder.setShare(false);
        folderDao.modify(folder);
        Set<Folder> children = folder.getChildren();
        Set<FileBean> files = folder.getFiles();
        for (FileBean file : files) {
            file.setShare(false);
            fileDao.modify(file);
        }
        for (Folder child : children) {
            cancelShareFolder(child);
        }
        return false;
    }

    /**
     * {method description}
     * 
     * @param parentFolder
     * @return
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
            }
            else {
                currentPath = parentPath + "\\" + parentFolder.getFolderId()
                        + "\\";
            }
        }
        return currentPath;
    }
}
