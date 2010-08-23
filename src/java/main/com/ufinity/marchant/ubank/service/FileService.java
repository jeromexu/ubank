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

import java.util.List;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.common.Pager;

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
     * @author zdxue
     */
    public Pager<FileBean> searchShareFiles(String fileName, String fileSize,
            String publishDate, int pageNum, int pageSize);
    
    /**
     * get all files under specified directory
     * 
     * @param folderId folder directory ID
     * @return files list
     * @author bxji
     */
    public List<FileBean> getFilesByFolder(Long folderId);
    
	/**
	 * 
	 * download file by fileId
	 * 
	 * @param fileId the Id of the file
	 * @return success or failure
	 * @author jerome
	 */
	public Integer downloadFile(Long fileId);
}
