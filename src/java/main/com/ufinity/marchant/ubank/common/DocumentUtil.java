package com.ufinity.marchant.ubank.common;

import com.ufinity.marchant.ubank.bean.File;
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
	//the path of root folder in disk
	static String DOCUMENT_PATH="";
	
	/**
	 * add the real file in disk by the FILE object
	 * 
	 * @param file
	 *            File object
	 * @return integer 
	 * 				success or not
	 */
	public static int addNewFile(File file) {
		return 0;
	}

	/**
	 * add the real directory is disk by FOLDER object
	 * 
	 * @param folder
	 *            folder object
	 * @return integer
	 * 				success or not
	 */
	public static int addNewFolder(Folder folder) {
		return 0;
	}

	/**
	 * rename the File with new Name
	 * @param file 
	 * 			the target file
	 * @param newName
	 *            the new file name
	 * @return integer
	 * 			success or not
	 */
	public static int renameFile(File file,String newName) {
		return 0;
	}

	/**
	 * rename the FOLDER with new name
	 * @param folder
	 * 			the target folder
	 * @param newFolder
	 * @return integer
	 * 				success or not
	 */
	public static int renameFolder(Folder folder,String newFolder) {
		return 0;
	}

	/**
	 * move the file to new path
	 * @param newPath 
	 * 				the new file path
	 * @param file 
	 * 				the target file
	 * @return integer
	 * 				success or not
	 */
	public static int moveFileTo(File file,Folder newPath) {
		return 0;
	}

	/**
	 * move the folder to new path
	 * @param newPath 
	 * 				the new file path
	 * @param folder
	 * 			the target folder
	 * @return integer
	 * 			success or not	
	 */
	public static int moveFolderTo(Folder folder,Folder newPath) {
		return 0;
	}

	/**
	 * remove the file
	 * 
	 * @param file  
	 * 			the target file
	 * @return integer
	 * 			success or not
	 */
	public static int removeFile(File file) {
		return 0;
	}

	/**
	 * remove the folder
	 * @param folder
	 * 			the target folder
	 * @return integer
	 * 			success or not
	 */
	public static int removeFolder(Folder folder) {
		return 0;
	}

}
