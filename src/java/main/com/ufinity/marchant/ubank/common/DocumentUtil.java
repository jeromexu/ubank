package com.ufinity.marchant.ubank.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;
import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.bean.Folder;

/**
 * a tool class for FILE and FOLDER bean ,you can do the some operations of file
 * or directory in real DISK by FILE or FOLDER info. such as: add new
 * file,remove file or directroy ,rename a file etc
 * 
 * @author yonghui
 * @version 1.0 - 2010-8-19
 */
public class DocumentUtil {
	// the path of root folder in disk
	static String DOCUMENT_PATH = "";
	public static String FOLDERNAME = null;
	public static String FILE_DIRECTORY = null;
	public static String FOLDER_DIRECTORY = null;
	public static String FILENAME = null;

	// Logger for this class
	protected static final Logger LOGGER = Logger.getLogger(DocumentUtil.class);

	/**
	 * add the real file in disk by the FILE object
	 * 
	 * @param file
	 *            File object
	 * @return integer success or not
	 */
	public static int addNewFile(FileBean file) {
		return 0;
	}

	/**
	 * add the real directory is disk by FOLDER object
	 * 
	 * @param folder
	 *            folder object
	 * @return integer success or not: success 1 failure 0
	 * @author jerome
	 */
	public static int addNewFolder(Folder folder) {

		if (folder != null) {
			FOLDERNAME = folder.getFolderName();
			FOLDER_DIRECTORY = folder.getDirectory();
		} else {
			return 0;
		}
		File baseFile = createFile(FOLDER_DIRECTORY, FOLDERNAME);
		if (!baseFile.exists()) {
			baseFile.mkdir();
		}
		return 1;
	}

	/**
	 * rename the File with new Name
	 * 
	 * @param fileBean
	 *            the target file
	 * @param newName
	 *            the new file name
	 * @return integer success or not: success 1 failure 0
	 * @author jerome
	 */
	public static int renameFile(FileBean fileBean, String newName) {

		if (fileBean != null) {
			FILENAME = fileBean.getFileName();
			FILE_DIRECTORY = fileBean.getDirectory();
		} else {
			return 0;
		}
		File sFile = createFile(FILE_DIRECTORY, FILENAME);
		File dFile = createFile(FILE_DIRECTORY, newName);
		boolean result = false;
		if(sFile.exists()){
			result = sFile.renameTo(dFile);
		} else  {
			return 0;
		}

		return result ? 1 : 0;

	}

	/**
	 * rename the FOLDER with new name
	 * 
	 * @param folder
	 *            the target folder
	 * @param newFolder
	 * @return integer success or not: success 1 failure 0
	 * @author jerome
	 */
	public static int renameFolder(Folder folder, String newFolder) {

		if (folder != null) {
			FOLDERNAME = folder.getFolderName();
			FOLDER_DIRECTORY = folder.getDirectory();
		} else {
			return 0;
		}
		File sFile = createFile(FOLDER_DIRECTORY, FOLDERNAME);
		File dFile = createFile(FOLDER_DIRECTORY, newFolder);
		boolean result = false;
		if(sFile.exists()){
			result = sFile.renameTo(dFile);
		} else {
			return 0;
		}

		return result ? 1 : 0;

	}

	/**
	 * move the file to new path
	 * 
	 * @param newPath
	 *            the new file path
	 * @param fileBean
	 *            the target file
	 * @return integer success or not: success 1 failure 0
	 * @author jerome
	 */
	public static int moveFileTo(FileBean fileBean, Folder newPath) {
		if (fileBean != null) {
			FILENAME = fileBean.getFileName();
			FILE_DIRECTORY = fileBean.getDirectory();
		} else {
			return 0;
		}
		if (newPath != null) {
			FOLDERNAME = newPath.getFolderName();
			FOLDER_DIRECTORY = newPath.getDirectory();
		} else {
			return 0;
		}
		File sFile = createFile(FILE_DIRECTORY, FILENAME);
		if (!sFile.exists()) {
			return 0;
		}
		File dFile = createFile(FOLDER_DIRECTORY, FOLDERNAME);
		if (!dFile.exists()) {
			dFile.mkdirs();
		}
		boolean result = sFile.renameTo(new File(dFile, sFile.getName()));

		return result ? 1 : 0;
	}

	/**
	 * move the folder to new path
	 * 
	 * @param newFolder
	 *            the new file path
	 * @param folder
	 *            the target folder
	 * @return integer success or not
	 * @author jerome
	 */
	public static int moveFolderTo(Folder folder, Folder newFolder) {
		if (folder != null) {
			FOLDERNAME = folder.getFolderName();
			FOLDER_DIRECTORY = folder.getDirectory();
		} else {
			return 0;
		}
		String newFolderName = null;
		String newFolderDirecotry = null;
		if (newFolder != null) {
			newFolderName = newFolder.getFolderName();
			newFolderDirecotry = newFolder.getDirectory();
		} else {
			return 0;
		}
		String olderPath = null;
		if (FOLDER_DIRECTORY.endsWith(File.separator)) {
			olderPath = FOLDER_DIRECTORY + FOLDERNAME;
		} else {
			olderPath = FOLDER_DIRECTORY + File.separator + FOLDERNAME;
		}
		String newPath = null;
		if (newFolderDirecotry.endsWith(File.separator)) {
			newPath = newFolderDirecotry + newFolderName;
		} else {
			newPath = newFolderDirecotry + File.separator + newFolderName
					+ File.separator + FOLDERNAME;
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("olderPath=" + olderPath);
			LOGGER.debug("newPath=" + newPath);
		}
		File oldFile = new File(olderPath);
		boolean result = false;
		if(oldFile.exists()){
			copyFolder(olderPath, newPath);
			result = delFolder(olderPath);
		} else {
			return 0;
		}
		
		return result ? 1 : 0;
	}

	/**
	 * remove the file
	 * 
	 * @param fileBean
	 *            the target file
	 * @return integer success or not: success 1 failure 0
	 * @author jerome
	 */
	public static int removeFile(FileBean fileBean) {
		if (fileBean != null) {
			FILENAME = fileBean.getFileName();
			FILE_DIRECTORY = fileBean.getDirectory();
		} else {
			return 0;
		}
		File baseFile = createFile(FILE_DIRECTORY, FILENAME);
		if (baseFile.exists()) {
			baseFile.delete();
		} else {
			return 0;
		}
		return 1;
	}

	/**
	 * remove the folder
	 * 
	 * @param folder
	 *            the target folder
	 * @return integer success or not: success 1 failure 0
	 * @author jerome
	 */
	public static int removeFolder(Folder folder) {
		if (folder != null) {
			FOLDERNAME = folder.getFolderName();
			FOLDER_DIRECTORY = folder.getDirectory();
		} else {
			return 0;
		}
		String path = null;
		if (FOLDER_DIRECTORY.endsWith(File.separator)) {
			path = FOLDER_DIRECTORY + FOLDERNAME;
		} else {
			path = FOLDER_DIRECTORY + File.separator + FOLDERNAME;
		}
		File baseFile = new File(path);
		boolean result = false;
		if (baseFile.exists()) {
			result = delFolder(path);
		} else {
			return 0;
		}
		return result ? 1 : 0;
	}

	/**
	 * copy the whole folder's content
	 * 
	 * @param oldPath
	 *            String the source path for example：c:/hello
	 * @param newPath
	 *            String the dest path for example：f:/hello/ee
	 * @author jerome
	 */
	public static void copyFolder(String oldPath, String newPath) {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			// if file not exist and create the new file
			File newPathFile = new File(newPath);
			if (!newPathFile.exists()) {
				newPathFile.mkdirs();
			}
			File oldPathFile = new File(oldPath);
			String[] file = oldPathFile.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				// file operation
				if (temp.isFile()) {
					input = new FileInputStream(temp);
					output = new FileOutputStream(newPath + "/"
							+ temp.getName().toString());
					byte[] b = new byte[1024 * 10];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
				}
				// folder operaction
				if (temp.isDirectory()) {
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			LOGGER.debug("copy file exception", e);
		} finally {
			try {
				output.close();
				input.close();

				// 回收所有占用对象，不然资源释放不了
				System.gc();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * delete the folder by the folder
	 * 
	 * @param folderPath
	 *            the folder path
	 * @return true:delete success false: delete failure
	 * @author jerome
	 */
	public static boolean delFolder(String folderPath) {
		boolean result = false;
		try {
			// delete all files of the folder
			delAllFile(folderPath);
			File myFilePath = new File(folderPath);
			// delete the empty folder
			result = myFilePath.delete();

		} catch (Exception e) {
			LOGGER.debug("delete file exception", e);
			return false;
		}
		return result;
	}

	/**
	 * delete the folder's the child folder
	 * 
	 * @param path
	 *            String the folder path for example c:/hello
	 * @author jerome
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		// System.out.println("total files count：" + tempList.length);
		File tempFile = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				tempFile = new File(path + tempList[i]);
			} else {
				tempFile = new File(path + File.separator + tempList[i]);
			}
			if (tempFile.isFile()) {
				try {
					// System.out.println("tempFile=" + tempFile);
					boolean result = tempFile.delete();
					// System.out.println("delete files：" + result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (tempFile.isDirectory()) {
				// first delete the folder's child folder
				delAllFile(path + File.separator + tempList[i]);
				// delete the empty folder
				delFolder(path + File.separator + tempList[i]);
			}
		}
	}

	/**
	 * 
	 * create a File by the old folder path and folder name
	 * 
	 * @param folderPath
	 *            the folder path
	 * @param folderName
	 *            the folder name
	 * @return a file object
	 * @author jerome
	 */
	private static File createFile(String folderPath, String folderName) {
		File file = null;
		if (folderPath.endsWith(File.separator)) {
			file = new File(folderPath + folderName);
		} else {
			file = new File(folderPath + File.separator + folderName);
		}
		return file;
	}

	/**
	 * test method 
	 * 
	 * @param args the param
	 */
	public static void main(String[] args) {
		/*
		 * FileBean fileBean = new FileBean();
		 * fileBean.setDirectory("D:\\test\\a");
		 * fileBean.setFileName("haha.txt"); Folder folder = new Folder();
		 * folder.setDirectory("D:\\test\\b"); folder.setFolderName("c");
		 * Integer a = moveFileTo(fileBean, folder);
		 */

		/*
		 * Folder folder1 = new Folder(); folder1.setDirectory("D:\\test");
		 * folder1.setFolderName("a"); Folder folder2 = new Folder();
		 * folder2.setDirectory("D:\\test"); folder2.setFolderName("b");
		 * moveFolderTo(folder1, folder2);
		 */

		/*
		 * Folder folder = new Folder(); folder.setDirectory("D:\\test\\b");
		 * folder.setFolderName("a"); Integer result = removeFolder(folder);
		 * System.out.println(result);
		 */

		/*
		 * Folder folder = new Folder(); folder.setDirectory("D:\\test\\b");
		 * folder.setFolderName("c"); Integer result
		 * =renameFolder(folder,"ggggggg"); System.out.println(result);
		 */
	}
}
