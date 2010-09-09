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

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.exception.UBankServiceException;
import com.ufinity.marchant.ubank.vo.DownloadResponse;
import com.ufinity.marchant.ubank.vo.Pager;

/**
 * File Service
 * 
 * @version 1.0 - 2010-8-19
 * @author zdxue
 */
public interface FileService {

    /**
     * Search share files
     * 
     * @param fileName
     *            file name
     * @param fileSize
     *            file size flag
     * @param publishDate
     *            publish date flag
     * @param pageNum
     *            pageNum
     * @param pageSize
     *            pageSize
     * @return file pager obj
     * @throws UBankServiceException
     *             if occur exception, throw it
     * @author zdxue
     */
    public Pager<FileBean> searchShareFiles(String fileName, String fileSize,
            String publishDate, int pageNum, int pageSize)
            throws UBankServiceException;

    /**
     * download file by id
     * 
     * @param fileId
     *            the id of the file
     * @param user
     *            who will download
     * @return the response obj of download operation
     * @throws UBankServiceException
     *             the exception which do not get the file
     * @author jerome
     * @author modify by zdxue - refact the return type and method name
     */
    public DownloadResponse download(Long fileId, User user)
            throws UBankServiceException;

    /**
     * Copy file to the specified directory
     * 
     * @param targetFolderId
     *            target FolderId
     * @param sourceFileId
     *            source FileId
     * @return success return 'true' else return 'false'
     * @author bxji
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public boolean copyFileToFolder(Long targetFolderId, Long sourceFileId)
            throws UBankServiceException;

    /**
     * Move file to specified directory
     * 
     * @param targetFolderId
     *            target FolderId
     * @param sourceFileId
     *            sourceFileId
     * @return success return 'true' else return 'false'
     * @author bxji
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public boolean moveFileToFloder(Long targetFolderId, Long sourceFileId)
            throws UBankServiceException;

    /**
     * delete a file from database and disk
     * 
     * @param fileId
     *            file id
     * @return success return 'true' else return 'false'
     * @author bxji
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public boolean removeFile(Long fileId) throws UBankServiceException;

    /**
     * Rename files
     * 
     * @param fileId
     *            file id
     * @param newName
     *            new name
     * @return success return 'true' else return 'false'
     * @author bxji
     * @throws UBankServiceException
     *             UBank Service Exception
     */
    public boolean renameFile(Long fileId, String newName)
            throws UBankServiceException;

    /**
     * get file
     * 
     * @param fileId
     *            file id
     * @return FileBean
     * @throws UBankServiceException
     *             occur exception, throw it
     * @author zdxue
     */
    public FileBean getFile(long fileId) throws UBankServiceException;

}
