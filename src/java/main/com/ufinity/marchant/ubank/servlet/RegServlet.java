package com.ufinity.marchant.ubank.servlet;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.captcha.MyCaptchaService;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.Validity;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
import com.ufinity.marchant.ubank.service.ServiceFactory;
import com.ufinity.marchant.ubank.service.UserService;

/**
 * user register module
 * 
 * @author jerome
 * @version 1.0
 * @since 2010-8-19
 */
public class RegServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	// Logger for this class
	protected final Logger logger = Logger.getInstance(RegServlet.class);

	// user service business logic instance
	private UserService userService = null;

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
        String rslt = Constant.ERROR_PAGE_500;
        if (Constant.ACTION_REGISTER.equals(method)) {
            rslt = register(request);
        }
        forward(request, response, rslt);
    }

	/**
	 * display register
	 * 
	 * @param request
	 *            req
	 * @param response
	 *            res
	 * @author jerome
	 */
	private String register(HttpServletRequest request) {

		try {
			String captchaCode = request
					.getParameter(Constant.REQ_PARAM_CAPTCHACODE);
			logger.debug(" request captchaCode = " + captchaCode);
			if (Validity.isNullAndEmpty(captchaCode)) {
				request.setAttribute(Constant.ATTR_ERROR_MSG,
						getText(MessageKeys.CAPTCHA_ERR_MSG));
				return Constant.REGISTER_PAGE;
			}
			HttpSession session = request.getSession();
			boolean isValidateCode = false;
			// retrieve the response
			String validateCode = captchaCode.trim();
			isValidateCode = MyCaptchaService.getInstance()
					.validateCaptchaResponse(validateCode, session);
			if (!isValidateCode) {
				// captcha code is not right
				request.setAttribute(Constant.ATTR_ERROR_MSG,
						getText(MessageKeys.CAPTCHA_ERR_MSG));
			} else {
				String userName = request
						.getParameter(Constant.REQ_PARAM_USERNAME);
				String pass = request.getParameter(Constant.REQ_PARAM_PASSWORD);
				String repass = request
						.getParameter(Constant.REQ_PARAM_REPASSWORD);
				logger.debug("userName = " + userName + ",pass = " + pass
						+ ",repass = " + repass);
				if (Validity.isNullAndEmpty(userName)
						&& !(Validity.isLessLength(userName,
								Constant.USERNAME_LENGTH))) {
					request.setAttribute(Constant.ATTR_ERROR_MSG,
							getText(MessageKeys.USERNAME_ERR_MSG));
					return Constant.REGISTER_PAGE;
				}
				if (Validity.isNullAndEmpty(pass)
						&& !(Validity.isLessLength(pass,
								Constant.PASSWORD_LENGTH))) {
					request.setAttribute(Constant.ATTR_ERROR_MSG,
							getText(MessageKeys.PASS_ERR_MSG));
					return Constant.REGISTER_PAGE;
				} else if (!pass.equals(repass)) {
					request.setAttribute(Constant.ATTR_ERROR_MSG,
							getText(MessageKeys.REPASS_ERR_MSG));
					return Constant.REGISTER_PAGE;
				}
				User user = new User();
				user.setUserName(userName);
				user.setPassword(pass);
				user.setCreateTime(new Date());
				user.setOverSize(SystemGlobals
						.getInt(ConfigKeys.DEFAULT_USER_SPACE_SIZE));
				userService = ServiceFactory.createService(UserService.class);
				String registerMsg = userService.doRegister(user);
				if (MessageKeys.REGISTER_FAILURE.equals(registerMsg)) {
					logger.debug(userName + " register failure!");
				} else {
					logger.debug(userName + " register success!");
				}
				request.setAttribute(Constant.REGISTER_MSG, registerMsg);
			}
		} catch (Exception e) {
			logger.error("register exception message!", e);
			request.setAttribute(Constant.ATTR_ERROR_MSG,
					getText(MessageKeys.REGISTER_EXCEPTION));
		}
		return Constant.REGISTER_PAGE;
	}

}
