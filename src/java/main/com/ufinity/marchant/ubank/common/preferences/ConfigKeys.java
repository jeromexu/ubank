// -------------------------------------------------------------------------
// Copyright (c) 2000-2009 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
// -------------------------------------------------------------------------
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
// -------------------------------------------------------------------------
package com.ufinity.marchant.ubank.common.preferences;

/**
 * System configuration keys
 * 
 * @version 1.0 - 2010-8-19
 * @author zdxue
 */
public class ConfigKeys {

    /** UserService key */
    public static final String SERVICE_USER = "user.service";

    /** FileService key */
    public static final String SERVICE_FILE = "file.service";

    /** FolderService key */
    public static final String SERVICE_FOLDER = "folder.service";

    public static final String SERVICE_DOWNLOAD = "download.service";

    public static final String SERVICE_UPLOAD = "upload.service";

    public static final String DAO_USER = "user.dao";
    public static final String DAO_FILE = "file.dao";
    public static final String DAO_FOLDER = "folder.dao";
    public static final String DAO_DOWNLOAD_LOG = "downloadlog.dao";
    
    /** ubank pagination config : for per page count */
    public static final String PAGE_SIZE = "page.size";

    // default 1G space for each user
    public static final String DEFAULT_USER_SPACE_SIZE = "default.user.space.size";

    /** file search config keys */
    public static final String FILE_SIZE_0 = "file.size.0";
    public static final String FILE_SIZE_1 = "file.size.1";
    public static final String FILE_SIZE_2 = "file.size.2";
    public static final String FILE_SIZE_3 = "file.size.3";
    public static final String FILE_SIZE_4 = "file.size.4";
    public static final String FILE_PUBLISHDATE_0 = "file.publishDate.0";
    public static final String FILE_PUBLISHDATE_1 = "file.publishDate.1";
    public static final String FILE_PUBLISHDATE_2 = "file.publishDate.2";
    public static final String FILE_PUBLISHDATE_3 = "file.publishDate.3";
    public static final String FILE_PUBLISHDATE_4 = "file.publishDate.4";
    public static final String FILE_PUBLISHDATE_5 = "file.publishDate.5";

    public static final String I18N_BASE_NAME = "i18n.baseName";

    // ubank app path
    public static final String SERVER_PATH = "ubank.path";

    // user space root dir
    public static final String USER_SPACE_ROOT_DIR = "user.space.root.dir";
    // each user's dir
    public static final String MY_FILE_NAME = "my.file.name";
    public static final String SOFTWARE_NAME = "software.name";
    public static final String DOCUMENT_NAME = "document.name";
    public static final String PHOTO_NAME = "photo.name";
    public static final String ROOT_NAME = "root.name";
    public static final String SHARE_ROOT_NAME = "share.root.name";

    // Document type
    public static final String DOCUMENT_TYPE_FOLDER = "document.type.folder";
    public static final String DOCUMENT_TYPE_UNKNOWN = "document.type.unknown";

    // captcha code
    public static final String MIN_WORD_LENGTH = "min.word.length";
    public static final String MAX_WORD_LENGTH = "max.word.length";
    public static final String IMAGE_WIDTH = "image.width";
    public static final String IMAGE_HEIGHT = "image.height";
    public static final String MIN_FONT_SIZE = "min.font.size";
    public static final String MAX_FONT_SIZE = "max.font.size";
    public static final String NUMBERIC_CHARS = "numberic.chars";
    public static final String UPPER_ASCII_CHARS = "upper.ascii.chars";
    public static final String LOWER_ASCII_CHARS = "lower.ascii.chars";
    
    public static final String USER_DEFAULT_POINT = "user.default.point";
    public static final String DOWNLOAD_POINT = "download.point";
    
    // 
}
