package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.common.FileOrFolderJsonEntity;
import com.ufinity.marchant.ubank.common.FolderNode;
import com.ufinity.marchant.ubank.common.JsonNode;
import com.ufinity.marchant.ubank.common.JsonUtil;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.NodeUtil;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.service.FileService;
import com.ufinity.marchant.ubank.service.FolderService;
import com.ufinity.marchant.ubank.service.ServiceFactory;

/**
 * Folder Servlet used to operation folder
 * 
 * @version 1.0 - 2010-8-19
 * @author liujun
 */
public class FolderServlet extends AbstractServlet {
    private static final long serialVersionUID = -8297805269743197486L;
    // Logger for this class
    protected final Logger LOG = Logger.getInstance(FolderServlet.class);

    FolderService folderService = null;
    FileService fileService = null;

    private static final String SHOW_MAIN = "showMain";

    /**
     * Constructor for FolderServlet
     */
    public FolderServlet() {
        folderService = ServiceFactory.createService(FolderService.class);
        fileService = ServiceFactory.createService(FileService.class);
    }

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
        String rslt = Constant.ERROR_PAGE_500;

        if (SHOW_MAIN.equals(method)) {
            rslt = showMain(req, resp);
        }
        else if (Constant.SHOW_TREE.equals(method)) {
            showTree(req, resp);
            return;
        }
        else if (Constant.SHOW_FOLDER_CONTENT.equals(method)) {
            showFolderContent(req, resp);
            return;
        }
        else if (Constant.ADD_FOLDER.equals(method)) {
            addFolder(req, resp);
            return;
        }
        else if (Constant.DEL_FOLDER_OR_FILE.equals(method)) {
            delFolderOrfile(req, resp);
            return;
        }
        else if (Constant.SHARE_FOLDER.equals(method)) {
            sharefolder(req, resp);
            return;
        }
        else if (Constant.RENAME.equals(method)) {
            rename(req, resp);
            return;
        }
        else if (Constant.MOVE_TO.equals(method)) {
            moveTo(req, resp);
            return;
        }
        else if (Constant.COPY_TO.equals(method)) {
            copyTo(req, resp);
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
        if (user == null) {
            return;
        }
        FolderNode treeRootNode = null;
        try {
            treeRootNode = folderService.getTreeRoot(user.getUserId());
        }
        catch (UBankException e) {
            LOG.error("when try get user root directory, "
                    + "throw an exception:user id can not be null", e);
        }
        if (treeRootNode == null) {
            return;
        }

        treeRootNode.setFolderName(SystemGlobals
                .getString(ConfigKeys.ROOT_NAME));
        JsonNode jsonNode = NodeUtil.copyFolderNodeToJsonNode(treeRootNode);
        String treeJson = JsonUtil.bean2json(jsonNode);
        resp.setContentType("application/json;charset=UTF-8");
        System.out.println(treeJson);
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
     * @author bxji
     */
    private void showFolderContent(HttpServletRequest req,
            HttpServletResponse resp) {
        String fid = req.getParameter(Constant.FOLDER_ID);
        Long folderId = null;
        if (Validity.isNullAndEmpty(fid)) {
            // if request parameter folderid is null
            User user = (User) req.getSession().getAttribute(
                    Constant.SESSION_USER);
            if (user == null) {
                return;
            }
            Folder folder = folderService.getRootFolder(user.getUserId());
            folderId = folder.getFolderId();
        }
        else {
            folderId = Long.parseLong(fid);
        }
        List<FileOrFolderJsonEntity> josnEntitys = null;
        josnEntitys = folderService.getAllByFolder(folderId);

        if (josnEntitys != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("total", josnEntitys.size());
            result.put("rows", josnEntitys);
            String jsonStr = JsonUtil.object2json(result);
            System.out.println(jsonStr);
            returnResp(jsonStr, resp);
        }
    }

    /**
     * create a new folder
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @author bxji
     */
    private void addFolder(HttpServletRequest req, HttpServletResponse resp) {
        String folderId = req.getParameter(Constant.PARENT_ID);
        String folderName = req.getParameter(Constant.FOLDER_NAME);
        String userID = req.getParameter(Constant.USER_ID);
        String result = Constant.REQUEST_RESULT_FAIL;

        if (!Validity.isNullAndEmpty(folderName)
                && !Validity.isNullAndEmpty(folderId)
                && !Validity.isNullAndEmpty(userID)) {
            Long pid = Long.parseLong(folderId);
            Long uid = Long.parseLong(userID);
            try {
                EntityManagerUtil.begin();
                Folder folder = folderService.addFolder(uid, pid, folderName,
                        null);
                EntityManagerUtil.commit();
                if (folder != null) {
                    result = Constant.REQUEST_RESULT_SUCCESS;
                }
            }
            catch (UBankException e) {
                EntityManagerUtil.rollback();
                LOG.error("create folder fail", e);
            }
        }
        returnResp(result, resp);
    }

    /**
     * create a new folder or file
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @author bxji
     */
    private void delFolderOrfile(HttpServletRequest req,
            HttpServletResponse resp) {
        String id = req.getParameter(Constant.FOLDER_OR_FILE_ID);
        String type = req.getParameter(Constant.DOCUMENT_TYPE);
        String result = Constant.REQUEST_RESULT_FAIL;

        if (!Validity.isNullAndEmpty(id) && !Validity.isNullAndEmpty(type)) {
            Long docId = Long.parseLong(id);

            try {
                EntityManagerUtil.begin();
                // if this is file that deleted
                if (Constant.DOCUMENT_TYPE_FILE.equals(type)) {
                    if (fileService.removeFile(docId)) {
                        result = Constant.REQUEST_RESULT_SUCCESS;
                    }
                }
                else if (Constant.DOCUMENT_TYPE_FOLDER.equals(type)) {
                    // if this is folder that deleted
                    if (folderService.delFolder(docId)) {
                        result = Constant.REQUEST_RESULT_SUCCESS;
                    }
                    else {
                        EntityManagerUtil.rollback();
                        return;
                    }
                }
                EntityManagerUtil.commit();
            }
            catch (Exception e) {
                EntityManagerUtil.rollback();
                LOG.error("deltte file or folder fail", e);
            }
            returnResp(result, resp);
        }
    }

    /**
     * share a new folder
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @author bxji
     */
    private void sharefolder(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter(Constant.FOLDER_ID);
        String result = Constant.REQUEST_RESULT_FAIL;
        if (Validity.isNullAndEmpty(id)) {
            return;
        }
        Long folderId = Long.parseLong("id");
        try {
            EntityManagerUtil.begin();
            if (folderService.shareFolder(folderId)) {
                result = Constant.REQUEST_RESULT_SUCCESS;
            }
            else {
                EntityManagerUtil.rollback();
                return;
            }
            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            EntityManagerUtil.rollback();
            LOG.error("share folder fail", e);
        }
        returnResp(result, resp);
    }

    /**
     * rename a folder or file
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @author bxji
     */
    private void rename(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter(Constant.FOLDER_OR_FILE_ID);
        String newName = req.getParameter(Constant.FOLDER_OR_FILE_NAME);
        String type = req.getParameter(Constant.DOCUMENT_TYPE);
        String result = Constant.REQUEST_RESULT_FAIL;
        if (Validity.isNullAndEmpty(id) || Validity.isNullAndEmpty(newName)
                || Validity.isNullAndEmpty(type)) {
            return;
        }
        Long fId = Long.parseLong(id);
        if (Constant.DOCUMENT_TYPE_FILE.equals(type.trim())) {
            if (fileService.renameFile(fId, newName)) {
                result = Constant.REQUEST_RESULT_SUCCESS;
            }
        }
        else if (Constant.DOCUMENT_TYPE_FOLDER.equals(type.trim())) {
            if (folderService.renameFolder(fId, newName)) {
                result = Constant.REQUEST_RESULT_SUCCESS;
            }
        }
        returnResp(result, resp);
    }

    /**
     * move a folder or file to specified folder
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @author bxji
     */
    private void moveTo(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter(Constant.FOLDER_OR_FILE_ID);
        String parentId = req.getParameter(Constant.PARENT_ID);
        String type = req.getParameter(Constant.DOCUMENT_TYPE);
        String result = Constant.REQUEST_RESULT_FAIL;
        if (Validity.isNullAndEmpty(id) || Validity.isNullAndEmpty(parentId)
                || Validity.isNullAndEmpty(type)) {
            return;
        }
        Long fId = Long.parseLong(id);
        Long folderId = Long.parseLong(parentId);
        if (Constant.DOCUMENT_TYPE_FILE.equals(type.trim())) {
            if (fileService.moveFileToFloder(folderId, fId)) {
                result = Constant.REQUEST_RESULT_SUCCESS;
            }
        }
        else if (Constant.DOCUMENT_TYPE_FOLDER.equals(type.trim())) {
            if (folderService.moveFolderTo(folderId, fId)) {
                result = Constant.REQUEST_RESULT_SUCCESS;
            }
        }
        returnResp(result, resp);
    }

    /**
     * copy a folder or file to specified folder
     * 
     * @param req
     *            request
     * @param resp
     *            response
     * @author bxji
     */
    private void copyTo(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter(Constant.FOLDER_OR_FILE_ID);
        String parentId = req.getParameter(Constant.PARENT_ID);
        String type = req.getParameter(Constant.DOCUMENT_TYPE);
        String result = Constant.REQUEST_RESULT_FAIL;
        if (Validity.isNullAndEmpty(id) || Validity.isNullAndEmpty(parentId)
                || Validity.isNullAndEmpty(type)) {
            return;
        }
        Long fId = Long.parseLong(id);
        Long folderId = Long.parseLong(parentId);
        if (Constant.DOCUMENT_TYPE_FILE.equals(type.trim())) {
            if (fileService.copyFileToFolder(folderId, fId)) {
                result = Constant.REQUEST_RESULT_SUCCESS;
            }
        }
        else if (Constant.DOCUMENT_TYPE_FOLDER.equals(type.trim())) {
            if (folderService.copyFolderTo(folderId, fId)) {
                result = Constant.REQUEST_RESULT_SUCCESS;
            }
        }
        returnResp(result, resp);
    }

    /**
     * return ajax request result
     * 
     * @param jsonStr
     *            json data String
     * @param resp
     *            HttpServletResponse
     * @author bxji
     */
    private void returnResp(String jsonStr, HttpServletResponse resp) {
        resp.setContentType("application/text;charset=UTF-8");
        try {
            PrintWriter pw = resp.getWriter();
            resp.getWriter().write(jsonStr);
            pw.flush();
        }
        catch (IOException e) {
            LOG.error("return json string exception", e);
        }
    }

}
