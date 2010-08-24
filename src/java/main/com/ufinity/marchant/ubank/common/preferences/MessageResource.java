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

import java.util.Locale;

/**
 * Message resources for ubank
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public final class MessageResource {
    
    private static final UBankResourceBundle RB = new UBankResourceBundle("MessageResources", Locale.getDefault());
    
    /**
     * Private Constructor
     */
    private MessageResource(){}
    
    /**
     * Get message from properties file 
     *
     * @param key key
     * @return message
     * @author zdxue
     */
    public static String getMessage(String key) {
        return RB.getValue(key);
    }
    
    /**
     * Get message from properties file 
     * 
     * @param key key
     * @param params replace param like {0},{1}...
     * @return message
     * @author liujun
     */
    public static String getMessage(String key, String[] params) {
        String value = getMessage(key);
        for (int i = 0; i < params.length; i++) {
            value = value.replace("{"+i+"}", params[i]);
        }
        return value;
    }
}

