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
package com.ufinity.marchant.ubank.service;

import java.util.List;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.common.FileOrFolderJsonEntity;
import com.ufinity.marchant.ubank.common.FolderNode;
import com.ufinity.marchant.ubank.exception.UBankException;

/**
 * {description of method or object}
 * 
 * @author Administrator
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
     */
    public FolderNode getTreeRoot(Long userId) throws UBankException;

    /**
     * Get all files and sub-folders under specified directory,return
     * FileOrFolderJsonEntity class list {method description}
     * 
     * @param folderId
     *            specified folder id
     * @return FileOrFolderJsonEntity list
     * @author bxji
     */
    public List<FileOrFolderJsonEntity> getAllByFolder(Long folderId);

    /**
     * this method is removed 'Folder' from the disk and database
     * 
     * @param folderId
     *            folder identification
     * @return success return true else return false
     * @author bxji
     */
    public boolean delFolder(Long folderId);
    

}
