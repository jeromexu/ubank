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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
@Entity
@Table(name = "U_FOLDER")
@NamedQueries( { @NamedQuery(name = "Folder.findRootRolderByUserId", query = "SELECT f FROM Folder AS f WHERE f.parent is null AND f.user.userId=:userId") })
public class Folder implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6403122592892326471L;

    private Long folderId;
    private String folderName;
    private Date createTime;
    private Date modifyTime;
    private String directory;
    private Boolean share;
    private String folderType;
    private Integer repeatCount;
    
    private Folder parent;
    private Set<Folder> children = new HashSet<Folder>();

    private User user;
    private Set<FileBean> files = new HashSet<FileBean>();

    /**
     * @return the folderId
     */
    @Column(name = "FOLDER_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getFolderId() {
        return folderId;
    }

    /**
     * @param folderId
     *            the folderId to set
     */
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    /**
     * @return the folderName
     */
    @Column(name = "FOLDER_NAME", nullable = false)
    public String getFolderName() {
        return folderName;
    }

    /**
     * @param folderName
     *            the folderName to set
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * @return the createTime
     */
    @Temporal(TemporalType.TIMESTAMP)
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_TIME")
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
     * @return the directory
     */
    @Column(name = "DIRECTORY", nullable = false, length = 2000)
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
     * @return the share
     */
    @Column(name = "SHARE", nullable = false, columnDefinition = "bool default false")
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
     * @return the folderType
     */
    @Column(name = "FOLDER_TYPE", length = 10, nullable = false)
    public String getFolderType() {
        return folderType;
    }

    /**
     * @param folderType
     *            the folderType to set
     */
    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }
    
    
    /**
     * @return the repeatCount
     */
    @Column(name = "REPEAT_COUNT")
    public Integer getRepeatCount() {
        return repeatCount;
    }

    /**
     * @param repeatCount the repeatCount to set
     */
    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    /**
     * @return the parent
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    public Folder getParent() {
        return parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(Folder parent) {
        this.parent = parent;
    }

    /**
     * @return the children
     */
    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "parent")
    @OrderBy("folderId desc")
    public Set<Folder> getChildren() {
        return children;
    }

    /**
     * @param children
     *            the children to set
     */
    public void setChildren(Set<Folder> children) {
        this.children = children;
    }

    /**
     * @return the user
     */
    @ManyToOne(cascade = { CascadeType.REFRESH }, optional = true)
    @JoinColumn(name = "USER_ID", nullable = false)
    public User getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the files
     */
    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "folder")
    public Set<FileBean> getFiles() {
        return files;
    }

    /**
     * @param files
     *            the files to set
     */
    public void setFiles(Set<FileBean> files) {
        this.files = files;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (null != obj && obj instanceof Folder) {
            return this.folderId.equals(((Folder) obj).getFolderId());
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return null == this.folderId ? super.hashCode() : this.folderId
                .hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Folder : folderId = " + this.getFolderId() + "\tfolderName = "
                + this.getFolderName() + "\tshare = " + this.getShare()
                + "\tdirectory = " + this.getDirectory();
    }

}
