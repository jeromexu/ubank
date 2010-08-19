package com.ufinity.marchant.ubank.common.preferences;

import java.util.ResourceBundle;

/**
 * System global configurations
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public final class SystemGlobals {

    private static final ResourceBundle RB = ResourceBundle.getBundle("SystemGlobals"); 

    private SystemGlobals(){}

    /**
     * Get string value by key, if cannot get config value, then return empty string
     *
     * @param key key
     * @return value
     * @author zdxue
     */
    public static String getString(String key) {
        try{
            return RB.getString(key);
        }catch(Exception e) {
            return "";
        }
    }

}

