//-------------------------------------------------------------------------
// Copyright (c) 2000-2009 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:
//
//-------------------------------------------------------------------------
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
//-------------------------------------------------------------------------
package com.ufinity.marchant.ubank.captcha;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;

/**
 * 
 * generate the captcha code by the new engine
 * 
 * @author jerome
 * @version 1.0
 * @since 2010-8-20
 */
public class MyCaptchaService extends ListImageCaptchaEngine {

	private final Logger logger = Logger.getInstance(MyCaptchaService.class);
	// Singleton instance of this class
	private static MyCaptchaService INSTANCE = new MyCaptchaService();
	@SuppressWarnings("unchecked")
	private ArrayList textPasterList;
	@SuppressWarnings("unchecked")
	private ArrayList backgroundGeneratorList;
	@SuppressWarnings("unchecked")
	private ArrayList fontGeneratorList;
	private ImageCaptcha imageCaptcha = null;

	/**
	 * 
	 * get the instant of the MyCaptchaService
	 * 
	 * @return the instance
	 */
	public static MyCaptchaService getInstance() {
		return INSTANCE;
	}

	/**
	 * init factory param
	 */
	@SuppressWarnings("unchecked")
	protected void buildInitialFactories() {
		try {
			textPasterList = new ArrayList();
			backgroundGeneratorList = new ArrayList();
			fontGeneratorList = new ArrayList();
			Integer minWordLength = SystemGlobals
					.getInt(ConfigKeys.MIN_WORD_LENGTH);
			Integer maxWordLength = SystemGlobals
					.getInt(ConfigKeys.MAX_WORD_LENGTH);
			textPasterList.add(new SimpleTextPaster(minWordLength,
					maxWordLength, Color.black));
			textPasterList.add(new RandomTextPaster(minWordLength,
					maxWordLength, Color.black));
			textPasterList.add(new SimpleTextPaster(minWordLength,
					maxWordLength, Color.black));
			textPasterList.add(new RandomTextPaster(minWordLength,
					maxWordLength, Color.black));
			textPasterList.add(new SimpleTextPaster(minWordLength,
					maxWordLength, Color.blue));
			textPasterList.add(new RandomTextPaster(minWordLength,
					maxWordLength, Color.blue));

			Integer imageWidth = SystemGlobals.getInt(ConfigKeys.IMAGE_WIDTH);
			Integer imageHeight = SystemGlobals.getInt(ConfigKeys.IMAGE_HEIGHT);
			backgroundGeneratorList.add(new GradientBackgroundGenerator(
					imageWidth, imageHeight, Color.orange, Color.white));
			backgroundGeneratorList.add(new GradientBackgroundGenerator(
					imageWidth, imageHeight, Color.white, Color.green));
			backgroundGeneratorList.add(new GradientBackgroundGenerator(
					imageWidth, imageHeight, Color.gray, Color.white));
			backgroundGeneratorList.add(new GradientBackgroundGenerator(
					imageWidth, imageHeight, Color.white, Color.orange));
			backgroundGeneratorList.add(new GradientBackgroundGenerator(
					imageWidth, imageHeight, Color.green, Color.white));
			backgroundGeneratorList.add(new GradientBackgroundGenerator(
					imageWidth, imageHeight, Color.yellow, Color.white));
			backgroundGeneratorList.add(new GradientBackgroundGenerator(
					imageWidth, imageHeight, Color.white, Color.magenta));

			fontGeneratorList.add(new RandomFontGenerator(SystemGlobals
					.getInt(ConfigKeys.MIN_FONT_SIZE), SystemGlobals
					.getInt(ConfigKeys.MAX_FONT_SIZE)));
			// use the number and upper letter to generate the captcha code
			WordGenerator words = new RandomWordGenerator(SystemGlobals
					.getString(ConfigKeys.NUMBERIC_CHARS)
					+ SystemGlobals.getString(ConfigKeys.UPPER_ASCII_CHARS));

			for (Iterator fontIter = fontGeneratorList.iterator(); fontIter
					.hasNext();) {
				FontGenerator font = (FontGenerator) fontIter.next();
				for (Iterator backIter = backgroundGeneratorList.iterator(); backIter
						.hasNext();) {
					BackgroundGenerator back = (BackgroundGenerator) backIter
							.next();
					for (Iterator textIter = textPasterList.iterator(); textIter
							.hasNext();) {
						TextPaster parser = (TextPaster) textIter.next();
						WordToImage word2image = new ComposedWordToImage(font,
								back, parser);
						ImageCaptchaFactory factory = new GimpyFactory(words,
								word2image);
						addFactory(factory);
					}
				}
			}
		} catch (Exception ex) {
			logger.error("gernate the captcha code expcetion ", ex);
		}
	}

	/**
	 * Write the captcha image of current user to the servlet response
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public void writeCaptchaImage(HttpServletRequest request,
			HttpServletResponse response) {
		imageCaptcha = getNextImageCaptcha();
		HttpSession session = request.getSession();
		session.setAttribute("imageCaptcha", imageCaptcha);
		BufferedImage image = (BufferedImage) imageCaptcha.getChallenge();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			// render the captcha challenge as a JPEG image in the response
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			JPEGImageEncoder encoder = JPEGCodec
					.createJPEGEncoder(outputStream);
			encoder.encode(image);
			outputStream.flush();
		} catch (IOException ex) {
			logger.error("gernate the picture exception:", ex);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
				}
			}
			imageCaptcha.disposeChallenge();
		}
	}

	/**
	 * check the captcha code is right or not
	 * 
	 * @param validateCode
	 *            captcha code
	 * @param session
	 *            the container
	 * @return true or false
	 */
	public boolean validateCaptchaResponse(String validateCode,
			HttpSession session) {
		boolean flag = true;
		try {
			imageCaptcha = (ImageCaptcha) session.getAttribute("imageCaptcha");
			if (imageCaptcha == null) {
				flag = false;
			}
			validateCode = validateCode.toUpperCase();
			flag = (imageCaptcha.validateResponse(validateCode)).booleanValue();
			session.removeAttribute("imageCaptcha");
			return flag;
		} catch (Exception ex) {
			logger.error("check the captcha code expcetion: ", ex);
			return false;
		}
	}

}
