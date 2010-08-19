package com.ufinity.marchant.ubank.common.preferences;

import java.util.ResourceBundle;

/**
 * Description of the class
 * 
 * @version 1.0 - 2010-8-19
 * @author zdxue
 */
public class UBankResourceBundle {
    private ResourceBundle rb;

    /**
     * Constructor
     * 
     * @author zdxue
     */
    public UBankResourceBundle(String baseName) {
        try{
            rb = ResourceBundle.getBundle(baseName);
        }catch(Exception e) {
            throw new RuntimeException("init ResourceBundle exception with - " + baseName);
        }
    }

    /**
     * Get string value by key, if cannot get config value, then return empty string
     *
     * @param key key
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

}
