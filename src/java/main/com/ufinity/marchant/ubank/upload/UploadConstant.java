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
package com.ufinity.marchant.ubank.upload;

/**
 * Constants for ubank system
 * 
 * @version 1.0 - 2010-8-20
 * @author liujun
 */
public interface UploadConstant {
    
    //servlet method
    String UPLOAD_METHOD = "upload";
    String CONTINUE_UOLOAD_METHOD = "continueUpload";
    String GET_INFO_METHOD = "getInfo";
    String PAUSE_METHOD= "pause";
    String SET_CURRENT_FOLDER_METHOD = "setCurrentFolder";
    
    //upload max size 10M
    long MAX_LENGTH = 10 * 1024 * 1024L;
    //1KB
    int HTTP_REDUNDANT_LENGTH = 1024;
    
    String CONTENT_TYPE = "application/json;charset=UTF-8";
    
    //param
    String PROGRESS_INFO = "progressInfo";
    String HEADER_ENCODE ="UTF-8";
    String CURRENT_FOLDER_ID = "currentFolderId";
    String FILED_NAME = "filedName";
    
    //global
    String UBANK_PATH = "ubank.path";
    
    //updates info every 10kb
    long UPDATE_THRESHOLD = 10 * 1024L;
    
    //default add point
    int UPLOAD_DEFAULT_POINT = 2;
}