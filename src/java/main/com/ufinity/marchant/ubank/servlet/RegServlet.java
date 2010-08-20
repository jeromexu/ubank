package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.captcha.MyCaptchaService;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageResource;
import com.ufinity.marchant.ubank.service.ServiceRetrieve;
import com.ufinity.marchant.ubank.service.UserService;

/**
 * 
 * user register module
 * 
 * @author jerome
 * @version 1.0
 * @since 2010-8-19
 */
public class RegServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	// Logger for this class
	protected final Logger logger = Logger.getLogger(RegServlet.class);
	
	// user service business logic instance

	private UserService userService = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegServlet() {
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
		processRequest(request, response);
	}

	/**
	 * 
	 * display register
	 * 
	 * @param request
	 *            req
	 * @param response
	 *            res
	 * @author jerome
	 */
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String captchaCode = request.getParameter(Constant.REQ_PARAM_CAPTCHACODE);
			if (logger.isDebugEnabled()) {
				logger.debug("captchaCode = " + captchaCode);
			}
			HttpSession session = request.getSession();   
			boolean isValidateCode = false;   
			// retrieve the response   
			String validateCode = captchaCode.trim();   
			isValidateCode = MyCaptchaService.getInstance().validateCaptchaResponse(validateCode, session);   
			if (!isValidateCode) {
				// captcha code is not right
				request.setAttribute(Constant.CAPTCHA_ERR, MessageResource
						.getMessage(MessageKeys.CAPTCHA_ERR_MSG));
			} else {
				String userName = request.getParameter(Constant.REQ_PARAM_USERNAME);
				String pass = request.getParameter(Constant.REQ_PARAM_PASSWORD);
				String repass = request.getParameter(Constant.REQ_PARAM_REPASSWORD);
				if (logger.isDebugEnabled()) {
					logger.debug("userName = " + userName);
					logger.debug("pass = " + pass);
					logger.debug("repass = " + repass);
				}
				if (null == userName || "".equals(userName.trim())) {
					request.setAttribute(Constant.USERNAME_ERR, MessageResource
							.getMessage(MessageKeys.USERNAME_ERR_MSG));
				}
				if (null == pass || "".equals(pass.trim())) {
					request.setAttribute(Constant.PASS_ERR, MessageResource
							.getMessage(MessageKeys.PASS_ERR_MSG));
				} else if (!pass.equals(repass)) {
					request.setAttribute(Constant.REPASS_ERR, MessageResource
							.getMessage(MessageKeys.REPASS_ERR_MSG));
				}
				User user = new User();
				user.setUserName(userName);
				user.setPassword(pass);
				user.setCreateTime(new Date());
				user.setOverSize(Constant.ONE_G_SPACE);
				userService = ServiceRetrieve.retrieve(UserService.class, ConfigKeys.SERVICE_USER);
				String registerMsg = userService.doRegister(user);
				request.setAttribute(Constant.REGISTER_MSG, registerMsg);
			}

			forward(request, response, Constant.REGISTER_PAGE);
		} catch (Exception e) {
			logger.error("error message :" + e.getMessage());
			try {
				response.sendRedirect(request.getContextPath()
						+ Constant.ERROR_PAGE);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

}
