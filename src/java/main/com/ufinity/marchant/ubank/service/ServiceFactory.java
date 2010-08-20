package com.ufinity.marchant.ubank.service;

import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.ObjectRetrieve;

/**
 * ServiceFactory
 *
 * @version 1.0 - 2010-8-20
 * @author zdxue     
 */
public final class ServiceFactory {

    /**
     * Private constructor
     */
    private ServiceFactory(){
        
    }
    
    /**
     * create Service interface's impl instance
     *
     * @param <T> Service interface
     * @param serviceClz Service interface class
     * @return Service interface impl instance
     * @author zdxue
     */
    public static <T> T createService(Class<T> serviceClz) {
        String serviceKey = "";
        
        if(UserService.class.equals(serviceClz)) {
            serviceKey = ConfigKeys.SERVICE_USER;
        }else if(FileService.class.equals(serviceClz)) {
            serviceKey = ConfigKeys.SERVICE_FILE;
        }
        
        return ObjectRetrieve.retrieve(serviceClz, serviceKey);
    }
}

