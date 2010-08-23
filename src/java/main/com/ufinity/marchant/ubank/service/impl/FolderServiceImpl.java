// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:bixiang Ji
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
import java.util.List;
import java.util.Set;

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

    // Logger for this class
    protected final Logger logger = Logger.getLogger(FolderServiceImpl.class);

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
        if (userId == null || new Long(0).equals(userId)) {
            throw new UBankException("userId can not be empty");
        }
        if (parentId == null || new Long(0).equals(userId)
                || Validity.isNullAndEmpty(folderName)) {
            throw new UBankException(
                    "parentId can not be null and folderName can not be null or space String");
        }

        UserDao userDao = DaoFactory.createDao(UserDao.class);
        User user = userDao.find(userId);

        FolderDao folderDao = DaoFactory.createDao(FolderDao.class);
        Folder parentfolder = folderDao.find(parentId);

        // create new foler object and set value
        Folder newFolder = new Folder();
        newFolder.setParent(parentfolder);
        newFolder.setFolderName(folderName);
        newFolder.setCreateTime(new Date());
        newFolder.setDirectory("-------");
        newFolder.setFolderType(Constant.C);
        if (!Validity.isNullAndEmpty(FolderType)) {
            newFolder.setFolderType(FolderType);
        }
        newFolder.setShare(false);
        newFolder.setUser(user);
        // create disk file
        DocumentUtil.addNewFolder(newFolder);
        // save to database
        folderDao.add(newFolder);

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
        if (userId == null || new Long(0).equals(userId)) {
            throw new UBankException("userId can not be empty");
        }
        FolderDao folderDao = DaoFactory.createDao(FolderDao.class);
        List<Folder> floders = folderDao.findFolderListByUserId(userId);
        FolderNode rootNode = FolderNode.generateFolderTree(floders);
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
     * @throws UBankException
     *             throw Possible exception
     */
    public List<FileOrFolderJsonEntity> getAllByFolder(Long folderId)
            throws UBankException {
        if (folderId == null || new Long(0).equals(folderId)) {
            throw new UBankException("folderId  can not be empty");
        }
        FolderDao folderDao = DaoFactory.createDao(FolderDao.class);
        Folder folder = folderDao.find(folderId);
        Set<FileBean> files = folder.getFiles();
        Set<Folder> chiFolders = folder.getChildren();

        List<FileOrFolderJsonEntity> jsonEntieys = new ArrayList<FileOrFolderJsonEntity>();

        // convert the FileBean to FileOrFolderJsonEntity
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

        // conver sub-folders Object to FileOrFolderJsonEntity
        for (Folder child : chiFolders) {
            FileOrFolderJsonEntity jsonEntity = new FileOrFolderJsonEntity();
            jsonEntity.setId(child.getFolderId());
            jsonEntity.setName(child.getFolderName());
            jsonEntity.setModifyTime(child.getModifyTime());
            jsonEntity.setDirectory(child.getDirectory());
            jsonEntity.setType(FileOrFolderJsonEntity.TYPE_FOLDER);
            jsonEntieys.add(jsonEntity);
        }

        return jsonEntieys;
    }

}
