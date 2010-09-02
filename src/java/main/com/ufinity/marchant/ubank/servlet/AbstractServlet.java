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
package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufinity.marchant.ubank.Context;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.preferences.MessageResource;

/**
 * Abstract Servlet
 * 
 * @version 1.0 - 2010-8-19
 * @author zdxue
 */
@SuppressWarnings("serial")
public class AbstractServlet extends HttpServlet {

    /**
     * Parse action name from servlet path
     * 
     * @param req
     *            request
     * @return action name
     * @author zdxue
     */
    protected String parseActionName(HttpServletRequest req) {
        String servletPath = req.getServletPath();
        servletPath = servletPath.substring(servletPath.lastIndexOf("/") + 1,
                servletPath.lastIndexOf("."));
        return servletPath;
    }
    
    /**
     * page forward
     *
     * @param req request
     * @param resp response
     * @param path forward path
     * @throws ServletException 
     * @throws IOException 
     * @author zdxue
     */
    protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
        if(path != null) {
            req.getRequestDispatcher(path).forward(req, resp);            
        }
    }
    
    /**
     * Redirect 
     *
     * @param resp response
     * @param path path
     * @throws IOException 
     * @author zdxue
     */
    protected void redirect(HttpServletResponse resp, String path) throws IOException {
        if(path == null)
            path = "";
        
        resp.sendRedirect(path);
    }
    
    /**
     * Get locale 
     *
     * @return Locale
     * @author zdxue
     */
    protected Locale getLocale() {
        Context ac = Context.getContext();
        if(ac == null){
            return Locale.getDefault();
        }
        
        return (Locale)ac.get(Context.GLOBAL_LOCALE);
    }
    
    /**
     * Get text 
     *
     * @param key key
     * @return value
     * @author zdxue
     */
    protected String getText(String key) {
        return MessageResource.getText(key, getLocale());
    }
    
    /**
     * Get text with dynamic params
     *
     * @param key key 
     * @param objects params
     * @return value
     * @author zdxue
     */
    protected String getText(String key, String...objects) {
        return MessageResource.getText(key, objects);
    }
    
    /**
     * check login  
     *
     * @param req HttpServletRequest
     * @return if login return true, else return false
     * @author zdxue
     */
    protected boolean checkLogin(HttpServletRequest req) {
        if(req.getSession().getAttribute(Constant.SESSION_USER) != null) {
            return true;
        }
        
        return false;
    }
}
