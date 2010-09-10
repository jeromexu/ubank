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
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.exception.UBankServiceException;
import com.ufinity.marchant.ubank.service.FolderService;
import com.ufinity.marchant.ubank.vo.FileOrFolderJsonEntity;
import com.ufinity.marchant.ubank.vo.FolderNode;

/**
 * FolderService Implement
 * 
 * @author jibixiang
 * @version 2010-8-20
 */
public class FolderServiceImpl implements FolderService {
    private FileDao fileDao;
    private FolderDao folderDao;

    // Logger for this class
    protected final Logger logger = Logger.getInstance(FolderServiceImpl.class);

    /**
     * Constructor for FolderServiceImpl
     */
    public FolderServiceImpl() {
        folderDao = DaoFactory.createDao(FolderDao.class);
        fileDao = DaoFactory.createDao(FileDao.class);
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
     * @param user
     *            current user
     * @return return a folder object
     * @throws UBankServiceException
     *             throw Possible exception
     * @author bxji
     */
    public Folder addFolder(User user, Long parentId, String folderName,
            String folderType) throws UBankServiceException {
        if (user == null) {
            throw new UBankServiceException("user can not be empty");
        }
        if (Validity.isNullOrZero(parentId)
                || Validity.isNullAndEmpty(folderName)) {
            throw new UBankServiceException(
                    "parentId can not be null and folderName can not be null or space String");
        }
        try {
            EntityManagerUtil.begin();
            Folder parentfolder = folderDao.find(parentId);
            if (parentfolder.getParent() == null) {
                logger
                        .debug("can not create new folder under the root directory");
                throw new UBankServiceException(
                        "can not create new folder under the root directory");
            }
            Date date = new Date();

            // create new folder object and set it's value
            Folder newFolder = new Folder();
            newFolder.setParent(parentfolder);
            newFolder.setCreateTime(date);
            newFolder.setModifyTime(date);
            newFolder.setDirectory(getDiskPath(parentfolder));
            newFolder.setShare(false);
            newFolder.setUser(user);
            newFolder.setRepeatCount(0);
            newFolder.setFolderShare(parentfolder.isFolderShare());

            // If there is a same name folder in the target directory
            Folder sameOldFolder = getSameNameFolder(parentfolder, folderName);
            if (sameOldFolder != null) {
                String newName = getNewName(sameOldFolder);
                newFolder.setFolderName(newName);
            }
            else {
                newFolder.setFolderName(folderName);
            }
            // set Folder type ,default is user directory
            newFolder.setFolderType(Constant.FOLDER_TYPE_CUSTOMER);
            if (!Validity.isNullAndEmpty(folderType)) {
                newFolder.setFolderType(folderType);
            }

            // save to database
            folderDao.add(newFolder);

            // create disk file
            int result = DocumentUtil.addNewFolder(newFolder);
            if (result != 1) {
                EntityManagerUtil.rollback();
                logger.debug("Create disk directory fail.");
                return null;
            }
            EntityManagerUtil.commit();
            return newFolder;
        }
        catch (Exception e) {
            logger.error("The database throw an  exception "
                    + "when create a folder. ", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException("add new folder fail.", e);
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }
    }

    /**
     * this method get a user directory tree
     * 
     * @param userId
     *            User id identification
     * @return return this user directory tree Struct
     * @author bxji
     * @throws UBankServiceException
     *             throw Possible exception
     */
    public FolderNode getTreeRoot(Long userId) throws UBankServiceException {
        if (Validity.isNullOrZero(userId)) {
            throw new UBankServiceException("userId can not be empty");
        }
        FolderNode rootNode = null;
        try {
            EntityManagerUtil.begin();
            List<Folder> folders = folderDao.findFolderListByUserId(userId,
                    null);
            rootNode = FolderNode.generateFolderTree(folders);
            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            logger.error("When try get user directory tree "
                    + "happened to thd database an exception", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException("get user's directory tree fail.",
                    e);
        }
        finally {
            EntityManagerUtil.closeEntityManager();
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
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public List<FileOrFolderJsonEntity> getAllFromFolder(Long folderId,
            Long layer) throws UBankServiceException {
        List<FileOrFolderJsonEntity> jsonEntitys = new ArrayList<FileOrFolderJsonEntity>();
        if (Validity.isNullOrZero(folderId) || Validity.isNullOrZero(layer)) {
            return jsonEntitys;
        }
        Folder folder = null;
        try {
            EntityManagerUtil.begin();
            folder = folderDao.find(folderId);
            if (folder != null) {
                // convert the FileBean to FileOrFolderJsonEntity
                Set<FileBean> files = folder.getFiles();
                for (FileBean file : files) {
                    FileOrFolderJsonEntity jsonEntity = new FileOrFolderJsonEntity();
                    jsonEntity.setId(file.getFileId());
                    jsonEntity.setPid(folder.getFolderId());
                    jsonEntity.setName(file.getFileName());
                    jsonEntity.setSize(file.getSize()
                            + Constant.FILE_SIZE_UNIT_KB);
                    jsonEntity.setModTime(DateUtil.format(file.getModifyTime(),
                            DateUtil.YYYY_MM_DD_HH_MM_SS));
                    jsonEntity.setDir(file.getDirectory());
                    jsonEntity.setType(getDocType(file));
                    jsonEntity.setInit(false);
                    jsonEntitys.add(jsonEntity);
                    jsonEntity.setLayer(layer);
                }
                // conver sub-folders Object to FileOrFolderJsonEntity
                Set<Folder> chiFolders = folder.getChildren();
                for (Folder child : chiFolders) {
                    FileOrFolderJsonEntity jsonEntity = new FileOrFolderJsonEntity();
                    jsonEntity.setId(child.getFolderId());
                    jsonEntity.setPid(folder.getFolderId());
                    jsonEntity.setName(child.getFolderName());
                    jsonEntity.setLayer(layer);
                    jsonEntity.setModTime(DateUtil
                            .format(child.getModifyTime(),
                                    DateUtil.YYYY_MM_DD_HH_MM_SS));
                    jsonEntity.setDir(child.getDirectory());
                    jsonEntity.setType(SystemGlobals
                            .getString(ConfigKeys.DOCUMENT_TYPE_FOLDER));
                    jsonEntity.setShare(child.getShare());
                    if (Constant.FOLDER_TYPE_CUSTOMER.equals(child
                            .getFolderType())) {
                        jsonEntity.setInit(false);
                    }
                    else {
                        jsonEntity.setInit(true);
                    }
                    jsonEntitys.add(jsonEntity);
                }

            }
            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            logger.error("happened to thd database an exception", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException("get user's directory tree fail.",
                    e);
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }
        return jsonEntitys;
    }

    /**
     * this method is removed 'Folder' from the disk and database
     * 
     * @param folderId
     *            folder identification
     * @return success return true else return false * @throws
     * @throws UBankServiceException
     *             system Initial directoy can not delete exception
     * @author bxji
     */
    public boolean delFolder(Long folderId) throws UBankServiceException {
        if (Validity.isNullOrZero(folderId)) {
            logger.debug("delete fail ,target folder id can not is null.");
            return false;
        }
        try {
            EntityManagerUtil.begin();
            Folder folder = folderDao.find(folderId);
            if (!Constant.FOLDER_TYPE_CUSTOMER.equals(folder.getFolderType())) {
                logger.debug("system Initial directoy can not delete");
                throw new UBankServiceException(
                        "system Initial directoy can not delete");
            }
            folderDao.delete(folder);
            int result = DocumentUtil.removeFolder(folder);
            if (result != 1) {
                EntityManagerUtil.rollback();
                return false;
            }
            EntityManagerUtil.commit();
            return true;
        }
        catch (Exception e) {
            logger.error("delete folder failed ", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException("delete folder exception.", e);
        }
        finally {
            EntityManagerUtil.closeEntityManager();
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
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public boolean copyFolderTo(Long targetFolderId, Long sourceFolderId)
            throws UBankServiceException {
        if (Validity.isNullOrZero(targetFolderId)
                || Validity.isNullOrZero(sourceFolderId)) {
            logger.debug("Folder replication fails, "
                    + "'targetFolderId' and 'sourceFolderId' can not be null.");
            return false;
        }
        try {
            Folder source = null;
            Folder target = null;
            source = folderDao.find(sourceFolderId);
            target = folderDao.find(targetFolderId);
            boolean result = false;
            // if source folder and target folder is the same directory
            // or source is subdirectory of the target
            if (isSelfOrChild(source, target)
                    || target.equals(source.getParent())) {
                logger
                        .debug("Can not be copied to the original folder or own parent folder.");
                // throw new UBankServiceException("");
                return false;
            }
            if (target.getShare()) {
                result = copyFolder(target, source, true);
            }
            else {
                result = copyFolder(target, source, false);
            }
            return result;
        }
        catch (Exception e) {
            logger.error("delete folder exception.", e);
            throw new UBankServiceException("delete folder exception.", e);
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
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public boolean moveFolderTo(Long targetFolderId, Long sourceFolderId)
            throws UBankServiceException {
        if (Validity.isNullOrZero(targetFolderId)
                || Validity.isNullOrZero(sourceFolderId)) {
            logger.debug("Folder moved fails, "
                    + "'targetFolderId' and 'sourceFolderId' can not be null.");
            return false;
        }
        try {
            EntityManagerUtil.begin();
            Folder source = folderDao.find(sourceFolderId);
            Folder target = folderDao.find(targetFolderId);
            // if move to self and own subfolder
            if (isSelfOrChild(source, target)) {
                logger.debug("Can not move a folder to the original folder.");
                return false;
            }
            String oldDir = source.getDirectory();
            source.setParent(target);
            source.setDirectory(getDiskPath(target));
            Folder sameOldFolder = getSameNameFolder(target, source
                    .getFolderName());
            if (sameOldFolder != null) {
                String folderNewName = getNewName(sameOldFolder);
                source.setFolderName(folderNewName);
            }
            folderDao.modify(source);

            // update all files Shared state and reset disk path (directory
            // property)
            if (target.getShare()) {
                resetChildrenDiskPathAndShare(source, true);
            }
            else {
                resetChildrenDiskPathAndShare(source, false);
            }
            // Restore directory structure
            source.setDirectory(oldDir);
            int result = DocumentUtil.moveOrCopyFolderTo(source, target, true);
            if (result != 1) {
                logger.debug("move disk file fail");
                if (EntityManagerUtil.isActive()) {
                    EntityManagerUtil.rollback();
                }
                return false;
            }
            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            logger.error("When moving folder to specified directory ,"
                    + " the database throw an exception.", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException("move folder exception.", e);
        }
        finally {
            EntityManagerUtil.closeEntityManager();
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
     * @throws Exception
     *             dd
     */
    private boolean copyFolder(Folder targetFolder, Folder sourceFolder,
            boolean fShare) throws Exception {
        if (targetFolder == null || sourceFolder == null) {
            logger.debug("Folder replication fails, "
                    + "'targetFolder' and 'sourceFolder' can not be null.");
            return false;
        }
        Folder tempFolder = null;
        tempFolder = (Folder) BeanUtils.cloneBean(sourceFolder);
        // If there is a same name folder in the target directory
        Folder sameNameOldFolder = getSameNameFolder(targetFolder, tempFolder
                .getFolderName());
        if (sameNameOldFolder != null) {
            String newName = getNewName(sameNameOldFolder);
            tempFolder.setFolderName(newName);
        }
        tempFolder.setParent(targetFolder);
        tempFolder.setDirectory(getDiskPath(targetFolder));
        tempFolder.setFiles(new HashSet<FileBean>());
        tempFolder.setChildren(new HashSet<Folder>());
        tempFolder.setModifyTime(new Date());
        tempFolder.setFolderId(null);
        tempFolder.setShare(false);
        tempFolder.setFolderShare(targetFolder.isFolderShare());

        // update database
        try {
            EntityManagerUtil.begin();
            folderDao.add(tempFolder);
            int result = DocumentUtil.addNewFolder(tempFolder);
            if (result != 1) {
                EntityManagerUtil.rollback();
            }
            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            logger.error("An exception when copying a directory "
                    + "to update the database. ", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException("copy folder database exception.",
                    e);
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }

        // If there are some files in the directory,
        // copy the documents and save to database
        Set<FileBean> files = sourceFolder.getFiles();
        for (FileBean file : files) {
            try {
                EntityManagerUtil.begin();
                FileBean copy = (FileBean) BeanUtils.cloneBean(file);
                copy.setFolder(tempFolder);
                copy.setDirectory(getDiskPath(tempFolder));
                copy.setModifyTime(new Date());
                copy.setFileId(null);
                fileDao.add(copy);
                int result = DocumentUtil.moveOrCopyFileTo(file, tempFolder,
                        false, copy.getFileName());
                if (result != 1) {
                    EntityManagerUtil.rollback();
                }
                EntityManagerUtil.commit();
            }
            catch (Exception e) {
                logger.error("An exception when copying a file"
                        + " add to the database", e);
                if (EntityManagerUtil.isActive()) {
                    EntityManagerUtil.rollback();
                }
                throw new UBankServiceException(
                        "copy file database exception.", e);
            }
            finally {
                EntityManagerUtil.closeEntityManager();
            }
        }
        // If the directory has a subdirectory, copy the entire directory
        Set<Folder> folders = sourceFolder.getChildren();
        for (Folder folder : folders) {
            // Subfolders recursive copy
            copyFolder(tempFolder, folder, fShare);
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
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public boolean renameFolder(Long folderId, String newName)
            throws UBankServiceException {
        if (Validity.isNullOrZero(folderId) || Validity.isNullAndEmpty(newName)) {
            logger.debug("rename folder fail,"
                    + "target folder id can not be null"
                    + " and new name can not be null or space string");
            return false;
        }
        try {
            EntityManagerUtil.begin();
            Folder folder = folderDao.find(folderId);
            // if old name equals new name
            if (newName.equals(folder.getFolderName())) {
                return true;
            }

            // If there is a same name folder in the target directory
            Folder sameOldFolder = getSameNameFolder(folder.getParent(),
                    newName);
            if (sameOldFolder != null) {
                String name = getNewName(sameOldFolder);
                folder.setFolderName(name);
            }
            else {
                folder.setFolderName(newName);
            }
            folderDao.modify(folder);
            EntityManagerUtil.commit();
            return true;
        }
        catch (Exception e) {
            logger.error("database exception ,when folder rename .", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException(
                    "rename folder database exception.", e);
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }
    }

    /**
     * this method can Sharing a directory
     * 
     * @param folderId
     *            target folder object id
     * @return success return true else return false
     * @author bxji
     * @throws UBankServiceException
     *             root directory can not share exception
     */
    public boolean shareFolder(Long folderId) throws UBankServiceException {
        if (Validity.isNullOrZero(folderId)) {
            return false;
        }
        try {
            EntityManagerUtil.begin();
            Folder folder = folderDao.find(folderId);
            if (Constant.FOLDER_TYPE_ROOT.equals(folder.getFolderType())) {
                throw new UBankServiceException("root directory can not share.");
            }
            if (folder.getShare()) {
                return true;
            }
            folder.setShare(true);
            folderDao.updateFShareByDirectory(true, folder.getDirectory());
            folderDao.modify(folder);
            EntityManagerUtil.commit();
            return true;
        }
        catch (Exception e) {
            logger.error("When sharing a folder exception .", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException("shaer folder exception.", e);
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }

    }

    /**
     * this method is cancel share a directory operation
     * 
     * @param folderId
     *            target folder object id
     * @return success return true else return false
     * @author bxji
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public boolean cancelShareFolder(Long folderId)
            throws UBankServiceException {
        if (Validity.isNullOrZero(folderId)) {
            return false;
        }
        try {
            EntityManagerUtil.begin();
            Folder folder = folderDao.find(folderId);
            if (!folder.getShare()) {
                return true;
            }
            folder.setShare(false);
            boolean fShare = folder.getParent().isFolderShare();
            folderDao.updateFShareByDirectory(fShare, folder.getDirectory());
            folderDao.modify(folder);
            EntityManagerUtil.commit();
            return true;
        }
        catch (Exception e) {
            logger.error("When cancel share a folder "
                    + ",database throw an exception", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException("shaer folder database exception.",
                    e);
        }
        finally {
            EntityManagerUtil.closeEntityManager();
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    private void cancelShareFolder(Folder folder, boolean fShare) {
        if (folder == null) {
            return;
        }
        folder.setShare(false);
        folder.setFolderShare(fShare);
        folderDao.modify(folder);
        Set<FileBean> files = folder.getFiles();
        for (FileBean file : files) {
            file.setModifyTime(new Date());
            file.setShare(false);
            fileDao.modify(file);
        }
        Set<Folder> children = folder.getChildren();
        for (Folder child : children) {
            cancelShareFolder(child, fShare);
        }
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
        StringBuilder currentPath = new StringBuilder();
        if (parentFolder == null) {
            return currentPath.toString();
        }
        String parentPath = parentFolder.getDirectory();
        if (parentPath.endsWith(Constant.FILE_SYSTEM_SEPARATOR)) {
            currentPath.append(parentPath).append(parentFolder.getFolderId());
        }
        else {
            currentPath.append(parentPath).append(
                    Constant.FILE_SYSTEM_SEPARATOR).append(
                    parentFolder.getFolderId());
        }
        return currentPath.toString();

    }

    /**
     * Reset the subfolders and files disk path and reset all files shared
     * status
     * 
     * @param folder
     *            target directory
     * @param fShare
     *            share status
     * @return success return true else return false
     * @author bxji
     */
    private void resetChildrenDiskPathAndShare(Folder folder, boolean fShare) {
        if (folder == null) {
            return;
        }
        Date date = new Date();
        String path = getDiskPath(folder);
        Set<Folder> children = folder.getChildren();
        for (Folder child : children) {
            child.setModifyTime(date);
            child.setDirectory(path);
            child.setShare(false);
            child.setFolderShare(fShare);
            folderDao.modify(child);
            resetChildrenDiskPathAndShare(child, fShare);
        }
        Set<FileBean> files = folder.getFiles();
        for (FileBean file : files) {
            file.setModifyTime(date);
            file.setDirectory(path);
            fileDao.modify(file);
        }
    }

    /**
     * Whether the same name with the current directory folders
     * 
     * @param folder
     *            current directory
     * @param name
     *            folder name
     * @return exist same name return this folder else return null
     * @throws UBankServiceException
     *             if folder is null or name is nul throw exception
     * @author bxji
     */
    private Folder getSameNameFolder(Folder folder, String name)
            throws UBankServiceException {
        if (folder == null || Validity.isNullAndEmpty(name)) {
            logger.debug("target folder can not be null");
            throw new UBankServiceException(
                    "target folder and name can not be null.");
        }
        Set<Folder> children = folder.getChildren();
        for (Folder child : children) {
            if (name.equals(child.getFolderName())) {
                return child;
            }
        }
        return null;
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
            String typeName = name.substring(index + 1);
            if (!Validity.isNullAndEmpty(typeName)) {
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
            EntityManagerUtil.begin();
            folder = folderDao.findRootFolderByUserId(userId);
            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            logger.error("get user root folder the database exception ", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }
        return folder;
    }

    /**
     * get user's share directory root node
     * 
     * @param userId
     *            user id
     * @return root Folder node
     * @author bxji
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public FolderNode getShareTree(Long userId) throws UBankServiceException {
        if (Validity.isNullOrZero(userId)) {
            return null;
        }
        List<Folder> shares = null;
        Folder root = null;
        FolderNode shareRoot = new FolderNode();
        try {
            EntityManagerUtil.begin();
            shares = folderDao.findFolderListByUserId(userId, true);
            root = folderDao.findRootFolderByUserId(userId);

            List<FolderNode> nodes = new ArrayList<FolderNode>();
            for (Folder folder : shares) {
                FolderNode node = FolderNode.generateFolderTree(folder);
                nodes.add(node);
            }
            FolderNode.copyProperties(shareRoot, root);
            shareRoot.setSubNodes(nodes);
            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            logger.error("generate share tree exception", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            throw new UBankServiceException("generate share tree exception.", e);
        }
        return shareRoot;
    }

    /**
     * get a new name according to the original folder name
     * 
     * @param oldFile
     *            original exist folder
     * @return a new name
     * @author bxji
     */
    private String getNewName(Folder oldFolder) {
        if (oldFolder == null) {
            return null;
        }
        int repeatCount = oldFolder.getRepeatCount();
        String oldName = oldFolder.getFolderName();
        StringBuilder newName = new StringBuilder(oldName);
        newName.append("(").append(repeatCount + 1).append(")");
        oldFolder.setRepeatCount(repeatCount + 1);
        return newName.toString();
    }

    /**
     * Check if the specified folder in a given tree
     * 
     * @param root
     *            the tree root
     * @param checkObj
     *            The object being inspected
     * @return if be return 'ture' else return 'flase'
     * @author bxji
     */
    private boolean isSelfOrChild(Folder root, Folder checkObj) {
        if (root == null || checkObj == null) {
            return false;
        }
        if (checkObj.getFolderId().equals(root.getFolderId())) {
            return true;
        }
        Set<Folder> children = root.getChildren();
        for (Folder child : children) {
            if (checkObj.getFolderId().equals(child.getFolderId())) {
                return true;
            }
            return isSelfOrChild(child, checkObj);
        }
        return false;
    }

    /**
     * set fshare property of folder and it's subfolder
     * 
     * @param folder
     *            folder
     * @author bxji
     */
    @SuppressWarnings("unused")
    private void setFolderFShare(Folder folder, boolean fShare) {
        folder.setFolderShare(fShare);
        folderDao.modify(folder);
        Set<Folder> children = folder.getChildren();
        for (Folder child : children) {
            setFolderFShare(child, fShare);
        }
    }
}
