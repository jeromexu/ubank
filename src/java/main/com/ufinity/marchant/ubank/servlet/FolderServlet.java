package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.common.FileOrFolderJsonEntity;
import com.ufinity.marchant.ubank.common.FolderNode;
import com.ufinity.marchant.ubank.common.JsonNode;
import com.ufinity.marchant.ubank.common.JsonUtil;
import com.ufinity.marchant.ubank.common.NodeUtil;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.impl.FolderDaoImpl;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.service.FolderService;
import com.ufinity.marchant.ubank.service.ServiceFactory;
import com.ufinity.marchant.ubank.service.impl.FolderServiceImpl;

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
    // Logger for this class
    protected final Logger logger = Logger.getLogger(FolderServlet.class);

    private static final String SHOW_MAIN = "showMain";
    private static final String SHOW_TREE = "showTree";
    private static final String USERNAME = "username";

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

        String method = req.getParameter("method");
        String rslt = Constant.ERROR_PAGE;

        if (SHOW_MAIN.equals(method)) {
            rslt = showMain(req, resp);
        }
        else if (SHOW_TREE.equals(method)) {
            showTree(req, resp);
            return;
        }

        forward(req, resp, rslt);
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
        // String username = req.getParameter(USERNAME);

        FolderDao folderDao = new FolderDaoImpl();
        EntityManagerUtil.begin();
        // List<Folder> floders = folderDao.findAndProcessByUserName(username);

        EntityManagerUtil.commit();

        return "";
    }

    /**
     * {method description}
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @return forward page
     * @author bxji
     */
    private void showTree(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute(Constant.SESSION_USER);
        FolderNode treeRootNode = null;

        if (user != null) {
            FolderService folderService = ServiceFactory
                    .createService(FolderService.class);
            EntityManagerUtil.begin();
            try {
                treeRootNode = folderService.getTreeRoot(user.getUserId());
            }
            catch (UBankException e) {
                logger.debug("when try get user root directory, "
                        + "throw an exception:user id can not be null", e);
            }
            EntityManagerUtil.commit();
        }

        String treeJson = "";
        JsonNode jsonNode = NodeUtil.copyFolderNodeToJsonNode(treeRootNode);

        if (treeRootNode != null) {
            treeJson = JsonUtil.bean2json(jsonNode);
        }
        resp.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter pw = resp.getWriter();
            resp.getWriter().write("[" + treeJson + "]");
            pw.flush();
        }
        catch (IOException e) {
        }
    }

    /**
     * Respond to requests for users to view directory list of files
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @return forward page
     * @author bxji
     */
    private void showFolderContent(HttpServletRequest req,
            HttpServletResponse resp) {
        String id = req.getParameter("folderId");
        Long folderId = null;
        if (!Validity.isNullAndEmpty(id)) {
            folderId = Long.parseLong(id);
        }
        FolderService folderService = ServiceFactory
                .createService(FolderService.class);
        List<FileOrFolderJsonEntity> josnEntitys = null;
        try {
            josnEntitys = folderService.getAllByFolder(folderId);
        }
        catch (UBankException e) {
            logger.debug("floder id can not be null", e);
        }
        String jsonStr = "";
        if (josnEntitys != null) {
            jsonStr = JsonUtil.object2json(josnEntitys);
        }
        resp.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter pw = resp.getWriter();
            resp.getWriter().write(jsonStr);
            pw.flush();
        }
        catch (IOException e) {
        }

    }

}
