package com.ufinity.marchant.ubank.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Logger, base on <code>org.apache.commons.logging.Log</code>
 * 
 * @version 1.0 - 2010-6-12
 * @author zdxue
 */
public final class Logger {
    private Log log;

    private Logger(Log log) {
        this.log = log;
    }

    public static Logger getInstance(Class<?> clz) {
        return new Logger(LogFactory.getLog(clz));
    }

    public void debug(Object obj) {
        if(log.isDebugEnabled()) {
            log.debug(obj);            
        }
    }
    
    public void debug(Object obj, Throwable t) {
        if(log.isDebugEnabled()) {
            log.debug(obj, t);            
        }
    }
    
    public void info(Object obj) {
        if(log.isInfoEnabled()) {
            log.info(obj);
        }
    }
    
    public void info(Object obj, Throwable t) {
        if(log.isInfoEnabled()) {
            log.info(obj, t);
        }
    }
    
    public void warn(Object obj) {
        if(log.isWarnEnabled()) {
            log.warn(obj);
        }
    }
    
    public void warn(Object obj, Throwable t) {
        if(log.isWarnEnabled()) {
            log.warn(obj, t);
        }
    }
    
    public void fatal(Object obj) {
        if(log.isFatalEnabled()) {
            log.fatal(obj);
        }
    }
    
    public void fatal(Object obj, Throwable t) {
        if(log.isFatalEnabled()) {
            log.fatal(obj, t);
        }
    }
    
    public void error(Object obj) {
        if(log.isErrorEnabled()) {
            log.error(obj);
        }
    }
    
    public void error(Object obj, Throwable t) {
        if(log.isErrorEnabled()) {
            log.error(obj, t);
        }
    }
    
    public void trace(Object obj) {
        if(log.isTraceEnabled()) {
            log.trace(obj);
        }
    }
    
    public void trace(Object obj, Throwable t) {
        if(log.isTraceEnabled()) {
            log.trace(obj, t);
        }
    }
}
