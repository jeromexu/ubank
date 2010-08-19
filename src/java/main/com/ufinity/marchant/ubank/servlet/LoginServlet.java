package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageResource;
import com.ufinity.marchant.ubank.service.ServiceFactory;
import com.ufinity.marchant.ubank.service.UserService;

/**
 * Login Servlet used to process users' login or logout.
 *
 * @version 1.0 - 2010-8-18
 * @author zdxue     
 */
@SuppressWarnings("serial")
public class LoginServlet extends AbstractServlet {

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String method = parseActionName(req);
        String rslt = Constant.ERROR_PAGE;
        
        if(Constant.ACTION_LOGIN.equals(method)){
            rslt = login(req, resp);
        }else if(Constant.ACTION_LOGOUT.equals(method)) {
            rslt = logout(req, resp);
        }
        
        forward(req, resp, rslt);
    }
    
    /**
     * Process Login  
     *
     * @param req request
     * @param resp response
     * @return forward page
     * @author zdxue
     */
    private String login(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter(Constant.REQ_PARAM_USERNAME);
        String password = req.getParameter(Constant.REQ_PARAM_PASSWORD);
        
        if(username == null) {
            username = "";
        }
        
        if(password == null) {
            password = "";
        }
        
        UserService userService = ServiceFactory.getInstance().getUserService();
        User user = userService.getUser(username, password);
        
        if(user == null) {
            req.setAttribute(Constant.ERROR_MSG, MessageResource.getMessage(MessageKeys.MSG_LOGIN_FAILURE));
        }else{
            req.getSession().setAttribute(Constant.SESSION_USER, user);
            return Constant.MAIN_PAGE;         
        }
        
        return Constant.HOME_PAGE;
    }
    
    /**
     * Process Logout  
     *
     * @param req request
     * @param resp response
     * @return forward page
     * @author zdxue
     */
    private String logout(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();
        return Constant.HOME_PAGE;
    }
}

