package com.ufinity.marchant.ubank.service;

import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;

/**
 * Service Factory
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public final class ServiceFactory {

    private static final ServiceFactory SF = new ServiceFactory();

    private ServiceFactory(){}

    /**
     * Get ServiceFactory instance 
     *
     * @return ServiceFactory obj
     * @author zdxue
     */
    public static ServiceFactory getInstance() {
        return SF;
    } 

    /**
     * Get UserService
     *  
     * @return UserService impl
     * @author zdxue
     */
    public UserService getUserService() {
        try{
            return (UserService)Class.forName(SystemGlobals.getString(ConfigKeys.SERVICE_USER)).newInstance();
        }catch(Exception e) {
            throw new RuntimeException("get UserService implements exception.");
        }
    }
    
    /**
     * Get FileService
     *  
     * @return FileService impl
     * @author zdxue
     */
    public FileService getFileService() {
        try{
            return (FileService)Class.forName(SystemGlobals.getString(ConfigKeys.SERVICE_FILE)).newInstance();
        }catch(Exception e) {
            throw new RuntimeException("get FileService implements exception.");
        }
    }

}

