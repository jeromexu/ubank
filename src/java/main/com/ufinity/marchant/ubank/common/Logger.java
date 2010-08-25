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

    /**
     * Get Logger instance
     * 
     * @param clz
     *            Be loged class
     * @return Logger obj
     * @author zdxue
     */
    public static Logger getInstance(Class<?> clz) {
        return new Logger(LogFactory.getLog(clz));
    }

    /**
     * Debug
     * 
     * @param obj
     * @author zdxue
     */
    public void debug(Object obj) {
        if (log.isDebugEnabled()) {
            log.debug(obj);
        }
    }

    /**
     * Debug
     * 
     * @param obj
     * @param t
     * @author zdxue
     */
    public void debug(Object obj, Throwable t) {
        if (log.isDebugEnabled()) {
            log.debug(obj, t);
        }
    }

    /**
     * Info
     * 
     * @param obj
     *            info obj
     * @author zdxue
     */
    public void info(Object obj) {
        if (log.isInfoEnabled()) {
            log.info(obj);
        }
    }

    /**
     * Info
     * 
     * @param obj
     *            info obj
     * @param t
     *            exception obj
     * @author zdxue
     */
    public void info(Object obj, Throwable t) {
        if (log.isInfoEnabled()) {
            log.info(obj, t);
        }
    }

    /**
     * Warn
     * 
     * @param obj
     *            warn obj
     * @author zdxue
     */
    public void warn(Object obj) {
        if (log.isWarnEnabled()) {
            log.warn(obj);
        }
    }

    /**
     * Warn
     * 
     * @param obj
     * @param t
     * @author zdxue
     */
    public void warn(Object obj, Throwable t) {
        if (log.isWarnEnabled()) {
            log.warn(obj, t);
        }
    }

    /**
     * Fatal
     * 
     * @param obj
     * @author zdxue
     */
    public void fatal(Object obj) {
        if (log.isFatalEnabled()) {
            log.fatal(obj);
        }
    }

    /**
     * Fatal
     * 
     * @param obj
     * @param t
     * @author zdxue
     */
    public void fatal(Object obj, Throwable t) {
        if (log.isFatalEnabled()) {
            log.fatal(obj, t);
        }
    }

    /**
     * Error
     * 
     * @param obj
     * @author zdxue
     */
    public void error(Object obj) {
        if (log.isErrorEnabled()) {
            log.error(obj);
        }
    }

    /**
     * Error
     * 
     * @param obj
     * @param t
     * @author zdxue
     */
    public void error(Object obj, Throwable t) {
        if (log.isErrorEnabled()) {
            log.error(obj, t);
        }
    }

    /**
     * Trace
     * 
     * @param obj
     * @author zdxue
     */
    public void trace(Object obj) {
        if (log.isTraceEnabled()) {
            log.trace(obj);
        }
    }

    /**
     * Trace
     * 
     * @param obj
     * @param t
     * @author zdxue
     */
    public void trace(Object obj, Throwable t) {
        if (log.isTraceEnabled()) {
            log.trace(obj, t);
        }
    }
}
