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

import com.ufinity.marchant.ubank.common.StringUtil;

/**
 * System global preferences configurations
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public final class SystemGlobals {

    private static final ResourceBundleWrapper RB = new ResourceBundleWrapper("SystemGlobals", Locale.US); 

    /**
     * Private Constructor
     */
    private SystemGlobals(){}

    /**
     * Get string value by key, if cannot get config value, then return empty string
     *
     * @param key key
     * @return value
     * @author zdxue
     */
    public static String getString(String key) {
        return RB.getValue(key);
    }

    /**
     * Get int value by key, if the value is not a number, then return 0.
     *
     * @param key key
     * @return int value.  if the value is not a number, then return 0.
     * @author zdxue
     */
    public static int getInt(String key) {
        String str = getString(key);
        return StringUtil.parseInt(str);
    }
}

