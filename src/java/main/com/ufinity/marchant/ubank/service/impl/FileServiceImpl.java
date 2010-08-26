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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.Folder;
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
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.exception.DbException;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.service.FileService;

/**
 * FileService implements
 * 
 * @version 1.0 - 2010-8-19
 * @author zdxue
 */
public class FileServiceImpl implements FileService {

    private FileDao fileDao;
    private FolderDao folderDao;

    // Logger for this class
    protected final Logger LOG = Logger.getInstance(FileServiceImpl.class);

    /**
     * Constructor
     */
    public FileServiceImpl() {
        fileDao = DaoFactory.createDao(FileDao.class);
        folderDao = DaoFactory.createDao(FolderDao.class);
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
     * @throws UBankException
     *             if occur exception, throw it
     * @author zdxue
     */
    public Pager<FileBean> searchShareFiles(String fileName, String fileSize,
            String publishDate, int pageNum, int pageSize)
            throws UBankException {

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
            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            // not need rollback
            LOG.error("search share file exception", e);
            throw new UBankException("search share file exception");
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }

        return pager;
    }

    /**
     * get the file path by the file id
     * 
     * @param fileId
     *            the id of the file
     * @return the fileBean object
     * @throws UBankException
     *             the exception which do not get the file
     * @author jerome
     */
    public FileBean getFileBean(Long fileId) throws UBankException {
        if (Validity.isEmpty(fileId)) {
            return null;
        }
        FileBean fileBean = null;
        try {
            EntityManagerUtil.begin();
            fileBean = fileDao.find(fileId);
            EntityManagerUtil.commit();
        }
        catch (Exception e) {
            LOG.error("get fileBean excepiton!", e);
            throw new UBankException("get file exception by fileId!");
        }
        finally {
            EntityManagerUtil.closeEntityManager();
        }

        return fileBean;
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
        return StringUtil.parseInt(getFileSizeConf(fileSize).split(
                Constant.FILE_SIZE_SEPARATOR)[0]);
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
        return StringUtil.parseInt(getFileSizeConf(fileSize).split(
                Constant.FILE_SIZE_SEPARATOR)[1]);
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

        if (amount == 1) {
            return null;
        }

        DateUtil.roll(cal, Calendar.DAY_OF_YEAR, amount);
        return cal.getTime();
    }

    /**
     * get all files under specified directory
     * 
     * @param folderId
     *            folder directory ID
     * @return files list
     * @author bxji
     */
    public List<FileBean> getFilesByFolder(Long folderId) {
        // TODO Auto-generated method stub
        return null;
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
        if (targetFolderId == null || 0l == targetFolderId
                || sourceFileId == null || 0l == sourceFileId) {
            return false;
        }
        FileBean fileCopy = copyFile(sourceFileId);
        if (fileCopy != null) {
            try {
                Folder folder = folderDao.find(targetFolderId);

                // If there is a same name file in the target directory
                if (isSameName(folder, fileCopy.getFileName())) {
                    autoRename(fileCopy);
                }

                // if target folder is shared directory
                if (folder.getShare()) {
                    fileCopy.setShare(true);
                }
                else {
                    fileCopy.setShare(false);
                }
                fileCopy.setDirectory(getDiskPath(folder));
                fileCopy.setFolder(folder);
                fileCopy.setModifyTime(new Date());
                // copy disk file
                int result = DocumentUtil.copyFile(fileCopy, folder);
                if (result != 1) {
                    LOG.debug("copy disk file IO exception");
                    return false;
                }
                fileDao.modify(fileCopy);
                return true;
            }
            catch (Exception e) {
                LOG.debug("Update the file information database exception ", e);
                return false;
            }
        }
        return false;
    }

    /**
     * this method is return a copy of the source file
     * 
     * @param FileId
     *            source file identificateion
     * @return Return a copy of the source file
     * @author bxji
     */
    private FileBean copyFile(Long FileId) {
        if (FileId == null || 0l == FileId) {
            return null;
        }
        FileBean copy = null;
        try {
            FileBean file = fileDao.find(FileId);
            if (file != null) {
                copy = (FileBean) BeanUtils.cloneBean(file);
            }
        }
        catch (Exception e) {
            LOG.debug(" copy file throw exception", e);
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
        if (targetFolderId == null || 0l == targetFolderId
                || sourceFileId == null || 0l == sourceFileId) {
            return false;
        }
        try {
            Folder folder = folderDao.find(targetFolderId);
            FileBean file = fileDao.find(sourceFileId);

            // if target directory is current directory
            if (file.getFolder().equals(folder)) {
                return true;
            }

            // If there is a same name file in the target directory
            if (isSameName(folder, file.getFileName())) {
                autoRename(file);
            }

            // if target folder is shared directory
            if (folder.getShare()) {
                file.setShare(true);
            }
            else {
                file.setShare(false);
            }
            file.setFolder(folder);
            file.setDirectory(getDiskPath(folder));
            file.setModifyTime(new Date());
            // move disk file
            int result = DocumentUtil.moveFileTo(file, folder);
            if (result != 1) {
                LOG.debug("Move disk file IO exception");
                return false;
            }
            // update database table
            fileDao.modify(file);
            return true;
        }
        catch (Exception e) {
            LOG.debug("Database operation exception when moving a file. ", e);
            return false;
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
        if (fileId == null || 0l == fileId) {
            return false;
        }
        try {
            FileBean file = fileDao.find(fileId);
            if (file != null) {
                int result = DocumentUtil.removeFile(file);
                if (result != 1) {
                    LOG.debug("delete disk file fail.");
                    return false;
                }
                fileDao.deleteById(fileId);
            }
        }
        catch (Exception e) {
            LOG.debug("Database exception where tried to remove a file.", e);
            return false;
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
        if (fileId == null || 0l == fileId || Validity.isNullAndEmpty(newName)) {
            return false;
        }
        try {
            FileBean file = fileDao.find(fileId);
            if (file != null) {
                file.setFileName(newName);
                if (isSameName(file.getFolder(), newName)) {
                    autoRename(file);
                }
                int result = DocumentUtil.renameFile(file, newName);
                if (result != 1) {
                    LOG.debug("rename disk file fail.");
                    return false;
                }
                fileDao.modify(file);
                return true;
            }
        }
        catch (Exception e) {
            LOG.debug("update the database exception when rename a file", e);
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
     * Whether the same name with the current directory files
     * 
     * @param folder
     *            current directory
     * @param name
     *            name
     * @return have same name return 'true' else return 'false'
     * @throws DbException
     *             if folder is null or name is nul throw exception
     * @author bxji
     */
    private boolean isSameName(Folder folder, String name) throws DbException {
        if (folder == null || Validity.isNullAndEmpty(name)) {
            LOG.debug("target folder can not be null");
            throw new DbException("target folder and name can not be null.");
        }
        Set<FileBean> files = folder.getFiles();
        for (FileBean file : files) {
            if (name.equals(file.getFileName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * If the same name file in the directory , automatically add default suffix
     * 
     * @param file
     *            file object
     * @author bxji
     */
    private void autoRename(FileBean file) {
        if (file == null) {
            return;
        }
        String name = file.getFileName();
        int index = name.indexOf('.');
        String newName = name.substring(0, index) + Constant.FILE_COPY
                + name.substring(index, name.length());
        file.setFileName(newName);
    }
}
