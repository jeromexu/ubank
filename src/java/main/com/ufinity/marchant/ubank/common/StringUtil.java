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
package com.ufinity.marchant.ubank.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * StringUtil
 * 
 * @version 1.0 - 2010-8-23
 * @author zdxue
 */
public class StringUtil {

    /**
     * Parse string to int, if parse occur exception, then return default value
     * 
     * @param s
     *            string
     * @param defVal
     *            if parse occur exception, then return default value
     * @return int value
     * @author zdxue
     */
    public static int parseInt(String s, int defVal) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return defVal;
        }
    }

    /**
     * Parse string to int , if occur exception , then return 0
     * 
     * @param s
     *            string
     * @return int value, if parse occur exception, then return default value
     * @author zdxue
     */
    public static int parseInt(String s) {
        return parseInt(s, 0);
    }

    /**
     * upper the first char of the source
     * 
     * @param source
     * @return String converted str
     * @author yonghui
     */
    public static String makeFirstCharUpper(String source) {
        if (null == source || source.trim().length() == 0) {
            return null;
        }
        return (source.charAt(0) + "").toUpperCase() + source.substring(1);
    }

    /**
     * Process filename as follow : 
     * <ul>
     *  <li>fileName contains chinese character</li>
     *  <li>fileName contains space character: in this case, 
     *  while use firefox to download, its will cut off the fileName at space char.</li>
     * </ul>
     *
     * 
     * @param fileName fileName
     * @param agent agent from request
     * @return needed fileName, if fileName empty return empty string
     * @throws UnsupportedEncodingException if charset not supported, throw it
     * @author zdxue
     */
    public static String processFileName(String fileName, String agent)
            throws UnsupportedEncodingException {
        if (Validity.isEmpty(fileName)) {
            return "";
        }

        if (null != agent && agent.indexOf("MSIE") != -1) {
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        } else if (null != agent && agent.indexOf("Mozilla") != -1) {
            fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
        }

        return fileName;
    }
}
