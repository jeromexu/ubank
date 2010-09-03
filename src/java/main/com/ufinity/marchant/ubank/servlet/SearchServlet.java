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
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.Pager;
import com.ufinity.marchant.ubank.common.StringUtil;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.service.FileService;
import com.ufinity.marchant.ubank.service.ServiceFactory;

/**
 * Search Servlet
 * 
 * @version 1.0 - 2010-8-19
 * @author zdxue
 */
@SuppressWarnings("serial")
public class SearchServlet extends AbstractServlet {
    private final Logger logger = Logger.getInstance(SearchServlet.class);

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
        
        logger.debug("method=" + method + " , rslt=" + rslt);

        try{
            if (Constant.ACTION_SEARCH.equals(method)) {
                rslt = search(req);
            }
        }catch(UBankException e) {
            logger.error("occur UBankException.", e);
            redirect(resp, Constant.ERROR_PAGE_500);
            return;
        }

        logger.debug("rslt=" + rslt);
        
        forward(req, resp, rslt);
    }

    /**
     * Process file search
     * 
     * @param req
     *            request
     * @return forward page
     * @author zdxue
     * @throws UBankException 
     */
    private String search(HttpServletRequest req) throws UBankException {
        String fileName = req.getParameter(Constant.REQ_PARAM_FILENAME);
        String fileSize = req.getParameter(Constant.REQ_PARAM_FILESIZE);
        String publishDate = req.getParameter(Constant.REQ_PARAM_PUBLISHDATE);
        String pageNumber = req.getParameter(Constant.REQ_PARAM_PAGENUM);
        logger.debug("fileName=" + fileName + " , fileSize=" + fileSize
                + " , publishDate=" + publishDate + " , pageNumber="
                + pageNumber);

        int pageNum = StringUtil.parseInt(pageNumber, Constant.PAGE_NUM_DEF);

        logger.debug("pageNum=" + pageNum);

        FileService fileService = ServiceFactory
                .createService(FileService.class);

        int pageSize = SystemGlobals.getInt(ConfigKeys.PAGE_SIZE);
        Pager<FileBean> filePager = fileService.searchShareFiles(fileName, fileSize,
                    publishDate, pageNum, pageSize);
        
        req.setAttribute(Constant.ATTR_FILEPAGER, filePager);
        req.setAttribute(Constant.ATTR_FILENAME, fileName);
        req.setAttribute(Constant.ATTR_FILESIZE, fileSize);
        req.setAttribute(Constant.ATTR_PUBLISHDATE, publishDate);

        return Constant.SEARCH_RESULT_PAGE;
    }

}
