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

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.exception.UBankServiceException;
import com.ufinity.marchant.ubank.model.Pager;
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

    private final Logger logger = Logger.getInstance(LoginServlet.class);

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
        String rslt = Constant.ERROR_PAGE_404;
        logger.debug("action method=" + method);

        try {
            if (Constant.ACTION_HOME.equals(method)) {
                rslt = home(req, resp);
            } else if (Constant.ACTION_LOGIN.equals(method)) {
                rslt = login(req, resp);
            } else if (Constant.ACTION_LOGOUT.equals(method)) {
                rslt = logout(req, resp);
            }
        } catch (UBankException e) {
            logger.error("occur UBankException.", e);
            redirect(resp, Constant.ERROR_PAGE_500);
            return;
        }

        logger.debug("go page=" + rslt);
        if (Constant.MAIN_PAGE.equals(rslt)) {
            redirect(resp, rslt);
            return;
        }

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
     * @throws UBankException
     */
    private String home(HttpServletRequest req, HttpServletResponse resp)
            throws UBankException {

        FileService fileService = ServiceFactory.createService(FileService.class);

        Pager<FileBean> filePager = null;
        try {
            int pageSize = SystemGlobals.getInt(ConfigKeys.PAGE_SIZE);
            filePager = fileService.searchShareFiles(Constant.FILENAME_EMPTY,
                    Constant.FILE_SIZE_0, Constant.FILE_PUBLISHDATE_0,
                    Constant.PAGE_NUM_DEF, pageSize);
        } catch (UBankServiceException e) {
            logger.error("go home page exception", e);
            throw new UBankException("go home page exception");
        }

        req.setAttribute(Constant.ATTR_FILEPAGER, filePager);
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
     * @throws UBankException
     */
    private String login(HttpServletRequest req, HttpServletResponse resp)
            throws UBankException {
        String eventPath = req.getParameter(Constant.REQ_PARAM_EVENTPATH);
        String username = req.getParameter(Constant.REQ_PARAM_USERNAME);
        String password = req.getParameter(Constant.REQ_PARAM_PASSWORD);

        logger.debug("username=" + username + " , password=" + password
                + ", eventPath=" + eventPath);

        // If validate failure, then return login page.
        if (Validity.isEmpty(username)
                || username.length() > Constant.USERNAME_LENGTH
                || Validity.isEmpty(password)
                || password.length() > Constant.PASSWORD_LENGTH) {

            logger.debug("login validation failure!");
            req.setAttribute(Constant.ATTR_ERROR_MSG,
                    getText(MessageKeys.MSG_LOGIN_FAILURE));
            return Constant.LOGIN_PAGE;
        }

        UserService userService = ServiceFactory
                .createService(UserService.class);
        User user = null;
        try {
            user = userService.getUser(username, password);
            logger.debug("get user = " + user);
        } catch (UBankServiceException e) {
            logger.error("login exception", e);
            throw new UBankException("login exception");
        }

        if (user == null) {
            logger.debug("user not exists, login failure");
            req.setAttribute(Constant.ATTR_ERROR_MSG,
                    getText(MessageKeys.MSG_LOGIN_FAILURE));
            if (Validity.isNotEmpty(eventPath)) {
                req.setAttribute(Constant.ATTR_EVENTPATH, eventPath);
            }
            return Constant.LOGIN_PAGE;
        } else {
            logger.debug("login success");
            req.getSession().setAttribute(Constant.SESSION_USER, user);

            if (Validity.isNotEmpty(eventPath)) {
                return eventPath;
            }
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
        logger.debug("invalidate session");

        req.getSession().invalidate();

        return Constant.LOGIN_PAGE;
    }
}
