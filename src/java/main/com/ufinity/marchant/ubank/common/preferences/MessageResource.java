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

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import com.ufinity.marchant.ubank.Context;

/**
 * Message resources
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public final class MessageResource {
    private static final String BASE_NAME = SystemGlobals.getString(ConfigKeys.I18N_BASE_NAME);
    private static final Map<Locale, ResourceBundleWrapper> BUNDLES = new Hashtable<Locale, ResourceBundleWrapper>();

    /**
     * Private Constructor
     */
    private MessageResource(){}

    /**
     * Get text by key and locale 
     *
     * @param key key 
     * @param locale locale
     * @return value
     * @author zdxue
     */
    public static String getText(String key, Locale locale) {
        if(locale == null) {
            locale = Locale.getDefault();
        }

        if(!BUNDLES.containsKey(locale)){
            BUNDLES.put(locale, new ResourceBundleWrapper(BASE_NAME, locale));
        }

        return BUNDLES.get(locale).getValue(key);
    }

    /**
     * Get text by key and Context's GLOBAL_LOCALE with params
     *
     * @param key key
     * @param params params
     * @return value
     * @author zdxue
     */
    public static String getText(String key, String...params) {
        Context ac = Context.getContext();
        Locale locale = Locale.getDefault();

        if(ac != null)
            locale = (Locale)ac.get(Context.GLOBAL_LOCALE);

        if(!BUNDLES.containsKey(locale)){
            BUNDLES.put(locale, new ResourceBundleWrapper(BASE_NAME, locale));
        }

        return BUNDLES.get(locale).getValue(key, params);
    }

    /**
     * Get text
     *
     * @param key key
     * @return text
     * @author zdxue
     */
    public static String getText(String key) {
        return getText(key, new String[]{});
    }
    
}

