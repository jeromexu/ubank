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
package com.ufinity.marchant.ubank.dao;

import java.util.Map;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.common.Pager;

/**
 * 
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
public interface FileDao extends GenericDao<FileBean, Long> {

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
    public Pager<FileBean> searchPaginatedForFile(int currentPage,
            int pageSize, Map<String, Object> condition);

    /**
     * this method is search file by critera and return Page object
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
    public Pager<FileBean> searchPaginatedByCriteriaWithFile(int currentPage,
            int pageSize, Map<String, Object> condition);
}
