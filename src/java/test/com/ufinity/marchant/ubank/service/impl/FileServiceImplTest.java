package com.ufinity.marchant.ubank.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.exception.UBankServiceException;
import com.ufinity.marchant.ubank.model.Pager;
import com.ufinity.marchant.ubank.service.FileService;

/**
 * FileServiceImplTest
 * 
 * @version 1.0 - Aug 25, 2010
 * @author zdxue
 */
public class FileServiceImplTest {

	private Mockery context = new Mockery();
	private FileDao fileDao;
	private FileService fileService;

	/**
	 * SetUp
	 * 
	 * @throws Exception
	 *             occur exception throw it
	 * @author zdxue
	 */
	@Before
	public void setUp() throws Exception {
		fileDao = context.mock(FileDao.class);
		FileServiceImpl fileServiceImpl = new FileServiceImpl();
		fileServiceImpl.setFileDao(fileDao);
		fileService = fileServiceImpl;
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
	 * Test saerch
	 * 
	 * @author zdxue
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSearchShareFile() {
		// 预设自定义测试数据
		String fileName = "";
		String fileSize = "0";
		String publishDate = "0";
		int pageNum = 1;
		int pageSize = 10;

		final Pager<FileBean> pager1 = new Pager<FileBean>();
		final Pager<FileBean> pager2 = new Pager<FileBean>();
		List<FileBean> pageRecords = new ArrayList<FileBean>();
		FileBean fb = new FileBean();
		fb.setFileId(1L);
		fb.setShare(true);
		fb.setFileName("file");
		pageRecords.add(fb);
		pager2.setCurrentPage(1);
		pager2.setPageRecords(pageRecords);
		pager2.setPageSize(10);
		pager2.setTotalRecords(1);

		// Case1:测试没有满足搜索条件的情况
		// 输入参数： 预设搜索条件
		// 预期值： 空Pager<FileBean>
		context.checking(new Expectations() {
			{
				exactly(1).of(fileDao).searchPaginatedForFile(
						with(any(int.class)), with(any(int.class)),
						with(any(Map.class)));
				will(returnValue(pager1));
			}
		});

		try {
			assertEquals(0, fileService.searchShareFiles(fileName, fileSize,
					publishDate, pageNum, pageSize).getTotalRecords());
		} catch (UBankServiceException e) {
		    fail("search file error");
        }

		// Case2:测试有满足搜索条件的记录
		// 输入参数： 预设搜索条件
		// 预期值： 非空Pager<FileBean>
		context.checking(new Expectations() {
			{
				exactly(1).of(fileDao).searchPaginatedForFile(
						with(any(int.class)), with(any(int.class)),
						with(any(Map.class)));
				will(returnValue(pager2));
			}
		});

		try {
			assertEquals(1, fileService.searchShareFiles(fileName, fileSize,
					publishDate, pageNum, pageSize).getTotalRecords());
		} catch (UBankServiceException e) {
		    fail("search file error");
        }

		context.assertIsSatisfied();
	}

	/**
	 * test get file
	 * @author jerome
	 */
	@Test
	public void testGetFileBean() {
		
		final Long fileId = 1L;
		
		// Case 1 : query file by file id and the file id does not exist
		// parameter value： the file id
		// expectation value： the fileBean which is null
		context.checking(new Expectations() {
			{
				oneOf(fileDao).find(fileId);
				will(returnValue(null));
			}
		});
		try {
			assertEquals(null,fileService.download(fileId, null));
		} catch (UBankServiceException e) {
		    fail("get file error");
        }
		
		final FileBean filebean = new FileBean();
		filebean.setFileId(3L);
		// Case 1 : query file by file id and the file id exist
		// parameter value： the file id
		// expectation value： the fileBean which is not null
		context.checking(new Expectations() {
			{
				oneOf(fileDao).find(fileId);
				will(returnValue(filebean));
			}
		});
		try {
			assertEquals(filebean.getFileId(),fileService.download(fileId, null).getFile().getFileId());
		}catch (UBankServiceException e) {
		    fail("get file error");
        }
	}
}
