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

import static org.junit.Assert.*;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;
import com.ufinity.marchant.ubank.service.impl.UserServiceImpl;

/**
 * 
 * test userSerivce class
 * 
 * @author jerome
 * @version 1.0
 * @since 2010-8-25
 */
public class UserServiceTest {

	private Mockery context;
	private UserDao userDao;
	private FolderDao folderDao;
	private UserServiceImpl userService;

	/**
	 * 
	 * init variable
	 */
	@Before
	public void setUp() {
		context = new Mockery();
		userDao = context.mock(UserDao.class);
		folderDao = context.mock(FolderDao.class);

		userService = new UserServiceImpl();

		userService.setUserDao(userDao);
		userService.setFolderDao(folderDao);
	}

	/**
	 * 
	 * {method description}
	 */
	@Test
	public void testDoRegister() {
		fail("Not yet implemented");
	}

	/**
	 * 
	 * {method description}
	 */
	@Test
	public void testGetUser() {
		fail("Not yet implemented");
	}

	/**
	 * 
	 * {method description}
	 */
	@Test
	public void testGetUserByUserName() {
		fail("Not yet implemented");
	}

}
