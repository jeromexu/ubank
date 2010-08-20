package com.ufinity.marchant.ubank.service;

import java.util.List;

import com.ufinity.marchant.ubank.bean.File;

/**
 * File Service
 *
 * @version 1.0 - 2010-8-19
 * @author zdxue     
 */
public interface FileService {

    /**
     * Search share files
     *
     * @param fileName file name
     * @param fileSize file size flag
     * @param publishDate publish date flag
     * @return file list
     * @author zdxue
     */
    public List<File> searchShareFiles(String fileName, String fileSize, String publishDate);
}

