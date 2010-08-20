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

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.DocumentUtil;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.service.FolderNode;
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
     *  create a new folder on disk and save to database
     * 
     * @param folderName
     *            folderName
     * @param FolderType
     *            default folder for the user to add
     * @param parentId
     *            parent foler 'folderId'
     * @param userId
     *            user ID identification
     * @return return a folder object
     * @throws UBankException
     *             throw Possible exception
     */
    @Override
    public Folder addFolder(Long userId, Long parentId, String folderName,
            String FolderType) throws UBankException {
        if (parentId == null || folderName == null
                || "".equals(folderName.trim())) {
            logger.debug("do not add a folder of parent folder is null");
            logger.debug("folder name can not is null or space string");
            throw new UBankException(
                    "parentId can not is null and folderName can not null or space String");
        }

        UserDao userDao = DaoFactory.createDao(UserDao.class);
        User user = userDao.find(userId);

        FolderDao folderDao = DaoFactory.createDao(FolderDao.class);
        Folder parentfolder = folderDao.find(parentId);

        Folder newFolder = new Folder();

        newFolder.setParent(parentfolder);
        newFolder.setFolderName(folderName);
        newFolder.setCreateTime(new Date());
        newFolder.setDirectory("-------");
        newFolder.setFolderType(Constant.C);
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
     */
    @Override
    public FolderNode getTreeRoot(Long userId) {
        FolderDao folderDao = DaoFactory.createDao(FolderDao.class);
        List<Folder> floders = folderDao.findFolderListByUserId(userId);
        FolderNode rootNode = FolderNode.generateFolderTree(floders);
        return rootNode;
    }

}
