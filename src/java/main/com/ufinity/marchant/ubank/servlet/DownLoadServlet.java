package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ufinity.marchant.ubank.common.Constant;

/**
 * 
 * download file servlet
 * 
 * @author jerome
 * @version 1.0
 * @since 2010-8-19
 */
public class DownLoadServlet extends AbstractServlet {
	
	private static final long serialVersionUID = 1L;
	// Logger for this class
    protected final Logger logger = Logger.getLogger(DownLoadServlet.class);
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownLoadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String method = parseActionName(request);
        String rslt = Constant.ERROR_PAGE;
        
        if(Constant.ACTION_DOWNLOAD.equals(method)){
            rslt = download(request, response);
        }
        
        forward(request, response, rslt);
	}

	/**
	 * 
	 * download the file
	 * 
	 * @param request
	 * @param response
	 */
	private String download(HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

}
