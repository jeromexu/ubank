package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.JsonUtil;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.NodeUtil;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageResource;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.exception.UBankServiceException;
import com.ufinity.marchant.ubank.service.FileService;
import com.ufinity.marchant.ubank.service.FolderService;
import com.ufinity.marchant.ubank.service.ServiceFactory;
import com.ufinity.marchant.ubank.upload.UploadConstant;
import com.ufinity.marchant.ubank.vo.FileOrFolderJsonEntity;
import com.ufinity.marchant.ubank.vo.FolderNode;
import com.ufinity.marchant.ubank.vo.JsonNode;

/**
 * Folder Servlet used to operation folder
 * 
 * @version 1.0 - 2010-8-19
 * @author liujun
 */
public class FolderServlet extends AbstractServlet {
    private static final long serialVersionUID = -8297805269743197486L;
    // Logger for this class
    private final Logger logger = Logger.getInstance(FolderServlet.class);

    FolderService folderService = null;
    FileService fileService = null;
    private static String SIZE = "size";
    private static String ASC = "asc";

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
            throws ServletException, IOException, UnsupportedEncodingException {

        String method = parseActionName(req);
        String rslt = Constant.ERROR_PAGE_500;

        if (Constant.SHOW_TREE.equals(method)) {
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
            shareFolder(req, resp);
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
        else if (Constant.CANCEL_SHARE.equals(method)) {
            cancelShare(req, resp);
            return;
        }
        forward(req, resp, rslt);
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
     * @throws IOException
     */
    private void showTree(HttpServletRequest req, HttpServletResponse resp) {
        User user = getLoginUser(req);
        if (user == null) {
            return;
        }
        FolderNode treeRootNode = null;
        FolderNode shareRootNode = null;
        try {
            treeRootNode = folderService.getTreeRoot(user.getUserId());
            shareRootNode = folderService.getShareTree(user.getUserId());

            treeRootNode.setFolderName(SystemGlobals
                    .getString(ConfigKeys.ROOT_NAME));
            shareRootNode.setFolderName(SystemGlobals
                    .getString(ConfigKeys.SHARE_ROOT_NAME));
            JsonNode jsonTree = NodeUtil.copyFolderNodeToJsonNode(treeRootNode);
            JsonNode jsonShare = NodeUtil
                    .copyFolderNodeToJsonNode(shareRootNode);
            JsonNode[] nodes = { jsonTree, jsonShare };

            String treeJson = JsonUtil.object2json(nodes);
            returnResp(treeJson, resp);
        }
        catch (UBankServiceException e) {
            logger.error("when try get user root directory, "
                    + "throw an exception:user id can not be null", e);
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
     * @throws IOException
     */
    private void showFolderContent(HttpServletRequest req,
            HttpServletResponse resp) {
        User user = getLoginUser(req);
        if (user == null) {
            return;
        }
        String fid = req.getParameter(Constant.FOLDER_ID);
        String layer = req.getParameter(Constant.FOLDER_LAYER);

        String sortBy = req.getParameter("sortBy");
        String sortType = req.getParameter("sortType");

        Long folderId = null;
        Long layerNumber = null;
        if (Validity.isNullAndEmpty(layer)) {
            layerNumber = 1l;
        }
        else {
            layerNumber = Long.parseLong(layer) + 1l;
        }
        if (Validity.isNullAndEmpty(fid)) {
            // if request parameter folderid is null
            Folder folder = folderService.getRootFolder(user.getUserId());
            folderId = folder.getFolderId();
        }
        else {
            folderId = Long.parseLong(fid);
        }
        List<FileOrFolderJsonEntity> josnEntitys = null;
        try {
            josnEntitys = folderService.getAllFromFolder(folderId, layerNumber);
        }
        catch (UBankServiceException e) {
            logger.error("show folder content serve layer exception.", e);
        }
        if (null != sortBy && sortType != null) {
            josnEntitys = NodeUtil.sortJsonObjs(josnEntitys, sortBy, sortType);
        }
        else {
            josnEntitys = NodeUtil.sortJsonObjs(josnEntitys, SIZE, ASC);
        }
        if (josnEntitys != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("total", josnEntitys.size());
            result.put("rows", josnEntitys);
            String jsonStr = JsonUtil.object2json(result);
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
     * @throws UnsupportedEncodingException
     */
    private void addFolder(HttpServletRequest req, HttpServletResponse resp)
            throws UnsupportedEncodingException {
        User user = getLoginUser(req);
        Long folderId = (Long) req.getSession().getAttribute(
                UploadConstant.CURRENT_FOLDER_ID);
        String folderName = req.getParameter(Constant.FOLDER_NAME);
        folderName = URLDecoder.decode(folderName, "utf-8");
        String layerNumber = req.getParameter(Constant.FOLDER_LAYER);
        String result = MessageResource.getText(MessageKeys.ADD_FOLDER_FAIL);
        if (!Validity.isNullAndEmpty(folderName)
                && !Validity.isNullOrZero(folderId)
                && !Validity.isNullAndEmpty(layerNumber) && user != null) {
            // is name contain special char
            if (Validity.isSpecial(folderName)) {
                result = MessageResource.getText(
                        MessageKeys.CAN_NOT_CONTAIN_SPECIAL_CHAR,
                        Validity.SPECIAL_CHARACTER);
                logger
                        .debug("new folder name can not contains  special char .");
                returnResp(result, resp);
                return;
            }
            // user directory layer number must less than ten
            Long layer = Long.parseLong(layerNumber);
            if (layer > Constant.FOLDER_MAX_LAYER) {
                logger.debug("create new Folder failed, "
                        + "user directory layer number must less than ten.");
                result = MessageResource
                        .getText(MessageKeys.FOLDER_LAYER_LIMIT);
                returnResp(result, resp);
                return;
            }
            try {
                Folder folder = folderService.addFolder(user, folderId,
                        folderName, null);
                if (folder != null) {
                    result = Constant.REQUEST_RESULT_SUCCESS;
                }
            }
            catch (UBankServiceException e) {
                logger.error("create folder fail", e);
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
        String result = MessageResource.getText(MessageKeys.DELETE_FAIL);

        if (!Validity.isNullAndEmpty(id) && !Validity.isNullAndEmpty(type)) {
            Long docId = Long.parseLong(id);
            try {
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
                }
            }
            catch (Exception e) {
                logger.error("deltte file or folder fail", e);
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
    private void shareFolder(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter(Constant.FOLDER_ID);
        String result = MessageResource.getText(MessageKeys.SHARE_FOLDER_FAIL);
        if (Validity.isNullAndEmpty(id)) {
            return;
        }
        Long folderId = Long.parseLong(id);
        try {
            if (folderService.shareFolder(folderId)) {
                result = Constant.REQUEST_RESULT_SUCCESS;
            }
        }
        catch (Exception e) {
            logger.error("share folder fail", e);
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
     * @throws UnsupportedEncodingException
     */
    private void rename(HttpServletRequest req, HttpServletResponse resp)
            throws UnsupportedEncodingException {
        String id = req.getParameter(Constant.FOLDER_OR_FILE_ID);
        String newName = req.getParameter(Constant.FOLDER_OR_FILE_NAME);
        newName = URLDecoder.decode(newName, "utf-8");
        String type = req.getParameter(Constant.DOCUMENT_TYPE);
        String result = MessageResource.getText(MessageKeys.RENAME_FAIL);
        // is name contain special char
        if (Validity.isSpecial(newName)) {
            result = MessageResource.getText(
                    MessageKeys.CAN_NOT_CONTAIN_SPECIAL_CHAR,
                    Validity.SPECIAL_CHARACTER);
            returnResp(result, resp);
            return;
        }
        if (!Validity.isNullAndEmpty(id) && !Validity.isNullAndEmpty(newName)
                && !Validity.isNullAndEmpty(type)) {
            Long fId = Long.parseLong(id);
            try {
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
            }
            catch (UBankServiceException e) {
                logger.error("rename request serve layer exception. ", e);
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
        String result = MessageResource.getText(MessageKeys.MOVE_FAIL);
        if (!Validity.isNullAndEmpty(id) && !Validity.isNullAndEmpty(parentId)
                && !Validity.isNullAndEmpty(type)) {
            Long fId = Long.parseLong(id);
            Long folderId = Long.parseLong(parentId);
            try {
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

            }
            catch (UBankServiceException e) {
                logger.error("move file or folder serve layer exception", e);
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
        String result = MessageResource.getText(MessageKeys.COPY_FAIL);
        if (!Validity.isNullAndEmpty(id) && !Validity.isNullAndEmpty(parentId)
                && !Validity.isNullAndEmpty(type)) {
            Long fId = Long.parseLong(id);
            Long folderId = Long.parseLong(parentId);
            try {
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
            }
            catch (Exception e) {
                logger.error("copy file or folder serve layer exception", e);
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
            logger.error("return json string exception", e);
        }
    }

    /**
     * return ajax cancel share folder request result
     * 
     * @param jsonStr
     *            json data String
     * @param resp
     *            HttpServletResponse
     * @author bxji
     */
    private void cancelShare(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter(Constant.FOLDER_ID);
        String result = MessageResource.getText(MessageKeys.CANCEL_SHARE_FAIL);
        if (!Validity.isNullAndEmpty(id)) {
            Long folderId = Long.parseLong(id);
            try {
                if (folderService.cancelShareFolder(folderId)) {
                    result = Constant.REQUEST_RESULT_SUCCESS;
                }
            }
            catch (Exception e) {
                logger.error("cancel share folder fail", e);
            }
        }
        returnResp(result, resp);
    }
}
