package com.ufinity.marchant.ubank.common.preferences;


/**
 * System global configurations
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public final class SystemGlobals {

    private static final UBankResourceBundle RB = new UBankResourceBundle("SystemGlobals"); 

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

}

