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
package com.ufinity.marchant.ubank.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.service.UploadService;
import com.ufinity.marchant.ubank.upload.ProgressInfo;

/**
 * UploadService implements
 * 
 * @version 1.0 - 2010-8-23
 * @author liujun
 */
public class UploadServiceImpl implements UploadService {

    private final Logger logger = Logger.getLogger(UploadServiceImpl.class);

    private FileDao fileDao;

    private FolderDao folderDao;

    /**
     * upload file and save to db
     * 
     * @param folderId
     *            current folder id
     * @param pi
     *            info of upload
     * @param fIter
     *            the FileItemIterator
     * @throws Exception
     *             if have exception
     */
    public void uploadAndSaveDb(Long folderId, ProgressInfo pi,
            FileItemIterator fIter) throws Exception {
        // TODO remove
        folderId = 1l;

        if (folderId == null || folderId == 0) {
            logger.warn("current folder or folder is null.");
            return;
        }
        if (fIter == null) {
            logger.warn("FileItemIterator is null. will return");
            return;
        }

        String fldName = "";
        FileItemStream item = null;
        BufferedInputStream stream = null;
        OutputStream out = null;
        ByteArrayOutputStream bStream = null;
        String folderDir = null;

        try {
            folderDir = getFolderDir(folderId);

            while (fIter.hasNext()) {
                item = fIter.next();
                if (!item.isFormField()) {
                    fldName = item.getFieldName();
                    String fileFullName = item.getName();
                    if (fileFullName == null || "".equals(fileFullName.trim())) {
                        continue;
                    }

                    pi.setCurFileName(fileFullName);
                    pi.setUploadedFiles(pi.getUploadedFiles() + "<b>"
                            + fileFullName + "</b><br/>");

                    stream = new BufferedInputStream(item.openStream());
                    bStream = new ByteArrayOutputStream();
                    long bStreamLen = Streams.copy(stream, bStream, true);

                    logger.debug("Upload path is :"
                            + this.getFileDir(folderDir, fileFullName));
                    // System.out.println("Upload path is :"
                    // + this.getFileDir(folderDir,fileFullName));

                    File file = new File(this.getFileDir(folderDir,
                            fileFullName));
                    if (file.exists()) {
                        file.delete();
                    }
                    out = new FileOutputStream(file);
                    bStream.writeTo(out);

                    logger.debug("Upload fldName :" + fldName
                            + ",just was uploaded len:" + bStreamLen);
                    // System.out.println("Upload fldName :" + fldName
                    // + ",just was uploaded len:" + bStreamLen);

                    Folder folder = new Folder();
                    folder.setFolderId(folderId);

                    FileBean fb = new FileBean();
                    fb.setFolder(folder);
                    fb.setFileName(fileFullName);
                    fb.setFileType(getFileType(fileFullName));
                    fb.setCreateTime(new Date());
                    fb.setDirectory(folderDir);
                    // kb
                    fb.setSize(bStreamLen / 1024);
                    fb.setShare(false);
                    this.saveFile(fb);
                }
            }
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

    /**
     * 
     * get file dir
     * 
     * @param currentDir
     *            current dir
     * @param fileName
     *            file name
     * @return file dir    
     */
    private String getFileDir(String currentDir, String fileName) {
        String name = fileName.substring(fileName.lastIndexOf("\\") + 1);
        return currentDir + name;
    }

    /**
     * 
     * get file name
     * 
     * @param fileName
     *            full file name.
     * @return file name     
     */
    private String getFileName(String fileName) {
        int i = fileName.lastIndexOf(".");
        if (i == -1) {
            return fileName;
        }
        return fileName.substring(0, i);
    }

    /**
     * 
     * get file type
     * 
     * @param fileName
     *            file name
     * @return file type           
     */
    private String getFileType(String fileName) {
        int i = fileName.lastIndexOf(".");
        if (i == -1) {
            return "";
        }
        return fileName.substring(i + 1);
    }

    /**
     * get folder dir
     * 
     * @param folderId
     *            folder id
     * @throws RuntimeException
     *             if has RuntimeException
     * @return folder dir
     */
    private String getFolderDir(Long folderId) throws RuntimeException {
        try {
            EntityManagerUtil.begin();
            folderDao = DaoFactory.createDao(FolderDao.class);
            Folder folder = folderDao.find(folderId);
            EntityManagerUtil.commit();

            return folder.getDirectory();
        } catch (RuntimeException e) {
            throw e;
        } finally {
            EntityManagerUtil.closeEntityManager();
        }
    }

    /**
     * save file to db
     * 
     * @param fb
     *            file bean
     * @throws RuntimeException
     *             if has RuntimeException
     */
    private void saveFile(FileBean fb) throws RuntimeException {
        try {
            EntityManagerUtil.begin();
            fileDao = DaoFactory.createDao(FileDao.class);
            fileDao.add(fb);
            EntityManagerUtil.commit();
        } catch (RuntimeException e) {
            EntityManagerUtil.rollback();
            throw e;
        } finally {
            EntityManagerUtil.closeEntityManager();
        }
    }
}
