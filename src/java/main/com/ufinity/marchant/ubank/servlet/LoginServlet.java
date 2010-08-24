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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.Pager;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageResource;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.service.FileService;
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

    private final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String method = parseActionName(req);
        String rslt = Constant.ERROR_PAGE;

        LOG.debug("action method=" + method);

        if (Constant.ACTION_HOME.equals(method)) {
            rslt = home(req, resp);
        } else if (Constant.ACTION_LOGIN.equals(method)) {
            rslt = login(req, resp);
        } else if (Constant.ACTION_LOGOUT.equals(method)) {
            rslt = logout(req, resp);
        }

        LOG.debug("go page=" + rslt);

        forward(req, resp, rslt);
    }

    /**
     * Go home page
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @return forward page
     * @author zdxue
     */
    private String home(HttpServletRequest req, HttpServletResponse resp) {
        FileService fileService = ServiceFactory
                .createService(FileService.class);
        int pageSize = SystemGlobals.getInt(ConfigKeys.PAGE_SIZE);
        Pager<FileBean> filePager = fileService.searchShareFiles(
                Constant.FILENAME_EMPTY, Constant.FILE_SIZE_0,
                Constant.FILE_PUBLISHDATE_0, Constant.PAGE_NUM_DEF, pageSize);
        req.setAttribute(Constant.ATTR_FILEPAGER, filePager);
        req.setAttribute(Constant.ATTR_FILENAME, Constant.FILENAME_EMPTY);
        req.setAttribute(Constant.ATTR_FILESIZE, Constant.FILE_SIZE_0);
        req
                .setAttribute(Constant.ATTR_PUBLISHDATE,
                        Constant.FILE_PUBLISHDATE_0);
        return Constant.HOME_PAGE;
    }

    /**
     * Process Login
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @return forward page
     * @author zdxue
     */
    private String login(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter(Constant.REQ_PARAM_USERNAME);
        String password = req.getParameter(Constant.REQ_PARAM_PASSWORD);

        LOG.debug("username=" + username + " , password=" + password);

        // If validate failure, then return.
        if (Validity.isEmpty(username)
                || username.length() > Constant.USERNAME_LENGTH
                || Validity.isEmpty(password)
                || password.length() > Constant.PASSWORD_LENGTH) {
            LOG.debug("login validation failure!");

            req.setAttribute(Constant.ATTR_ERROR_MSG, MessageResource
                    .getMessage(MessageKeys.MSG_LOGIN_FAILURE));
            return Constant.HOME_PAGE;
        }

        UserService userService = ServiceFactory
                .createService(UserService.class);
        User user = userService.getUser(username, password);

        if (user == null) {
            LOG.debug("user not exists, login failure");
            req.setAttribute(Constant.ATTR_ERROR_MSG, MessageResource
                    .getMessage(MessageKeys.MSG_LOGIN_FAILURE));
            home(req, resp);
            
            return Constant.HOME_PAGE;
        } else {
            LOG.debug("login success");
            req.getSession().setAttribute(Constant.SESSION_USER, user);
            return Constant.MAIN_PAGE;
        }
    }

    /**
     * Process Logout
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @return forward page
     * @author zdxue
     */
    private String logout(HttpServletRequest req, HttpServletResponse resp) {
        LOG.debug("invalidate session");

        req.getSession().invalidate();

        home(req, resp);

        return Constant.HOME_PAGE;
    }
}
