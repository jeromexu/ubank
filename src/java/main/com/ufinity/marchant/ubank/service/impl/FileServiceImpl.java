package com.ufinity.marchant.ubank.service.impl;

import java.util.List;

import com.ufinity.marchant.ubank.bean.File;
import com.ufinity.marchant.ubank.common.DaoFactory;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.service.FileService;

/**
 * Description of the class
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public class FileServiceImpl implements FileService {
    
    private FileDao fileDao;
    
    /**
     * construtor
     */
    public FileServiceImpl() {
        fileDao = DaoFactory.getFileDao();
    }

    /**
     * Search share files
     *
     * @param fileName file name
     * @param fileSize file size flag
     * @param publishDate publish date flag
     * @return file list
     * @author zdxue
     */
    public List<File> searchShareFiles(String fileName, String fileSize,
            String publishDate) {
        
        
        System.out.println(fileDao);
        
        // TODO Auto-generated method stub
        return null;
    }

}

