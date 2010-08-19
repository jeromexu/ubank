package com.ufinity.marchant.ubank.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.ufinity.marchant.ubank.common.Constant;

/**
 * Charset filter
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public class CharsetFilter implements Filter {
    private String enable;
    private String encode;

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     * @throws IOException 
     * @throws ServletException  
     */
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {
        if(Constant.ENABLE_FILTER.equals(enable)) {
            HttpServletRequest request = (HttpServletRequest)req;
            request.setCharacterEncoding(encode);
        }
        
        chain.doFilter(req, resp);
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     * @throws ServletException 
     */
    public void init(FilterConfig config) throws ServletException {
        enable = config.getInitParameter(Constant.ENABLE);
        encode = config.getInitParameter(Constant.ENCODE);
    }

}

