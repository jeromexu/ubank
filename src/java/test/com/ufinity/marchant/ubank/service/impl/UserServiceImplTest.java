package com.ufinity.marchant.ubank.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.bean.User;
import com.ufinity.marchant.ubank.common.preferences.MessageKeys;
import com.ufinity.marchant.ubank.common.preferences.MessageResource;
import com.ufinity.marchant.ubank.dao.FolderDao;
import com.ufinity.marchant.ubank.dao.UserDao;
import com.ufinity.marchant.ubank.exception.UBankException;
import com.ufinity.marchant.ubank.service.UserService;

/**
 * UserServiceImpl test
 * 
 * @version 1.0 - Aug 25, 2010
 * @author zdxue
 */
public class UserServiceImplTest {

	private Mockery context = new Mockery();
	private UserDao userDao;
	private UserService userService;
	private FolderDao folderDao;

	/**
	 * setUpBeforeClass
	 * 
	 * @throws Exception
	 *             occur exception throw it
	 * @author zdxue
	 */
	@Before
	public static void setUpBeforeClass() throws Exception {
		// TODO
	}

	/**
	 * SetUp
	 * 
	 * @throws Exception
	 *             occur exception throw it
	 * @author zdxue
	 */
	@Before
	public void setUp() throws Exception {
		userDao = context.mock(UserDao.class);
		folderDao = context.mock(FolderDao.class);
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		userServiceImpl.setUserDao(userDao);
		userServiceImpl.setFolderDao(folderDao);
		userService = userServiceImpl;
	}
    /**
     * TearDown
     * 
     * @throws Exception
     *             occur exception throw it
     * @author zdxue
     */
    @After
    public void tearDown() throws Exception {
        // TODO
    }

	/**
	 * test login
	 * 
	 * @author zdxue
	 */
	@Test
	public void testLogin() {
		// 预设自定义测试数据
		String userName = "";
		String password = "";

		final User user = new User();
		user.setUserId(1L);
		user.setUserName("admin");
		user.setPassword("pass");

		// Case1:查询数据库，检查是否该数据库中有此用户名和密码组成的对象，测试没有情况
		// 输入参数： 预设userName , password
		// 预期值： NULL对象
		context.checking(new Expectations() {
			{
				exactly(1).of(userDao).findUser(with(any(String.class)),
						with(any(String.class)));
				will(returnValue(null));
			}
		});

		try {
			assertEquals(null, userService.getUser(userName, password));
		} catch (UBankException e) {
			fail("get user error");
		}
        try {
            assertNull(userService.getUser(userName, password));
        } catch (UBankException e) {
            fail("get user error");
        }

		// Case2:查询数据库，检查是否该数据库中有此用户名和密码组成的对象，测试存在的情况
		// 输入参数： 预设userName , password
		// 预期值： User对象
		context.checking(new Expectations() {
			{
				exactly(1).of(userDao).findUser(with(any(String.class)),
						with(any(String.class)));
				will(returnValue(user));
			}
		});

		try {
			assertEquals("admin", userService.getUser(userName, password)
					.getUserName());
		} catch (UBankException e) {
			fail("get user error");
		}

		context.assertIsSatisfied();
	}

	/**
	 * 
	 * test user register
	 * 
	 * @author jerome
	 */
	@Test
	public void testRegister() {
		
		
		final User user = new User();
		user.setUserId(1L);
		user.setUserName("admin");
		user.setPassword("pass");
		
		final Folder folder = new Folder();
		folder.setFolderId(1L);
		folder.setFolderName("hello");
		folder.setUser(user);
		folder.setDirectory("/a/b");
		folder.setCreateTime(new Date());
		// Case 1 : query user by userName that the user exists
		// parameter value： the userName
		// expectation value： register failure msg
		context.checking(new Expectations() {
			{
				exactly(1).of(userDao).findUserByName(with(any(String.class)));
				will(returnValue(user));
				
			}
		});
		String msg = userService.doRegister(user);
		assertEquals(MessageResource.getMessage(MessageKeys.REGISTER_FAILURE), msg);
		
		
		final User member = new User();
		user.setUserId(2L);
		user.setUserName("hello");
		user.setPassword("111111");
		// Case 2 : query user by userName that the user does not exist
		// parameter value： the userName
		// expectation value： register success msg
		context.checking(new Expectations() {
			{
				oneOf(userDao).findUserByName(with(any(String.class)));
				will(returnValue(null));
				
				oneOf(userDao).add(member);
				
				
				oneOf(folderDao).add(folder);
				
			}
		});
		String message = userService.doRegister(user);
		assertEquals(MessageResource.getMessage(MessageKeys.REGISTER_SUCCESS), message);
		context.assertIsSatisfied();
	}
}
