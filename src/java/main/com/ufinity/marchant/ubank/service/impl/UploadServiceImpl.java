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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.exception.DbException;
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
     * @param folderDir
     *            current folder dir   
     * @param pi
     *            info of upload
     * @param item
     *            the FileItemStream
     * @throws Exception
     *             if have exception
     */
    public void uploadAndSaveDb(Long folderId, String folderDir, ProgressInfo pi, FileItemStream item)
            throws Exception {
        if (folderId == null || folderId == 0) {
            logger.warn("current folder or folder is null.");
            return;
        }
        if (item == null) {
            logger.warn("FileItem is null. will return");
            return;
        }

        String fldName = "";
        BufferedInputStream stream = null;
        OutputStream out = null;
        ByteArrayOutputStream bStream = null;
        List<File> createFiles = new ArrayList<File>();

        try {
            fldName = item.getFieldName();
            String fileFullName = item.getName();
            if (fileFullName == null || "".equals(fileFullName.trim())) {
                return;
            }

            pi.setCurFileName(fileFullName);
            pi.setUploadedFiles(pi.getUploadedFiles() + "<b>" + fileFullName
                    + "</b><br/>");

            stream = new BufferedInputStream(item.openStream());
            bStream = new ByteArrayOutputStream();
            long bStreamLen = Streams.copy(stream, bStream, true);

            logger.debug("Upload path is :"
                    + this.getFileDir(folderDir, fileFullName));
            System.out.println("Upload path is :"
                    + this.getFileDir(folderDir, fileFullName));

            File file = new File(this.getFileDir(folderDir, fileFullName));
            if (file.exists()) {
                file.delete();
            }
            out = new FileOutputStream(file);
            bStream.writeTo(out);
            createFiles.add(file);

            logger.debug("Upload fldName :" + fldName
                    + ",just was uploaded len:" + bStreamLen);
            System.out.println("Upload fldName :" + fldName
                    + ",just was uploaded len:" + bStreamLen);

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
        } catch (DbException e) {
            // remove files
            for (File file : createFiles) {
                if (file.exists()) {
                    file.delete();
                }
            }
            throw e;
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
     * @throws DbException
     *             if has DbException
     * @return folder dir
     */
    public String getFolderDir(Long folderId) throws DbException {
        try {
            EntityManagerUtil.begin();
            folderDao = DaoFactory.createDao(FolderDao.class);
            Folder folder = folderDao.find(folderId);
            if(folder == null){
                throw new DbException("Upload folder is null.");
            }
            EntityManagerUtil.commit();
            
            String objectPath = SystemGlobals.getString("ubank.path", new String[]{System.getProperty("catalina.home")});
            String folderDir = objectPath + File.separator + folder.getDirectory()+ File.separator  + folderId + File.separator;
            return folderDir;
        } catch (RuntimeException e) {
            throw new DbException(e);
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
    private void saveFile(FileBean fb) throws DbException {
        try {
            EntityManagerUtil.begin();
            fileDao = DaoFactory.createDao(FileDao.class);
            fileDao.add(fb);
            EntityManagerUtil.commit();
        } catch (RuntimeException e) {
            EntityManagerUtil.rollback();
            throw new DbException(e);
        } finally {
            EntityManagerUtil.closeEntityManager();
        }
    }
}
