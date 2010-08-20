// -------------------------------------------------------------------------
// Copyright (c) 2000-2009 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:WenQiang Wu
//
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
package com.ufinity.marchant.ubank.dao.impl;

import java.util.Date;
import java.util.Map;

import com.ufinity.marchant.ubank.bean.File;
import com.ufinity.marchant.ubank.common.Pager;
import com.ufinity.marchant.ubank.dao.FileDao;

/**
 * 
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
public class FileDaoImpl extends GenericDaoSupport<File, Long> implements
        FileDao {

    /**
     * this method is search file and return Pager object
     * 
     * @param condition
     *            search's condition
     * @param currentPage
     *            current page size
     * @param pageSize
     *            page size
     * @return Pager pager object
     * @author skyqiang
     */
    public Pager<File> searchPaginatedForFile(int currentPage, int pageSize,
            Map<String, Object> condition) {
        String jpaQuery = "SELECT f from File f "
                + getJPAQueryString(condition);
        String fileName = (String) condition.get("fileName");
        if (null != condition && null != fileName && !"".equals(fileName)) {
            fileName = "%" + fileName + "%";
            condition.put("fileName", fileName);
        }
        return findPager(jpaQuery, currentPage, pageSize, condition);
    }

    /**
     * this method is get jpa query string
     * 
     * @param condition
     *            query's condition
     * @return String jpa query string
     * @author skyqiang
     */
    private String getJPAQueryString(Map<String, Object> condition) {
        StringBuffer jpqQuery = new StringBuffer("WHERE 1=1 AND f.share=true ");

        String fileName = (String) condition.get("fileName");
        if (null != fileName && !"".equals(fileName)) {
            jpqQuery.append("AND f.fileName LIKE :fileName ");
        }

        Long minFileSize = (Long) condition.get("minFileSize");
        Long maxFileSize = (Long) condition.get("maxFileSize");
        if (null != minFileSize && null != maxFileSize) {
            jpqQuery
                    .append("AND f.size BETWEEN :minFileSize AND :maxFileSize ");
        }

        Date minModifyTime = (Date) condition.get("minModifyTime");
        Date maxModifyTime = (Date) condition.get("maxModifyTime");
        if (null != minModifyTime && null != maxModifyTime) {
            jpqQuery
                    .append("AND f.modifyTime BETWEEN :minModifyTime AND :maxModifyTime ");
        }

        StringBuffer order = new StringBuffer("ORDER BY f.modifyTime DESC");
        jpqQuery.append(order);

        return jpqQuery.toString();
    }
}
