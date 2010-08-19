package com.ufinity.marchant.ubank.common.preferences;


/**
 * Message resources for ubank
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public final class MessageResource {
    
    private static final UBankResourceBundle RB = new UBankResourceBundle("MessageResources");
    
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
}

