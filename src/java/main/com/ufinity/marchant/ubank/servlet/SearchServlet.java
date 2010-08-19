package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.common.Constant;

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
        
        return Constant.SEARCH_RESULT_PAGE;
    }
    
}

