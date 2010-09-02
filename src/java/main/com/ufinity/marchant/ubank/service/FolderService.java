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
package com.ufinity.marchant.ubank.service;

import java.util.List;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.common.FileOrFolderJsonEntity;
import com.ufinity.marchant.ubank.common.FolderNode;
import com.ufinity.marchant.ubank.exception.UBankException;

/**
 * {description of method or object}
 * 
 * @author bxji
 * @version 2010-8-20
 */
public interface FolderService {

    /**
     * create a new folder on disk and save to database
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
     * @author bxji
     */
    public Folder addFolder(Long userId, Long parentId, String folderName,
            String FolderType) throws UBankException;

    /**
     * this method get a user directory tree
     * 
     * @param userId
     *            User id identification
     * @return return this user directory tree Struct
     * @throws UBankException
     *             throw Possible exception
     * @author bxji
     */
    public FolderNode getTreeRoot(Long userId) throws UBankException;

    /**
     * Get all files and sub-folders under specified directory,return
     * FileOrFolderJsonEntity class list {method description}
     * 
     * @param folderId
     *            specified folder id
     * @param layer
     *            current directory layer number
     * @return FileOrFolderJsonEntity list
     * @author bxji
     */
    public List<FileOrFolderJsonEntity> getAllFromFolder(Long folderId,
            Long layer);

    /**
     * this method is removed 'Folder' from the disk and database
     * 
     * @param folderId
     *            folder identification
     * @return success return true else return false
     * @author bxji
     */
    public boolean delFolder(Long folderId);

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
    public boolean moveFolderTo(Long targetFolderId, Long sourceFolderId);

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
    public boolean copyFolderTo(Long targetFolderId, Long sourceFolderId);

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
    public boolean renameFolder(Long folderId, String newName);

    /**
     * this method can Sharing a directory
     * 
     * @param folderId
     *            target folder object id
     * @return success return true else return false
     * @author bxji
     */
    public boolean shareFolder(Long folderId);

    /**
     * this method is cancel share a directory operation
     * 
     * @param folderId
     *            target folder object id
     * @return success return true else return false
     * @author bxji
     */
    public boolean cancelShareFolder(Long folderId);

    /**
     * get user root directory
     * 
     * @param userId
     *            user id
     * @return return user root directory
     * @author bxji
     */
    public Folder getRootFolder(Long userId);

    /**
     * get user's share directory root node
     * 
     * @param userId
     *            user id
     * @return root Folder node
     * @author bxji
     */
    public FolderNode getShareTree(Long userId);

}
