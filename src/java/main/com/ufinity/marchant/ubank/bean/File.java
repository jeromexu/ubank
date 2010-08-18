// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:WenQiang Wu
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
package com.ufinity.marchant.ubank.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
@Entity
@Table(name = "U_FILE")
public class File implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1134837226969157586L;
    private Long fileId;
    private String fileName;
    private Date createTime;
    private Date modifyTime;
    private String fileType;
    private String directory;
    private Long size;
    private Boolean share;

    private Folder folder;

    /**
     * @return the fileId
     */
    @Column(name = "FILE_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getFileId() {
        return fileId;
    }

    /**
     * @param fileId
     *            the fileId to set
     */
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    /**
     * @return the fileName
     */
    @Column(name = "FILE_NAME", nullable = false)
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the createTime
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_TIME", nullable = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the modifyTime
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "MODIFIED_TIME", nullable = false)
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     *            the modifyTime to set
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return the fileType
     */
    @Column(name = "FILE_TYPE", nullable = false)
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType
     *            the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the directory
     */
    @Column(name = "DIRECTORY", nullable = false)
    public String getDirectory() {
        return directory;
    }

    /**
     * @param directory
     *            the directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * @return the size
     */
    @Column(name = "SIZE", nullable = false)
    public Long getSize() {
        return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * @return the share
     */
    @Column(name = "SHARE", nullable = false)
    public Boolean getShare() {
        return share;
    }

    /**
     * @param share
     *            the share to set
     */
    public void setShare(Boolean share) {
        this.share = share;
    }

    /**
     * @return the folder
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE }, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLDER_ID", nullable = false)
    public Folder getFolder() {
        return folder;
    }

    /**
     * @param folder
     *            the folder to set
     */
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

}
