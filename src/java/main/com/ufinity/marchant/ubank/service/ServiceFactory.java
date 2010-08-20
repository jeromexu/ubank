package com.ufinity.marchant.ubank.service;

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
     * @param serviceKey Service interface key
     * @return Service interface instance
     * @author zdxue
     */
    public static <T> T createService(Class<T> serviceClz, String serviceKey) {
        return ObjectRetrieve.retrieve(serviceClz, serviceKey);
    }
}

