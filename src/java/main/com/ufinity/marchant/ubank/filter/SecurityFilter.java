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
package com.ufinity.marchant.ubank.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.CollectionUtil;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.exception.NoAuthorizationException;

/**
 * SecurityFilter
 * 
 * @author zdxue
 */
public class SecurityFilter implements Filter {
    private List<String> needCheckedPaths;
    private List<String> exceptPaths;

    /**
     * Destroy
     */
	public void destroy() {
	    needCheckedPaths.clear();
	    exceptPaths.clear();
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 * @throws IOException 
	 * @throws ServletException 
	 * @author zdxue
	 */
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

	    HttpServletRequest request = (HttpServletRequest)req;
	    String currentPath = request.getRequestURI();
	    if((urlPatternValidate(currentPath, needCheckedPaths)) && (!urlPatternValidate(currentPath, exceptPaths))){
	        User user = (User)request.getSession().getAttribute(Constant.SESSION_USER);
	        if(user==null) {
	            throw new NoAuthorizationException("NoLogin");
	        }
	    }
		
	    chain.doFilter(req, resp);
	}

	/**
	 * init
	 * @throws ServletException 
	 */
	public void init(FilterConfig config) throws ServletException {
	    needCheckedPaths = new ArrayList<String>();
	    exceptPaths = new ArrayList<String>();
	    
	    String needCheckedPath = config.getInitParameter("needCheckedPaths");
	    String exceptPath = config.getInitParameter("exceptPaths");
	    
	    if(Validity.isNotEmpty(needCheckedPath)) {
	        CollectionUtil.add(needCheckedPaths, needCheckedPath.split(","));
	    }
	    
	    if(Validity.isNotEmpty(exceptPath)) {
            CollectionUtil.add(exceptPaths, exceptPath.split(","));
        }
	}
	
	/**
	 * url pattern check 
	 *
	 * @param url current request url
	 * @param urlPatternList urlPattern list
	 * @return if matched return true
	 * @author zdxue
	 */
	private boolean urlPatternValidate(String url, List<String> urlPatternList) {
	    if(url == null || urlPatternList == null){
	        return false;
	    }
	    
	    for(String regex : urlPatternList) {
	        if(url.contains(regex)) {
	            return true;
	        }
	    }
	    
	    return false;
	}
}
