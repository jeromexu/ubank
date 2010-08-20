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
            req.setAttribute(Constant.ATTR_ERROR_MSG, MessageResource.getMessage(MessageKeys.MSG_LOGIN_FAILURE));
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

