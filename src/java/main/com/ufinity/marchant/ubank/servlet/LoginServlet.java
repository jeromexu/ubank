package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
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

    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    
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
        
        //System.out.println("Coming ...");
        
        String method = parseActionName(req);
        String rslt = "";
        
        //System.out.println("method=" + method);
        if(LOGIN.equals(method)){
            rslt = login(req, resp);
        }else if(LOGOUT.equals(method)) {
            rslt = logout(req, resp);
        }
        
        req.getRequestDispatcher(rslt).forward(req, resp);
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
        String username = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);
        
        UserService userService = ServiceFactory.getInstance().getUserService();
        User user = userService.getUser(username, password);
        
        if(user == null) {
            req.setAttribute("error_msg", "username or password error.");
        }else{
            req.getSession().setAttribute(Constant.SESSION_USER, user);
            return "profile";            
        }
        
        return "home.jsp";
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
        return null;
    }
}

