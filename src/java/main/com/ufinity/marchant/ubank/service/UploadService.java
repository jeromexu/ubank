// -------------------------------------------------------------------------
// Copyright (c) 2000-2009 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
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

import org.apache.commons.fileupload.FileItemStream;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.exception.UBankServiceException;
import com.ufinity.marchant.ubank.upload.ProgressInfo;

/**
 * Upload Service
 * 
 * @version 1.0 - 2010-8-23
 * @author liujun
 */
public interface UploadService {

    /**
     * upload file and save to db
     * 
     * @param folder
     *            current folder 
     * @param pi
     *            info of upload
     * @param item
     *            the FileItemStream
     * @throws UBankServiceException
     *             if have UBankServiceException
     */
    public void uploadAndSaveDb(Folder folder, ProgressInfo pi, FileItemStream item) throws UBankServiceException;

    /**
     * get folder dir with id
     * 
     * @param folderId
     *            folder id
     * @return Folder folder
     * @throws UBankServiceException
     *             if db have UBankServiceException
     */
    public Folder getFolder(Long folderId) throws UBankServiceException;
    
    /**
     * get all file size by user id
     * 
     * @param userId
     *            user id
     * @throws UBankServiceException
     *             if has UBankServiceException
     * @return total size
     */
    public long getTotalFileSize(Long userId) throws UBankServiceException;
    
    /**
     * user add point
     * 
     * @param userId
     *            user id
     */
    public void addPoint(Long userId);
}
