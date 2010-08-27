// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:bixiang Ji
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

import java.util.ArrayList;
import java.util.List;

/**
 * this class is a directory tree node of Json data
 * 
 * @author bxji
 * @version 2010-8-17
 */
public class JsonNode {

    private Long id;
    private String text;
    private String state;
    private Long uid;
    /**
     * the getter method of uid
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * the setter method of the uid
     * @param uid the uid to set
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    private List<JsonNode> children;
    
    /**
     * 
     * Constructor for JsonNode
     */
    public JsonNode(){
        this.children = new ArrayList<JsonNode>();
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
     * the getter method of text
     * 
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * the setter method of the text
     * 
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * the getter method of state
     * 
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * the setter method of the state
     * 
     * @param state
     *            the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * the getter method of children
     * 
     * @return the children
     */
    public List<JsonNode> getChildren() {
        return children;
    }

    /**
     * the setter method of the children
     * 
     * @param children
     *            the children to set
     */
    public void setChildren(List<JsonNode> children) {
        this.children = children;
    }

}
