// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:
//
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

import com.ufinity.marchant.ubank.common.JsonUtil;
import com.ufinity.marchant.ubank.upload.ProgressInfo;
import com.ufinity.marchant.ubank.upload.UploadListener;

/**
 * 
 * file upload servlet
 * 
 * @author liujun
 * @version 1.0
 * @since 2010-8-20
 */
public class FileUploadServlet extends AbstractServlet {

    private static final long serialVersionUID = 6092584996678971635L;

    private Logger logger = Logger.getLogger(FileUploadServlet.class);

    private static final String UPLOAD = "upload";

    private static final String CONTINUE_UOLOAD = "continueUpload";

    private static final String GET_INFO = "getInfo";

    private static final String PAUSE = "pause";

    private static final long MAX_LENGTH = 100 * 1024 * 1024L; // 10MB

    private static final int HTTP_REDUNDANT_LENGTH = 1024; // 1KB

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String method = parseActionName(request);
        if (UPLOAD.equals(method)) {
            doUpload(request);
        } else if (GET_INFO.equals(method)) {
            getUploadInfo(request, response);
        } else if (PAUSE.equals(method)) {
            pause(request);
        } else if (CONTINUE_UOLOAD.equals(method)) {
            continueUpload(request);
        }
    }

    /**
     * 
     * response json date to client
     * 
     * @param request
     *            response
     * @param String
     *            json
     */
    private void responseClient(HttpServletResponse response, String json) {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(json);
            out.flush();
        } catch (Exception e) {
            logger.error("response client error:", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 
     * get file name
     * 
     * @param String
     *            fileName
     */
    private String getFileName(String fileName) {
        String name = fileName.substring(fileName.lastIndexOf("\\") + 1);
        // TODO
        return "E:/temp/" + name;
    }

    /**
     * 
     * get upload info
     * 
     * @param request
     *            request
     * @param response
     *            response
     */
    private void getUploadInfo(HttpServletRequest request,
            HttpServletResponse response) {
        // System.out.println("getUploadInfo.........");
        ProgressInfo pi = (ProgressInfo) request.getSession().getAttribute(
                "progressInfo");
        if (pi != null) {
            String json = JsonUtil.bean2json(pi);
            responseClient(response, json);
        }
    }

    /**
     * 
     * pause upload
     * 
     * @param request
     *            request
     */
    private void pause(HttpServletRequest request) {
        // System.out.println("pause~~~~~~~~~~~~~~~~~");
        ProgressInfo pi = (ProgressInfo) request.getSession().getAttribute(
                "progressInfo");
        if (pi != null) {
            pi.setPause(true);
            request.getSession().setAttribute("progressInfo", pi);
        }
    }

    /**
     * 
     * continue upload
     * 
     * @param request
     *            request
     */
    private void continueUpload(HttpServletRequest request) {
        // System.out.println("continue upload~~~~~~~~~~~~~~~~~");
        ProgressInfo pi = (ProgressInfo) request.getSession().getAttribute(
                "progressInfo");
        if (pi != null) {
            pi.setPause(false);
            request.getSession().setAttribute("progressInfo", pi);
        }
    }

    /**
     * 
     * do file upload
     * 
     * @param request
     *            request
     */
    private void doUpload(HttpServletRequest request) {
        ProgressInfo pi = new ProgressInfo();
        request.getSession().setAttribute("progressInfo", pi);

        String fldName = "";
        FileItemStream item = null;
        BufferedInputStream stream = null;
        OutputStream out = null;
        ByteArrayOutputStream bStream = null;
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);

            if (isMultipart) {
                int filesSize = request.getContentLength()
                        - HTTP_REDUNDANT_LENGTH;
                if (filesSize >= MAX_LENGTH) {
                    String errorMsg = "Error: Current files size is "
                            + filesSize / (1024 * 1024)
                            + "MB which has exceeded max " + "10MB";
                    pi.setCompleted(true);
                    pi.setErrorMsg(errorMsg);
                    throw new Exception(errorMsg);
                }
                ServletFileUpload upload = new ServletFileUpload();
                upload.setHeaderEncoding("UTF-8");
                upload.setFileSizeMax(MAX_LENGTH);
                upload.setSizeMax(MAX_LENGTH);
                UploadListener uploadListener = new UploadListener(pi);
                upload.setProgressListener(uploadListener);
                // Parse the request
                FileItemIterator fIter = upload.getItemIterator(request);
                while (fIter.hasNext()) {
                    item = fIter.next();
                    if (!item.isFormField()) {
                        fldName = item.getFieldName();
                        if (item.getName() == null
                                || "".equals(item.getName().trim())) {
                            continue;
                        }

                        pi.setCurFileName(item.getName());
                        pi.setUploadedFiles(pi.getUploadedFiles() + "<b>"
                                + item.getName() + "</b><br/>");

                        stream = new BufferedInputStream(item.openStream());
                        bStream = new ByteArrayOutputStream();
                        long bStreamLen = Streams.copy(stream, bStream, true);

                        // logger.debug("Upload path is :" +
                        // this.getFileName(item.getName()));
                        System.out.println("Upload path is :"
                                + this.getFileName(item.getName()));

                        File file = new File(this.getFileName(item.getName()));
                        if (file.exists()) {
                            file.delete();
                        }
                        out = new FileOutputStream(file);
                        bStream.writeTo(out);

                        // logger.debug("Upload fldName :" + fldName
                        // + ",just was uploaded len:" + bStreamLen);
                        System.out.println("Upload fldName :" + fldName
                                + ",just was uploaded len:" + bStreamLen);
                    }
                }

                pi.setInProgress(false);
                pi.setCurrentTime(System.currentTimeMillis());
                pi.setBytesRead(filesSize);
                pi.setCompleted(true);
            }
        } catch (Exception e) {
            pi.setInProgress(false);
            logger.warn("Upload cancelled or interrupted!", e);
        } finally {
            try {
                if (bStream != null) {
                    bStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }

}
