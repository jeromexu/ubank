package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     * forward
     *
     * @param req request
     * @param resp response
     * @param path forward path
     * @throws ServletException 
     * @throws IOException 
     * @author zdxue
     */
    protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
}
