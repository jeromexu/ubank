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

    String ATTR_FILEPAGER = "filePager";
    String ATTR_FILENAME = "fileName";
    String ATTR_FILESIZE = "fileSize";
    String ATTR_PUBLISHDATE = "publishDate";

    /** charset filter config */
    String ENABLE_FILTER = "true";
    String ENABLE = "enable";
    String ENCODE = "encode";

    /** user login and logout action */
    String ACTION_LOGIN = "login";
    String ACTION_LOGOUT = "logout";
    String ACTION_HOME = "home";

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

    String HOME_PAGE = "home.jsp";
    String MAIN_PAGE = "main.jsp";
    String SEARCH_RESULT_PAGE = "searchResult.jsp";
    String ERROR_PAGE_403 = "../common/403.jsp";
    String ERROR_PAGE_404 = "../common/404.jsp";
    String ERROR_PAGE_500 = "../common/500.jsp";
    public final String REGISTER_PAGE = "register.jsp";

    // user register message
    public final String REGISTER_MSG = "register_msg";

    // user download action
    public final String ACTION_DOWNLOAD = "download";
    // user register action
    public final String ACTION_REGISTER = "register";

    short USERNAME_LENGTH = 30;
    short PASSWORD_LENGTH = 50;

    /*
     * Folder type: root--root directory; init--Initialization directoy;
     * customer--user directory
     */
    public final String FOLDER_TYPE_ROOT = "R";
    public final String FOLDER_TYPE_INIT = "I";
    public final String FOLDER_TYPE_CUSTOMER = "C";
    public final String FILE_COPY = "_copy";
    public final String FOLDER_COPY = "_copy";

    /** file search pagenum */
    String REQ_PARAM_PAGENUM = "pageNum";
    int PAGE_NUM_DEF = 1;

    /** file search constant */
    String FILE_SIZE_SEPARATOR = ",";
    String FILE_SIZE_0 = "0";
    String FILE_SIZE_1 = "1";
    String FILE_SIZE_2 = "2";
    String FILE_SIZE_3 = "3";
    String FILE_SIZE_4 = "4";
    String FILE_PUBLISHDATE_0 = "0";
    String FILE_PUBLISHDATE_1 = "1";
    String FILE_PUBLISHDATE_2 = "2";
    String FILE_PUBLISHDATE_3 = "3";
    String FILE_PUBLISHDATE_4 = "4";
    String FILE_PUBLISHDATE_5 = "5";
    String FILENAME_EMPTY = "";
    String FILENAME = "fileName";
    String MIN_FILE_SIZE = "minFileSize";
    String MAX_FILE_SIZE = "maxFileSize";
    String MIN_MODIFY_TIME = "minModifyTime";
    String MAX_MODIFY_TIME = "maxModifyTime";

    // file and folder operation action names
    public static final String SHOW_TREE = "showTree";
    public static final String SHOW_FOLDER_CONTENT = "showFolderContent";
    public static final String ADD_FOLDER = "addFolder";
    public static final String DEL_FOLDER_OR_FILE = "delFolderOrFile";
    public static final String SHARE_FOLDER = "shareFolder";
    public static final String RENAME = "rename";
    public static final String MOVE_TO = "moveTo";
    public static final String COPY_TO = "copyTo";

    // file and folder operation request parameter
    public static final String FOLDER_ID = "folderId";
    public static final String USER_ID = "userId";
    public static final String FOLDER_NAME = "folderName";
    public static final String PARENT_ID = "parentId";
    public static final String DOCUMENT_TYPE = "type";
    public static final String DOCUMENT_TYPE_FILE = "file";
    public static final String DOCUMENT_TYPE_FOLDER = "folder";
    public static final String FOLDER_OR_FILE_ID = "id";
    public static final String FOLDER_OR_FILE_NAME = "name";
    public static final String FOLDER_LAYER = "layer";

    // rquest result String
    public static final String REQUEST_RESULT_SUCCESS = "success";
    public static final String REQUEST_RESULT_FAIL = "fail";

}