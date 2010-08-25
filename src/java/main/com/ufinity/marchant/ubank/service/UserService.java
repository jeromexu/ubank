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
package com.ufinity.marchant.ubank.service;

import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.exception.UBankException;

/**
 * 
 * user business logic interface
 * 
 * @author jerome
 * @version 1.0
 * @since 2010-8-18
 */
public interface UserService {

	/**
	 * 
	 * do user register
	 * 
	 * @param user
	 *            a person who do register
	 * @return return the message which register a user
	 * @author jerome
	 */
	public String doRegister(User user);

	/**
	 * Get User by username and password
	 * 
	 * @param username
	 *            user's name
	 * @param password
	 *            user's password
	 * @return User obj
	 * @throws UBankException if occur exception, then throw it
	 * @author zdxue
	 */
	public User getUser(String username, String password) throws UBankException ;

	/**
	 * 
	 * get user by userName
	 * 
	 * @param userName
	 *            user's name
	 * @return user object
	 */
	public User getUserByUserName(String userName);
}
