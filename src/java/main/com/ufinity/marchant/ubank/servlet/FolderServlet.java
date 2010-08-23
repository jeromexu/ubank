package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.common.FolderNode;
import com.ufinity.marchant.ubank.common.JsonUtil;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.impl.FolderDaoImpl;
import com.ufinity.marchant.ubank.service.FolderService;
import com.ufinity.marchant.ubank.service.ServiceFactory;

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
    private static final String USER_ID = "userId";

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
        String rslt = "";

        if (SHOW_MAIN.equals(method)) {
            rslt = showMain(req, resp);
        }

        if (rslt == null || rslt.equals("")) {
            rslt = "../common/404.html";
        }

        req.getRequestDispatcher(rslt).forward(req, resp);
    }

    /**
     * show main
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @return forward page
     */
    private String showMain(HttpServletRequest req, HttpServletResponse resp) {
//        String username = req.getParameter(USERNAME);

        FolderDao folderDao = new FolderDaoImpl();
        EntityManagerUtil.begin();
        // List<Folder> floders = folderDao.findAndProcessByUserName(username);

        EntityManagerUtil.commit();

        return "";
    }

    /**
     * {method description}
     * 
     * @param req request
     * @param resp response
     * @return forward page
     */
    @SuppressWarnings("unused")
    private String showTree(HttpServletRequest req, HttpServletResponse resp) {
       User user = (User)req.getSession().getAttribute("");

        String userId = req.getParameter(USER_ID);
        FolderService folderService = ServiceFactory.createService(FolderService.class);
        
        EntityManagerUtil.commit();
        FolderNode  treeRootNode = folderService.getTreeRoot(Long.parseLong(userId.trim()));
        EntityManagerUtil.commit();
        
        String treeJson = JsonUtil.bean2json(treeRootNode);
        resp.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter pw = resp.getWriter();
            resp.getWriter().write(treeJson);
            pw.flush();
        }
        catch (IOException e) {
        }

        return "";

    }

}
