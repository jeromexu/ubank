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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.ufinity.marchant.ubank.bean.DownLoadLog;
import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.DateUtil;
import com.ufinity.marchant.ubank.common.DocumentUtil;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.Pager;
import com.ufinity.marchant.ubank.common.StringUtil;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.dao.DaoFactory;
import com.ufinity.marchant.ubank.dao.DownLoadLogDao;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;
import com.ufinity.marchant.ubank.exception.UBankServiceException;
import com.ufinity.marchant.ubank.model.DownloadResponse;
import com.ufinity.marchant.ubank.model.DownloadStatus;
import com.ufinity.marchant.ubank.service.FileService;

/**
 * FileService implements
 * 
 * @version 1.0 - 2010-8-19
 * @author zdxue
 */
public class FileServiceImpl implements FileService {

    private FileDao fileDao;
    private UserDao userDao;
    private FolderDao folderDao;
    private DownLoadLogDao downloadLogDao;

    // Logger for this class
    protected final Logger logger = Logger.getInstance(FileServiceImpl.class);

    /**
     * Constructor
     */
    public FileServiceImpl() {
        userDao = DaoFactory.createDao(UserDao.class);
        fileDao = DaoFactory.createDao(FileDao.class);
        folderDao = DaoFactory.createDao(FolderDao.class);
        downloadLogDao = DaoFactory.createDao(DownLoadLogDao.class);
    }

    public void setFileDao(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    /**
     * Search share files
     * 
     * @param fileName
     *            file name
     * @param fileSize
     *            file size flag
     * @param publishDate
     *            publish date flag
     * @param pageNum
     *            pageNum
     * @param pageSize
     *            pageSize
     * @return file pager obj
     * @throws UBankServiceException
     *             if occur exception, throw it
     * @author zdxue
     */
    public Pager<FileBean> searchShareFiles(String fileName, String fileSize,
            String publishDate, int pageNum, int pageSize)
            throws UBankServiceException {

        logger.debug("search condition - [fileName=" + fileName
                + " , fileSize=" + fileSize + " , publishDate=" + publishDate
                + " , pageNum=" + pageNum + " , pageSize=" + pageSize);

        if (fileName == null) {
            fileName = Constant.FILENAME_EMPTY;
        }

        if (fileSize == null) {
            fileSize = Constant.FILE_SIZE_0;
        }

        if (publishDate == null) {
            publishDate = Constant.FILE_PUBLISHDATE_0;
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(Constant.FILENAME, fileName);
        condition.put(Constant.MIN_FILE_SIZE, getMinFileSize(fileSize));
        condition.put(Constant.MAX_FILE_SIZE, getMaxFileSize(fileSize));

        Calendar cal = Calendar.getInstance();
        String datePattern = DateUtil.YYYY_MM_DD_HH_MM_SS;
        Date minDate = getMaxModifyDate(publishDate, (Calendar) cal.clone());
        Date maxDate = cal.getTime();
        condition.put(Constant.MIN_MODIFY_TIME, DateUtil.parse(DateUtil.format(
                minDate, datePattern), datePattern));
        condition.put(Constant.MAX_MODIFY_TIME, DateUtil.parse(DateUtil.format(
                maxDate, datePattern), datePattern));

        Pager<FileBean> pager = null;
        try {
            EntityManagerUtil.begin();

            pager = fileDao
                    .searchPaginatedForFile(pageNum, pageSize, condition);
            logger.debug("pager records=" + pager.getPageRecords());

            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            // not need rollback
            logger.error("search share file exception", e);
            throw new UBankServiceException("search share file exception");
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }

        return pager;
    }

    /**
     * download file by id
     * 
     * @param fileId
     *            the id of the file
     * @param user
     *            who will download
     * @return the response obj of download operation
     * @throws UBankServiceException
     *             the exception which do not get the file
     * @author jerome
     * @author modify by zdxue - refact and add process the point
     */
    public DownloadResponse download(Long fileId, User user)
            throws UBankServiceException {
        logger.debug("fileId=" + fileId + " , user=" + user);

        DownloadResponse response = new DownloadResponse();

        if (Validity.isEmpty(fileId)) {
            response.setFile(null);
            response.setStatus(DownloadStatus.FILE_NOT_EXIST);
            logger.debug("download response=" + response);
            return response;
        }

        if (user == null || user.getUserId() == null) {
            response.setFile(null);
            response.setStatus(DownloadStatus.OTHER_ERROR);
            logger.debug("download response=" + response);
            return response;
        }

        //because of the param user be passed from servlet(its the session user)
        //so find here in order to override the session user's point
        user = userDao.find(user.getUserId());
        logger.debug("find user=" + user);

        try {
            EntityManagerUtil.begin();
            FileBean fileBean = fileDao.find(fileId);
            logger.debug("find fileBean=" + fileBean);
            response.setFile(fileBean);

            if (fileBean == null) {
                response.setStatus(DownloadStatus.FILE_NOT_EXIST);
                logger.debug("download response=" + response);
                return response;
            }

            if (user.getUserId().equals(
                    fileBean.getFolder().getUser().getUserId())) {
                response.setStatus(DownloadStatus.OK);
                logger.debug("download response=" + response);
                return response;
            }

            Date downLoadTime = new Date();
            DownLoadLog downloadLog = downloadLogDao.findDownLoadLog(user
                    .getUserId(), fileBean.getFileId());
            logger.debug("find downloadLog by userId and fileId = "
                    + downloadLog);

            int downloadPoint = SystemGlobals.getInt(ConfigKeys.DOWNLOAD_POINT);
            logger.debug("download need point = " + downloadPoint);

            if (downloadLog == null) {
                if (user.getPoint() < downloadPoint) {
                    response.setStatus(DownloadStatus.POINT_NOT_ENOUGH);
                    logger.debug("download response=" + response);
                    return response;
                }

                userDao.modifyPointByUserId(user.getUserId(), -downloadPoint);
                DownLoadLog entity = new DownLoadLog();
                entity.setUser(user);
                entity.setFile(fileBean);
                entity.setDownLoadTime(downLoadTime);

                logger.debug("new downloadlog that will add to DB =" + entity);

                downloadLogDao.add(entity);
            }
            else {
                downloadLog.setDownLoadTime(downLoadTime);
                downloadLogDao.modify(downloadLog);
            }

            userDao.modifyPointByUserId(fileBean.getFolder().getUser()
                    .getUserId(), downloadPoint);

            EntityManagerUtil.commit();

            response.setStatus(DownloadStatus.OK);

            logger.debug("download response=" + response);

            return response;
        }
        catch (Exception e) {
            logger.error("get fileBean excepiton!", e);
            EntityManagerUtil.rollback();
            throw new UBankServiceException("get file exception by fileId!");
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }
    }

    /**
     * Get file size config condition
     * 
     * @param fileSize
     *            fileSize level
     * @return file size config
     * @author zdxue
     */
    private String getFileSizeConf(String fileSize) {
        logger.debug("fileSize=" + fileSize);

        String fileSizeConf = "";
        if (Constant.FILE_SIZE_0.equals(fileSize)) {
            fileSizeConf = SystemGlobals.getString(ConfigKeys.FILE_SIZE_0);
        }
        else if (Constant.FILE_SIZE_1.equals(fileSize)) {
            fileSizeConf = SystemGlobals.getString(ConfigKeys.FILE_SIZE_1);
        }
        else if (Constant.FILE_SIZE_2.equals(fileSize)) {
            fileSizeConf = SystemGlobals.getString(ConfigKeys.FILE_SIZE_2);
        }
        else if (Constant.FILE_SIZE_3.equals(fileSize)) {
            fileSizeConf = SystemGlobals.getString(ConfigKeys.FILE_SIZE_3);
        }
        else if (Constant.FILE_SIZE_4.equals(fileSize)) {
            fileSizeConf = SystemGlobals.getString(ConfigKeys.FILE_SIZE_4);
        }

        logger.debug("fileSizeConf=" + fileSizeConf);

        return fileSizeConf;
    }

    /**
     * Get min file size
     * 
     * @param fileSize
     *            file size level
     * @return min file size
     * @author zdxue
     */
    private long getMinFileSize(String fileSize) {
        long minFileSize = StringUtil.parseInt(getFileSizeConf(fileSize).split(
                Constant.FILE_SIZE_SEPARATOR)[0]);

        logger.debug("MinFileSize=" + minFileSize);

        return minFileSize;
    }

    /**
     * Get max file size
     * 
     * @param fileSize
     *            file size level
     * @return max file size
     * @author zdxue
     */
    private long getMaxFileSize(String fileSize) {
        long maxFileSize = StringUtil.parseInt(getFileSizeConf(fileSize).split(
                Constant.FILE_SIZE_SEPARATOR)[1]);

        logger.debug("MaxFileSize=" + maxFileSize);

        return maxFileSize;
    }

    /**
     * Get file modify date
     * 
     * @param publishDate
     *            publish date level
     * @param cal
     *            min modify date
     * @return max modify date
     * @author zdxue
     */
    private Date getMaxModifyDate(String publishDate, Calendar cal) {
        int amount = 0;
        if (Constant.FILE_PUBLISHDATE_0.equals(publishDate)) {
            amount = SystemGlobals.getInt(ConfigKeys.FILE_PUBLISHDATE_0);
        }
        else if (Constant.FILE_PUBLISHDATE_1.equals(publishDate)) {
            amount = SystemGlobals.getInt(ConfigKeys.FILE_PUBLISHDATE_1);
        }
        else if (Constant.FILE_PUBLISHDATE_2.equals(publishDate)) {
            amount = SystemGlobals.getInt(ConfigKeys.FILE_PUBLISHDATE_2);
        }
        else if (Constant.FILE_PUBLISHDATE_3.equals(publishDate)) {
            amount = SystemGlobals.getInt(ConfigKeys.FILE_PUBLISHDATE_3);
        }
        else if (Constant.FILE_PUBLISHDATE_4.equals(publishDate)) {
            amount = SystemGlobals.getInt(ConfigKeys.FILE_PUBLISHDATE_4);
        }
        else if (Constant.FILE_PUBLISHDATE_5.equals(publishDate)) {
            amount = SystemGlobals.getInt(ConfigKeys.FILE_PUBLISHDATE_5);
        }

        logger.debug("date roll amount = " + amount);

        if (amount == 1) {
            logger.info("amount == 1, return null");
            return null;
        }

        DateUtil.roll(cal, Calendar.DAY_OF_YEAR, amount);
        logger.debug("MaxModifyDate=" + cal.getTime());

        return cal.getTime();
    }

    /**
     * Copy file to the specified directory
     * 
     * @param targetFolderId
     *            target FolderId
     * @param sourceFileId
     *            source FileId
     * @return success return 'true' else return 'false'
     * @author bxji
     */
    public boolean copyFileToFolder(Long targetFolderId, Long sourceFileId) {
        if (Validity.isNullOrZero(targetFolderId)
                || Validity.isNullOrZero(sourceFileId)) {
            return false;
        }
        try {
            FileBean fileCopy = copyFile(sourceFileId);
            if (fileCopy != null) {
                EntityManagerUtil.begin();
                Folder folder = folderDao.find(targetFolderId);
                // If there is a same name file in the target directory
                String fileName = DocumentUtil.getNewName(
                        folder.getDirectory(), fileCopy.getFileName(), 0);
                // copy disk file
                int result = DocumentUtil.moveOrCopyFileTo(fileCopy, folder,
                        false, fileName);
                if (result != 1) {
                    logger.debug("copy disk file IO exception");
                    return false;
                }

                // if target folder is shared directory
                if (folder.getShare()) {
                    fileCopy.setShare(true);
                }
                else {
                    fileCopy.setShare(false);
                }
                fileCopy.setFileName(fileName);
                fileCopy.setDirectory(getDiskPath(folder));
                fileCopy.setFolder(folder);
                fileCopy.setModifyTime(new Date());
                fileCopy.setFileId(null);
                fileCopy.setDownLoadLogs(new HashSet<DownLoadLog>());
                fileDao.add(fileCopy);
                EntityManagerUtil.commit();
                return true;
            }
        }
        catch (Exception e) {
            logger.error("Update the file  database exception ", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }
        return false;
    }

    /**
     * this method is return a copy of the source file
     * 
     * @param fileId
     *            source file identificateion
     * @return Return a copy of the source file
     * @author bxji
     */
    private FileBean copyFile(Long fileId) {
        if (Validity.isNullOrZero(fileId)) {
            return null;
        }
        FileBean copy = null;
        try {
            FileBean file = fileDao.find(fileId);
            if (file != null) {
                copy = (FileBean) BeanUtils.cloneBean(file);
            }
        }
        catch (Exception e) {
            logger.error(" copy file throw exception", e);
        }
        return copy;
    }

    /**
     * Move file to specified directory
     * 
     * @param targetFolderId
     *            target FolderId
     * @param sourceFileId
     *            sourceFileId
     * @return success return 'true' else return 'false'
     * @author bxji
     */
    public boolean moveFileToFloder(Long targetFolderId, Long sourceFileId) {
        if (Validity.isNullOrZero(targetFolderId)
                || Validity.isNullOrZero(sourceFileId)) {
            return false;
        }
        try {
            EntityManagerUtil.begin();
            Folder folder = folderDao.find(targetFolderId);
            FileBean file = fileDao.find(sourceFileId);
            // if target directory is current directory
            if (file.getFolder().equals(folder)) {
                return true;
            }
            // If there is a same name file in the target directory
            String fileName = DocumentUtil.getNewName(folder.getDirectory(), file
                    .getFileName(), 0);

            // move disk file
            int result = DocumentUtil.moveOrCopyFileTo(file, folder, true,
                    fileName);
            if (result != 1) {
                logger.debug("Move disk file IO exception");
                return false;
            }
            // if target folder is shared directory
            if (folder.getShare()) {
                file.setShare(true);
            }
            else {
                file.setShare(false);
            }
            file.setFileName(fileName);
            file.setFolder(folder);
            file.setModifyTime(new Date());
            file.setDirectory(getDiskPath(folder));
            // update database table
            fileDao.modify(file);
            EntityManagerUtil.commit();
            return true;
        }
        catch (Exception e) {
            logger.error("Database operation exception "
                    + "when moving a file. ", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            return false;
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }
    }

    /**
     * delete a file from database and disk
     * 
     * @param fileId
     *            file id
     * @return success return 'true' else return 'false'
     * @author bxji
     */
    public boolean removeFile(Long fileId) {
        if (Validity.isNullOrZero(fileId)) {
            return false;
        }
        try {
            EntityManagerUtil.begin();
            FileBean file = fileDao.find(fileId);
            if (file != null) {
                fileDao.deleteById(fileId);
                int result = DocumentUtil.removeFile(file);
                if (result == 1) {
                    EntityManagerUtil.commit();
                }
                else {
                    EntityManagerUtil.rollback();
                    logger.debug("delete disk file fail.");
                    return false;
                }
            }
        }
        catch (Exception e) {
            logger.error("Database exception where tried to remove a file.", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
            return false;
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }
        return true;
    }

    /**
     * Rename files
     * 
     * @param fileId
     *            file id
     * @param newName
     *            new name
     * @return success return 'true' else return 'false'
     * @author bxji
     */
    public boolean renameFile(Long fileId, String newName) {
        if (Validity.isNullOrZero(fileId) || Validity.isNullAndEmpty(newName)) {
            return false;
        }
        try {
            EntityManagerUtil.begin();
            FileBean file = fileDao.find(fileId);
            if (file != null) {
                if (newName.equals(file.getFileName())) {
                    return true;
                }
                String fileName = DocumentUtil.getNewName(file.getDirectory(),
                        newName, 0);
                int result = DocumentUtil.renameFile(file, fileName);
                if (result != 1) {
                    logger.debug("rename disk file fail.");
                    return false;
                }
                file.setFileName(fileName);
                fileDao.modify(file);
                EntityManagerUtil.commit();
                return true;
            }
        }
        catch (Exception e) {
            logger.error("update the database exception when rename a file", e);
            if (EntityManagerUtil.isActive()) {
                EntityManagerUtil.rollback();
            }
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }
        return false;
    }

    /**
     * get current directory disk path
     * 
     * @param parentFolder
     *            parent directory of current directory
     * @return disk path String
     * @author bxji
     */
    private String getDiskPath(Folder parentFolder) {
        String currentPath = "";
        if (parentFolder == null) {
            return currentPath;
        }
        String parentPath = parentFolder.getDirectory();
        if (parentPath.length() > 0) {
            char c = parentPath.charAt(parentPath.length() - 1);
            if ('\\' == c) {
                currentPath = parentPath + parentFolder.getFolderId() + "\\";
            }
            else {
                currentPath = parentPath + "\\" + parentFolder.getFolderId()
                        + "\\";
            }
        }
        return currentPath;
    }

    /**
     * get file
     * 
     * @param fileId
     *            file id
     * @return FileBean
     * @throws UBankServiceException
     *             occur exception, throw it
     * @author zdxue
     */
    public FileBean getFile(long fileId) throws UBankServiceException {
        logger.debug("fileId=" + fileId);

        FileBean file = null;
        try {
            EntityManagerUtil.begin();

            file = fileDao.find(fileId);
            logger.debug("file=" + file);

            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            logger.error("find file exception", e);
            throw new UBankServiceException("find file exception");
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }

        return file;
    }
}
