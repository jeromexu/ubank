package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.impl.FolderDaoImpl;

/**
 * Folder Servlet used to operation folder
 *
 * @version 1.0 - 2010-8-19
 * @author liujun     
 */
public class FolderServlet extends AbstractServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -8297805269743197486L;
    
    private static final String SHOW_MAIN = "showMain";
    private static final String USERNAME = "username";
    
    
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
        String rslt = "";
        
        if(SHOW_MAIN.equals(method)){
            rslt = showMain(req, resp);
        }
        
        if(rslt == null || rslt.equals("")){
            rslt = "../common/404.html";
        }
        
        req.getRequestDispatcher(rslt).forward(req, resp);
    }
    
    /**
     * show main   
     *
     * @param req request
     * @param resp response
     * @return forward page
     */
    private String showMain(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter(USERNAME);
        
        FolderDao folderDao = new FolderDaoImpl();
        EntityManagerUtil.begin();
        //List<Folder> floders = folderDao.findAndProcessByUserName(username);
        
        
        EntityManagerUtil.commit();
        
        
        return "";
    }
    
}

