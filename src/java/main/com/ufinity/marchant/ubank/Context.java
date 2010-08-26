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
package com.ufinity.marchant.ubank;

import java.util.Map;

/**
 * Context for per request.
 * Bundle a context to a request thread.
 * 
 * @author zdxue
 */
public class Context {
	private Map<String, Object> context;
	
	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public Context(Map<String, Object> context) {
		this.context = context;
	}
	
	/**
	 * put value in request context 
	 *
	 * @param key key
	 * @param value value
	 * @author zdxue
	 */
	public void put(String key, Object value) {
		if(context != null)
			context.put(key, value);
	}
	
	/**
	 * Get value from request context
	 *
	 * @param key key
	 * @return value
	 * @author zdxue
	 */
	public Object get(String key) {
		if(context != null)
			return context.get(key);
		else
			return null;
	}
	
	public static final String GLOBAL_LOCALE = "global_local";
	
	/** use threadlocal to store per thread context */
	private static final ThreadLocal<Context> CONTEXT_THREADLOCAL = new ThreadLocal<Context>();
	
	/**
	 * Bundle thread context to threadlocal
	 *
	 * @param context
	 * @author zdxue
	 */
	public static void setContext(Context context) {
		CONTEXT_THREADLOCAL.set(context);
	}
	
	public static Context getContext() {
		return CONTEXT_THREADLOCAL.get();
	}
}
