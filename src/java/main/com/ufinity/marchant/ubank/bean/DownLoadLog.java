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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @version Sep 1, 2010
 */
@Entity
@Table(name = "U_DOWNLOAD_LOG")
public class DownLoadLog {

    private Long downLoadLogId;
    private Date downLoadTime;

    private User user;
    private FileBean file;

    /**
     * @return the downLoadLogId
     */
    @Column(name = "DOWNLOAD_LOG_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getDownLoadLogId() {
        return downLoadLogId;
    }

    /**
     * @param downLoadLogId
     *            the downLoadLogId to set
     */
    public void setDownLoadLogId(Long downLoadLogId) {
        this.downLoadLogId = downLoadLogId;
    }

    /**
     * @return the downLoadTime
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOWNLOAD_TIME", nullable = false)
    public Date getDownLoadTime() {
        return downLoadTime;
    }

    /**
     * @param downLoadTime
     *            the downLoadTime to set
     */
    public void setDownLoadTime(Date downLoadTime) {
        this.downLoadTime = downLoadTime;
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
     * @return the file
     */
    @ManyToOne(cascade = { CascadeType.REFRESH }, optional = true)
    @JoinColumn(name = "FILE_BEAN_ID", nullable = false)
    public FileBean getFile() {
        return file;
    }

    /**
     * @param file
     *            the file to set
     */
    public void setFile(FileBean file) {
        this.file = file;
    }

}
