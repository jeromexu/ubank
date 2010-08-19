package com.ufinity.marchant.ubank.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

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
    public String parseActionName(HttpServletRequest req) {
        String servletPath = req.getServletPath();
        servletPath = servletPath.substring(servletPath.lastIndexOf("/") + 1,
                servletPath.lastIndexOf("."));
        return servletPath;
    }
}
