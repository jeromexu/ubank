package com.ufinity.marchant.ubank.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.DocumentUtil;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.StringUtil;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.exception.UBankServiceException;
import com.ufinity.marchant.ubank.service.FileService;
import com.ufinity.marchant.ubank.service.ServiceFactory;
import com.ufinity.marchant.ubank.service.UserService;
import com.ufinity.marchant.ubank.vo.DownloadResponse;

/**
 * 
 * download file servlet
 * 
 * @author jerome
 * @version 1.0
 * @since 2010-8-19
 */
public class DownLoadServlet extends AbstractServlet {

    private static final long serialVersionUID = 1L;
    // Logger for this class
    protected final Logger logger = Logger.getInstance(DownLoadServlet.class);

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        String method = parseActionName(request);
        String rslt = null;

        try{
            if (Constant.ACTION_DOWNLOAD.equals(method)) {
                rslt = download(request, response);
            }else if(Constant.ACTION_SHOW_DOWNLOAD.equals(method)) {
                rslt = showDownload(request, response);
            }
        } catch (UBankException e) {
            logger.error("occur UBankException.", e);
            redirect(response, Constant.ERROR_PAGE_500);
            return;
        }

        if (rslt != null) {
            forward(request, response, rslt);
        }
    }

    /**
     * show download 
     *
     * @param request request
     * @param response response
     * @return download page
     * @throws UBankException 
     * @author zdxue
     */
    private String showDownload(HttpServletRequest request, HttpServletResponse response) throws UBankException {
        String id = request.getParameter("id");
        logger.debug("fileId=" + id);

        int fileId = StringUtil.parseInt(id, 0);
        logger.debug("fileId=" + fileId);

        FileService fileService = ServiceFactory.createService(FileService.class);
        UserService userService = ServiceFactory.createService(UserService.class);
        
        FileBean file = null;
        User user = null;
        try {
            if(checkLogin(request)){
                user = userService.getUserByUserName(getLoginUser(request).getUserName());
                logger.debug("get user=" + user);
            }
            
            file = fileService.getFile(fileId);
            logger.debug("file=" + file);
        } catch (UBankServiceException e) {
            logger.error("show download exception", e);
            throw new UBankException("show download exception");
        }

        request.setAttribute(Constant.ATTR_FILE, file);
        request.setAttribute(Constant.ATTR_EVENTPATH, request.getServletPath()+"?"+request.getQueryString());
        request.setAttribute(Constant.ATTR_USER, user);
        return Constant.DOWNLOAD;
    }

    /**
     * 
     * download the file
     * 
     * @param request
     *            download file
     * @param response
     * @throws IOException 
     * @throws UBankException
     * @author jerome 
     * @author modify by zdxue (about DownloadResponse) 
     */
    private String download(HttpServletRequest request,
            HttpServletResponse response) throws UBankException {
        if(!checkLogin(request)){
            request.setAttribute(Constant.ATTR_EVENTPATH, request.getParameter(Constant.REQ_PARAM_EVENTPATH));
            return Constant.LOGIN_PAGE;
        }

        try {
            String id = request.getParameter("id");
            logger.debug("file id = " + id);
            Long fileId = 0l;
            if (!Validity.isEmpty(id)) {
                fileId = Long.valueOf(id);
            } else {
                return Constant.ERROR_PAGE_500;
            }
            FileService fileService = ServiceFactory.createService(FileService.class);
            UserService userService = ServiceFactory.createService(UserService.class);
            
            User user = userService.getUserByUserName(getLoginUser(request).getUserName());
            logger.debug("get user=" + user);
            request.setAttribute(Constant.ATTR_USER, user);

            File file = null;

            DownloadResponse resp = fileService.download(fileId, getLoginUser(request));
            FileBean fileBean = resp.getFile();
            request.setAttribute(Constant.ATTR_FILE, fileBean);

            switch(resp.getStatus()) {
            case OK :
                return download(file, fileBean, request, response);
            case FILE_NOT_EXIST:
                request.setAttribute(Constant.ATTR_ERROR_MSG, getText(MessageKeys.DOWNLOAD_FILE_NOT_EXIST));
                return Constant.DOWNLOAD;
            case POINT_NOT_ENOUGH:
                request.setAttribute(Constant.ATTR_ERROR_MSG, getText(MessageKeys.DOWNLOAD_POINT_NOT_ENOUGH));
                return Constant.DOWNLOAD;
            case OTHER_ERROR: 
                request.setAttribute(Constant.ATTR_ERROR_MSG, getText(MessageKeys.DOWNLOAD_OTHER_ERROR));
                return Constant.DOWNLOAD;
            }
        }catch(UBankServiceException e){
            logger.error("download exception", e);
            throw new UBankException("download exception");
        } catch (IOException e) {
            logger.error("IO exception", e);
            request.setAttribute(Constant.ATTR_ERROR_MSG, getText(MessageKeys.DOWNLOAD_OTHER_ERROR));
            return Constant.DOWNLOAD;
        }

        return Constant.ERROR_PAGE_500;
    }

    /**
     * download
     *
     * @param file
     * @param fileBean
     * @param request
     * @param response
     * @param buff
     * @param outPut
     * @return
     * @throws IOException
     * @author jerome 
     * @author modify by zdxue (when construct FileOutputStream exception, throw out, no need to set HTTP header) 
     */
    private String download(File file, FileBean fileBean, HttpServletRequest request, 
            HttpServletResponse response) throws IOException {

        logger.debug("filename = " + fileBean.getFileName());

        BufferedInputStream buff = null;
        OutputStream outPut = null;
        try{
            file = new File(DocumentUtil.getApplicationPath()
                    + getFilePath(fileBean.getDirectory(), fileBean
                            .getFileName()));

            buff = new BufferedInputStream(new FileInputStream(file));
            byte[] b = new byte[5 * 1024];
            long k = 0;

            response.setContentType("text/html; charset=UTF-8");
            response.setContentType("application/x-msdownload");
            response.setContentLength((int) file.length());
            
            String agent = (String)request.getHeader("user-agent");
            String fileName = StringUtil.processFileName(fileBean.getFileName(), agent);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            outPut = response.getOutputStream();
            while (k < file.length()) {
                int j = buff.read(b, 0, 1024);
                k += j;
                logger.debug("file has downloaded " + k);
                // write the data into the client memory
                outPut.write(b, 0, j);
            }

            outPut.flush();
        } finally {
            if (buff != null) {
                buff.close();
            }
            
            if (outPut != null) {
                outPut.close();
            }
        }
        
        return null;
    }

    /**
     * get the integrited path
     * 
     * @param directory
     *            the file path folder
     * @param fileName
     *            the file name
     * @return integrited path
     * @author jerome
     */
    private String getFilePath(String directory, String fileName) {
        String filePath = null;
        if (!Validity.isEmpty(directory) && directory.endsWith(File.separator)) {
            filePath = directory + fileName;
        } else {
            filePath = directory + File.separator + fileName;
        }
        return filePath;
    }

}
