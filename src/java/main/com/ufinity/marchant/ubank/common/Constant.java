package com.ufinity.marchant.ubank.common;

/**
 * Constants for ubank system
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public interface Constant {

    String ERROR_MSG = "error_msg";
    
    String ACTION_LOGIN = "login";
    String ACTION_LOGOUT = "logout";

    String REQ_PARAM_USERNAME = "username";
    String REQ_PARAM_PASSWORD = "password";

    /** the key for set session attribute */
    String SESSION_USER = "session_user"; 
    
    //userName error message
    public static final String USERNAME_ERR_MSG = "userName is not valid!";
    
    // password error message
    public static final String PASS_ERR_MSG = "password is not valid!";
    
    // repass error
    public static final String REPASS_ERR_MSG = "repass is not valid!";
    
    // default 1G space for each user
    public static final Integer  ONE_G_SPACE = 1;
    
    String HOME_PATH = "home.jsp";
    
    // captcha code error
    public static final String CAPTCHA_ERR_MSG = "captcha code is not valid!";
}