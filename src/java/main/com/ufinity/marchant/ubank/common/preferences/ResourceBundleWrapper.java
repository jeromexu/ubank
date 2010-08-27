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
package com.ufinity.marchant.ubank.common.preferences;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ResourceBundleWrapper
 * 
 * @version 1.0 - 2010-8-19
 * @author zdxue
 */
public class ResourceBundleWrapper {
    private ResourceBundle rb;

    /**
     * Constructor with baseName and locale
     * 
     * @param baseName config file base
     * @param locale locale
     * @author zdxue
     */
    public ResourceBundleWrapper(String baseName, Locale locale) {
        try{
            rb = ResourceBundle.getBundle(baseName, locale);
        }catch(Exception e) {
            throw new RuntimeException("init ResourceBundle exception with - " + baseName);
        }
    }

    /**
     * Get value by key, if cannot get config value, then return empty string
     *
     * @param key the given key
     * @return value
     * @author zdxue
     */
    public String getValue(String key) {
        try{
            return rb.getString(key);
        }catch(Exception e) {
            return "";
        }
    }

    /**
     * Get value with dynamic params
     * 
     * @param key key
     * @param arguments arguments
     * @return value
     * @author zdxue
     */
    public String getValue(String key, Object...arguments) {
        try{
            return MessageFormat.format(rb.getString(key), arguments);
        }catch(Exception e) {
            return "";
        }
    }
}
