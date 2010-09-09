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
package com.ufinity.marchant.ubank.vo;

/**
 * this is a general entity of the folder and fileBean object used to convert
 * the json data
 * 
 * @author bxji
 * @version 2010-8-23
 */
public class FileOrFolderJsonEntity {

    private Long id;
    private Long pid;
    private String name;
    private String modTime;
    private String type;
    private String dir;
    private String size;
    private boolean init;
    private Long layer;

    /**
     * the getter method of layer
     * 
     * @return the layer
     */
    public Long getLayer() {
        return layer;
    }

    /**
     * the setter method of the layer
     * 
     * @param layer
     *            the layer to set
     */
    public void setLayer(Long layer) {
        this.layer = layer;
    }

    /**
     * @return the init
     */
    public boolean isInit() {
        return init;
    }

    /**
     * @param init
     *            the init to set
     */
    public void setInit(boolean init) {
        this.init = init;
    }

    /**
     * @return the pid
     */
    public Long getPid() {
        return pid;
    }

    /**
     * @param pid
     *            the pid to set
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * the getter method of id
     * 
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * the setter method of the id
     * 
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * the getter method of name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * the setter method of the name
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * the getter method of type
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * the setter method of the type
     * 
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * the getter method of modTime
     * 
     * @return the modTime
     */
    public String getModTime() {
        return modTime;
    }

    /**
     * the setter method of the modTime
     * 
     * @param modTime
     *            the modTime to set
     */
    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    /**
     * the getter method of dir
     * 
     * @return the dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * the setter method of the dir
     * 
     * @param dir
     *            the dir to set
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * the getter method of size
     * 
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * the setter method of the size
     * 
     * @param size
     *            the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

}
