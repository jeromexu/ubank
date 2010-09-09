package com.ufinity.marchant.ubank.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.ufinity.marchant.ubank.common.Logger;
import com.ufinity.marchant.ubank.common.preferences.ConfigKeys;
import com.ufinity.marchant.ubank.common.preferences.SystemGlobals;
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
	protected final static Logger LOGGER = Logger
			.getInstance(DocumentUtil.class);

	/**
	 * Check a FileBean is exist or not
	 * 
	 * @param fileBean
	 *            FileBean obj
	 * @return if exist return true
	 * @author zdxue
	 */
	public static boolean exist(FileBean fileBean) {
		if (fileBean == null)
			return false;

		StringBuffer fullPath = new StringBuffer();
		fullPath.append(getApplicationPath()).append(fileBean.getDirectory())
				.append(Constant.FILE_SYSTEM_SEPARATOR).append(
						fileBean.getFileName());

		return new File(fullPath.toString()).exists();
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
		try {
			if (folder != null) {
				FOLDERNAME = String.valueOf(folder.getFolderId());
				FOLDER_DIRECTORY = folder.getDirectory();
				File baseFile = createFile(FOLDER_DIRECTORY, FOLDERNAME);
				if (!baseFile.exists()) {
					return baseFile.mkdir() ? 1 : 0;
				}
				
			}
		} catch (Exception e) {
			LOGGER.error("add new folder exception!", e);
		}
		return 0;
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
		try {
			if (fileBean != null) {
				FILENAME = fileBean.getFileName();
				FILE_DIRECTORY = fileBean.getDirectory();
				File sFile = createFile(FILE_DIRECTORY, FILENAME);
				File dFile = createFile(FILE_DIRECTORY, newName);
				if (sFile.exists()) {
					return sFile.renameTo(dFile) ? 1 : 0;
				}
			}
		} catch (Exception e) {
			LOGGER.error("rename file exception!", e);
		}
		return 0;

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
		try {
			if (folder != null) {
				FOLDERNAME = String.valueOf(folder.getFolderId());
				FOLDER_DIRECTORY = folder.getDirectory();
				File sFile = createFile(FOLDER_DIRECTORY, FOLDERNAME);
				return (sFile.exists()) ? 1 : 0;
			}
		} catch (Exception e) {
			LOGGER.error("rename folder exception!", e);
		}
		return 0;

	}

	/**
	 * 
	 * copy the file to the dest folder
	 * 
	 * @param fileBean
	 *            file object
	 * @param folder
	 *            folder object
	 * @param isMoveOrCopy
	 *            move:true copy:false
	 * @param newName
	 *            the new file name
	 * 
	 * @return success:1 failure:0
	 * @author jerome
	 */
	public static Integer moveOrCopyFileTo(FileBean fileBean, Folder folder,
			boolean isMoveOrCopy, String newName) {
		try {
			if (fileBean != null && folder != null) {
				FILENAME = fileBean.getFileName();
				FILE_DIRECTORY = fileBean.getDirectory();
				FOLDERNAME = String.valueOf(folder.getFolderId());
				FOLDER_DIRECTORY = folder.getDirectory();
				File sFile = createFile(FILE_DIRECTORY, FILENAME);
				// source file exists
				if (!sFile.exists()) {
					return 0;
				}
				File dFile = createFile(FOLDER_DIRECTORY, FOLDERNAME);
				if (!dFile.exists()) {
					dFile.mkdirs();
				}
				dFile = new File(dFile, newName);
				// copy the file
				boolean copyResult = copyFile(sFile, dFile);
				return isMoveOrCopy ? (copyResult && sFile.delete() ? 1 : 0)
						: (copyResult ? 1 : 0);
			}
		} catch (Exception e) {
			LOGGER.error("move or copy a file exception!", e);
		}
		return 0;
	}

	/**
	 * move the folder to new path
	 * 
	 * @param newFolder
	 *            the new file path
	 * @param folder
	 *            the target folder
	 * @param isMoveOrCopy
	 *            move:true copy:false
	 * @return integer success or not: success 1 failure 0
	 * @author jerome
	 */
	public static int moveOrCopyFolderTo(Folder folder, Folder newFolder,
			boolean isMoveOrCopy) {
		try {
			String newFolderName = null;
			String newFolderDirecotry = null;
			if (folder != null && newFolder != null) {
				FOLDERNAME = String.valueOf(folder.getFolderId());
				FOLDER_DIRECTORY = folder.getDirectory();
				newFolderName = String.valueOf(newFolder.getFolderId());
				newFolderDirecotry = newFolder.getDirectory();
				String serverPath = getApplicationPath();
				if (serverPath != null) {
					StringBuffer olderPath = new StringBuffer();
					if (FOLDER_DIRECTORY
							.endsWith(Constant.FILE_SYSTEM_SEPARATOR)) {
						olderPath.append(serverPath).append(FOLDER_DIRECTORY)
								.append(FOLDERNAME);
					} else {
						olderPath.append(serverPath).append(FOLDER_DIRECTORY)
								.append(Constant.FILE_SYSTEM_SEPARATOR).append(
										FOLDERNAME);
					}
					File oldFile = new File(olderPath.toString());
					if (!oldFile.exists()) {
						return 0;
					}
					StringBuffer newPath = new StringBuffer();
					if (newFolderDirecotry
							.endsWith(Constant.FILE_SYSTEM_SEPARATOR)) {
						newPath.append(serverPath).append(newFolderDirecotry)
								.append(newFolderName);
					} else {
						newPath.append(serverPath).append(newFolderDirecotry)
								.append(Constant.FILE_SYSTEM_SEPARATOR).append(
										newFolderName);
					}
					LOGGER.debug("olderPath=" + olderPath + ", newPath="
							+ newPath);
					newPath.append(Constant.FILE_SYSTEM_SEPARATOR).append(
							FOLDERNAME);
					// copy the folder
					boolean copyResult = copyFolder(olderPath.toString(), newPath
							.toString());
					// move operation
					if (isMoveOrCopy) {
						// move: first copy and then delete the folder
						boolean delResult = delFolder(olderPath.toString());
						return (copyResult && delResult) ? 1 : 0;
					}
					// copy operation
					else {
						return copyResult ? 1 : 0;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("move or copy folder exception!", e);
		}
		return 0;
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
		try {
			if (fileBean != null) {
				FILENAME = fileBean.getFileName();
				FILE_DIRECTORY = fileBean.getDirectory();
				File baseFile = createFile(FILE_DIRECTORY, FILENAME);
				if (baseFile.exists()) {
					return baseFile.delete() ? 1 : 0;
				}
			}
		} catch (Exception e) {
			LOGGER.error("remove file exception!", e);
		}
		return 0;

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
		try {
			if (folder != null) {
				FOLDERNAME = String.valueOf(folder.getFolderId());
				FOLDER_DIRECTORY = folder.getDirectory();

				StringBuffer path = new StringBuffer(getApplicationPath());
				if (path != null) {
					if (FOLDER_DIRECTORY
							.endsWith(Constant.FILE_SYSTEM_SEPARATOR)) {
						path.append(FOLDER_DIRECTORY).append(FOLDERNAME);
					} else {
						path.append(FOLDER_DIRECTORY).append(
								Constant.FILE_SYSTEM_SEPARATOR).append(
								FOLDERNAME);
					}
					File baseFile = new File(path.toString());
					if (baseFile.exists()) {
						return delFolder(path.toString()) ? 1 : 0;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("remove folder exception!", e);
		}
		return 0;
	}

	/**
	 * 
	 * get disk new file name
	 * 
	 * @param realDir
	 *            file directory
	 * @param fileName
	 *            file name
	 * @return the new file name
	 * @author jerome
	 */
	public static String getNewName(String realDir, String fileName) {
		return getNewName(realDir, fileName, 0);
	}

	/**
	 * 
	 * get the server path
	 * 
	 * @return the server path
	 * @author jerome
	 */
	public static String getApplicationPath() {
		String catalinaHome = System.getProperty("catalina.home");
		String serverPath = null;
		if (!Validity.isEmpty(catalinaHome)) {
			serverPath = SystemGlobals.getString(ConfigKeys.SERVER_PATH,
					catalinaHome);
		}
		return serverPath;
	}

	/**
	 * get disk file name
	 * 
	 * @param realDir
	 *            dest dir name
	 * @param fileName
	 *            file name
	 * @param index
	 *            repeat index
	 * @return file
	 * @author jerome
	 */
	private static String getNewName(String realDir, String fileName, int index) {

		try {
			StringBuffer newName = new StringBuffer();
			if (index != 0) {
				if (fileName.lastIndexOf(".") != -1) {
					String prefix = fileName.substring(0, fileName
							.lastIndexOf("."));
					String suffix = fileName.substring(fileName
							.lastIndexOf("."), fileName.length());
					newName.append(prefix).append("(").append(index)
							.append(")").append(suffix);
				} else {
					newName.append(fileName).append("(").append(index).append(
							")");
				}
			} else {
				newName.append(fileName);
			}
			String serverPath = getApplicationPath();
			if (serverPath != null) {
				File file = new File(serverPath + realDir
						+ Constant.FILE_SYSTEM_SEPARATOR + newName.toString());
				if (file.exists()) {
					index++;
					return getNewName(realDir, fileName, index);
				} else {
					return file.getName();
				}
			}
		} catch (Exception e) {
			LOGGER.error("get disk file name exception!", e);
		}
		return null;
	}

	/**
	 * 
	 * copy source file to the dest folder
	 * 
	 * @param sFile
	 *            the source file
	 * @param dFile
	 *            the dest folder
	 * @return success:1 failure:0
	 * @author jerome
	 */
	private static boolean copyFile(File sFile, File dFile) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			// read source file
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					sFile), "GBK"));
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dFile)));
			String hasRead = null;
			while ((hasRead = br.readLine()) != null) {
				bw.write(hasRead);
			}
			bw.flush();
			return true;
		} catch (Exception e) {
			LOGGER.error("copy the file exception!", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error("input stream close exception!", e);
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					LOGGER.error("output stream close exception!", e);
				}
			}
		}

		return false;

	}

	/**
	 * copy the whole folder's content
	 * 
	 * @param oldPath
	 *            String the source path for example：c:/hello
	 * @param newPath
	 *            String the dest path for example：f:/hello/ee
	 * @return success:1 failure:0
	 * @author jerome
	 */
	private static boolean copyFolder(String oldPath, String newPath) {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			// if the older file not exist and return
			File oldPathFile = new File(oldPath);
			if (!oldPathFile.exists()) {
				return false;
			}
			// if new file not exist and create the new file
			File newPathFile = new File(newPath);
			if (!newPathFile.exists()) {
				newPathFile.mkdir();
			}
			String[] file = oldPathFile.list();
			if (file == null) {
				return false;
			}
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(Constant.FILE_SYSTEM_SEPARATOR)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + Constant.FILE_SYSTEM_SEPARATOR
							+ file[i]);
				}
				// file operation
				if (temp.isFile()) {
					copyFile(temp, new File(newPathFile, temp.getName()));
				}
				// folder operaction
				if (temp.isDirectory()) {
					copyFolder(oldPath + Constant.FILE_SYSTEM_SEPARATOR
							+ file[i], newPath + Constant.FILE_SYSTEM_SEPARATOR
							+ file[i]);
				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.debug("copy folder exception!", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error("input stream close exception!", e);
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					LOGGER.error("output stream close exception!", e);
				}
			}
			// recover all the occupancy object so that all the resource release
			System.gc();
		}
		return false;
	}

	/**
	 * delete the folder by the folder
	 * 
	 * @param folderPath
	 *            the folder path
	 * @return true:delete success false: delete failure
	 * @author jerome
	 */
	private static boolean delFolder(String folderPath) {
		try {
			// delete all files of the folder
			boolean delResult = delAllFile(folderPath);
			File myFilePath = new File(folderPath);
			// delete the empty folder
			return (delResult && myFilePath.delete());
		} catch (Exception e) {
			LOGGER.debug("delete file exception", e);
		}
		return false;
	}

	/**
	 * delete the folder's the child folder
	 * 
	 * @param path
	 *            String the folder path for example c:/hello
	 * @author jerome
	 */
	private static boolean delAllFile(String path) {
		try {
			File file = new File(path);
			if (!file.exists() && !file.isDirectory()) {
				return false;
			}
			String[] tempList = file.list();
			if (tempList == null) {
				return false;
			}
			File tempFile = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(Constant.FILE_SYSTEM_SEPARATOR)) {
					tempFile = new File(path + tempList[i]);
				} else {
					tempFile = new File(path + Constant.FILE_SYSTEM_SEPARATOR
							+ tempList[i]);
				}
				if (tempFile.isFile()) {
					tempFile.delete();
				}
				if (tempFile.isDirectory()) {
					// first delete the folder's child folder
					delAllFile(path + Constant.FILE_SYSTEM_SEPARATOR
							+ tempList[i]);
					// delete the empty folder
					delFolder(path + Constant.FILE_SYSTEM_SEPARATOR
							+ tempList[i]);
				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("delete all file exception", e);
		}
		return false;
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
		String serverPath = getApplicationPath();
		StringBuffer sb = new StringBuffer();
		if (serverPath != null) {
			if (folderPath.endsWith(Constant.FILE_SYSTEM_SEPARATOR)) {
				file = new File(sb.append(serverPath).append(folderPath)
						.append(folderName).toString());
			} else {
				file = new File(sb.append(serverPath).append(folderPath)
						.append(Constant.FILE_SYSTEM_SEPARATOR).append(
								folderName).toString());
			}
		}
		return file;
	}

}
