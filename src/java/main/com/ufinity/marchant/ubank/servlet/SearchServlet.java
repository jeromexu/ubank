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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.bean.File;
import com.ufinity.marchant.ubank.common.Constant;
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
        
        if(Constant.ACTION_SEARCH.equals(method)) {
            rslt = search(req);
        }
        
        forward(req, resp, rslt);
    }

    /**
     * Process file search 
     *
     * @param req request
     * @return forward page
     * @author zdxue
     */
    private String search(HttpServletRequest req) {
        String fileName = req.getParameter(Constant.REQ_PARAM_FILENAME);
        String fileSize = req.getParameter(Constant.REQ_PARAM_FILESIZE);
        String publishDate = req.getParameter(Constant.REQ_PARAM_PUBLISHDATE);

        if(fileName == null) {
            fileName = "";
        }
        
        if(fileSize == null) {
            fileSize = "0";
        }
        
        if(publishDate == null) {
            publishDate = "0";
        }

        System.out.println("fileName=" + fileName + " , fileSize=" + fileSize + " , publishDate=" + publishDate);
        //TODO
        FileService fileService = ServiceFactory.createService(FileService.class);
        fileService.searchShareFiles(fileName, fileSize, publishDate);
        
        List<File> fileList = null;
        
        req.setAttribute(Constant.ATTR_FILELIST, fileList);
        
        return Constant.SEARCH_RESULT_PAGE;
    }
    
}

