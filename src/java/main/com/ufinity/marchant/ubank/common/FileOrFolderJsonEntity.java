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
package com.ufinity.marchant.ubank.common;

import java.util.Date;

/**
 * {description of method or object}
 * 
 * @author bxji
 * @version 2010-8-23
 */
public class FileOrFolderJsonEntity {
    public static final String TYPE_FILE = "file";
    public static final String TYPE_FOLDER = "folder";

    private Long id;
    private String name;
    private Date modifyTime;
    private String type;
    private String directory;
    private Long size;
    
    /**
     * the getter method of id
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * the setter method of the id
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * the getter method of name
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * the setter method of the name
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * the getter method of modifyTime
     * @return the modifyTime
     */
    public Date getModifyTime() {
        return modifyTime;
    }
    /**
     * the setter method of the modifyTime
     * @param modifyTime the modifyTime to set
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    /**
     * the getter method of type
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * the setter method of the type
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * the getter method of directory
     * @return the directory
     */
    public String getDirectory() {
        return directory;
    }
    /**
     * the setter method of the directory
     * @param directory the directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    /**
     * the getter method of size
     * @return the size
     */
    public Long getSize() {
        return size;
    }
    /**
     * the setter method of the size
     * @param size the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }
    
    

}
