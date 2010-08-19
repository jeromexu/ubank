package com.ufinity.marchant.ubank.service;

import com.ufinity.marchant.ubank.service.impl.UserServiceImpl;

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
        //TODO
        return new UserServiceImpl();
    }

}

