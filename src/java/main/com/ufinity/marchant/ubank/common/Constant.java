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
    
    /** servlet filter config */
    String ENABLE_FILTER = "true"; 
    String ENABLE = "enable";
    String ENCODE = "encode";
    
    /** user login and logout action */
    String ACTION_LOGIN = "login";
    String ACTION_LOGOUT = "logout";
    
    /** file search action */
    String ACTION_SEARCH = "search";

    /** user login parameters */
    String REQ_PARAM_USERNAME = "username";
    String REQ_PARAM_PASSWORD = "password";
    
    /** file search parameters */
    String REQ_PARAM_FILESIZE = "fileSize";
    String REQ_PARAM_PUBLISHDATE = "publishDate";
    String REQ_PARAM_FILENAME = "fileName";

    /** the key for set session attribute */
    String SESSION_USER = "session_user"; 
    
    // default 1G space for each user
    public static final Integer  ONE_G_SPACE = 1;
    
    String HOME_PAGE = "home.jsp";
    String MAIN_PAGE = "main.jsp";
    String SEARCH_RESULT_PAGE = "searchResult.jsp";
    String ERROR_PAGE = "/common/404.html";
    
    // user register error message
    public  final String  USERNAME_ERR = "userName_error_msg";
	public  final String  PASS_ERR = "pass_error_msg";
	public  final String  REPASS_ERR = "repass_error_msg";
	public  final String  CAPTCHA_ERR = "captcha_error_msg";
	public  final String  REGISTER_MSG = "register_msg";
    
   
}