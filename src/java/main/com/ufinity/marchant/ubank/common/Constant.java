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
package com.ufinity.marchant.ubank.common;

/**
 * Constants for ubank system
 * 
 * @version 1.0 - 2010-8-19
 * @author zdxue
 */
public interface Constant {

    /** error message key */
    String ATTR_ERROR_MSG = "error_msg";

    String ATTR_FILELIST = "fileList";

    /** charset filter config */
    String ENABLE_FILTER = "true";
    String ENABLE = "enable";
    String ENCODE = "encode";

    /** user login and logout action */
    String ACTION_LOGIN = "login";
    String ACTION_LOGOUT = "logout";

    /** file search action */
    String ACTION_SEARCH = "search";

    /** user login parameters */
    String REQ_PARAM_USERNAME = "userName";
    String REQ_PARAM_PASSWORD = "password";
    public final String REQ_PARAM_REPASSWORD = "repassword";
    public final String REQ_PARAM_CAPTCHACODE = "captchaCode";

    /** file search parameters */
    String REQ_PARAM_FILESIZE = "fileSize";
    String REQ_PARAM_PUBLISHDATE = "publishDate";
    String REQ_PARAM_FILENAME = "fileName";

    /** the key for set session attribute */
    String SESSION_USER = "session_user";

    // default 1G space for each user
    public static final Integer ONE_G_SPACE = 1;

    String HOME_PAGE = "home.jsp";
    String MAIN_PAGE = "main.jsp";
    String SEARCH_RESULT_PAGE = "searchResult.jsp";
    String ERROR_PAGE = "/common/404.html";
    public final String REGISTER_PAGE = "register.jsp";

    // user register error message
    public  final String  USERNAME_ERR = "userName_error_msg";
	public  final String  PASS_ERR = "pass_error_msg";
	public  final String  REPASS_ERR = "repass_error_msg";
	public  final String  CAPTCHA_ERR = "captcha_error_msg";
	public  final String  REGISTER_MSG = "register_msg";
	
	// user download action
	public final String ACTION_DOWNLOAD = "download";
    
	short USERNAME_LENGTH = 30;
	short PASSWORD_LENGTH = 50;

    /*
     * Folder type: root--root directory; 
     * init--Initialization directoy;
     * customer--user directory
     */
    public final String R = "root";
    public final String I = "init";
    public final String C = "customer";

}