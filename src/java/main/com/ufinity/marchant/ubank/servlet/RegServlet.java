package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.captcha.MyCaptchaService;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.service.ServiceFactory;
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
	
	// Logger for this class
    protected final Logger logger = Logger.getLogger(RegServlet.class);
	private static final long serialVersionUID = 1L;
	private UserService userService = ServiceFactory.getInstance().getUserService();
	private ImageCaptchaService imageCaptchaService = MyCaptchaService.getInstance();
	private final String  USERNAME_ERR = "userName_error_msg";
	private final String  PASS_ERR = "pass_error_msg";
	private final String  REPASS_ERR = "repass_error_msg";
	private final String  CAPTCHA_ERR = "captcha_error_msg";
	private final String  REGISTER_MSG = "register_msg";
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
	 * @param request req
	 * @param response res
	 * @author jerome
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		try{
			String captchaCode = request.getParameter("captchaCode");
			if(logger.isDebugEnabled()){
				logger.debug("captchaCode = "+captchaCode);
			}
			String captchaId = request.getSession().getId();
			Boolean isValidateCode = false;
			try {
				// Call the Service method
				isValidateCode = imageCaptchaService.validateResponseForID(captchaId, captchaCode);
			} catch (CaptchaServiceException e) {
				// should not happen, may be thrown if the id is not valid
				logger.error(e.getMessage());
			}
			if (!isValidateCode) {
				//captcha code is not right
				request.setAttribute(CAPTCHA_ERR, Constant.CAPTCHA_ERR_MSG);
			}
			String userName = request.getParameter("userName");
			String pass = request.getParameter("password");
			String repass = request.getParameter("repassword");
			if (logger.isDebugEnabled()) {
				logger.debug("userName = "+userName);
				logger.debug("pass = "+pass);
				logger.debug("repass = "+repass);
			}
			if (null == userName || "".equals(userName.trim())) {
				request.setAttribute(USERNAME_ERR, Constant.USERNAME_ERR_MSG);
			} 
			if (null == pass || "".equals(pass.trim())) {
				request.setAttribute(PASS_ERR, Constant.PASS_ERR_MSG);
			} else if (!pass.equals(repass)) {
				request.setAttribute(REPASS_ERR, Constant.REPASS_ERR_MSG);
			}
			User user = new User();
			user.setUserName(userName);
			user.setPassword(pass);
			user.setCreateTime(new Date());
			user.setOverSize(Constant.ONE_G_SPACE);
			String registerMsg = userService.doRegister(user);
			request.setAttribute(REGISTER_MSG, registerMsg);
			request.getRequestDispatcher("register.jsp").forward(request, response);
		}catch(Exception e){
			logger.error("error message :"+e.getMessage());
			try {
				response.sendRedirect(request.getContextPath()+"/common/error.jsp");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}

}
