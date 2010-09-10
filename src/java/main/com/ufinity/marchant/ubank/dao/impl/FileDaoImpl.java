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
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.ufinity.marchant.ubank.bean.FileBean;
import com.ufinity.marchant.ubank.common.Constant;
import com.ufinity.marchant.ubank.common.EntityManagerUtil;
import com.ufinity.marchant.ubank.dao.FileDao;
import com.ufinity.marchant.ubank.vo.Pager;

/**
 * 
 * @author WenQiang Wu
 * @version Aug 18, 2010
 */
public class FileDaoImpl extends GenericDaoSupport<FileBean, Long> implements
        FileDao {

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
            int pageSize, Map<String, Object> condition) {
        Criterion[] criterion = new Criterion[] {
                Restrictions.eq("share", condition.get("share")),
                Restrictions.like("fileName", "%"
                        + condition.get(Constant.FILENAME) + "%"),
                Restrictions.between("size", condition
                        .get(Constant.MIN_FILE_SIZE), condition
                        .get(Constant.MAX_FILE_SIZE)),
                Restrictions.between("modifyTime", condition
                        .get(Constant.MIN_MODIFY_TIME), condition
                        .get(Constant.MAX_MODIFY_TIME)) };

        return queryEntitiesByCriteriaWithPager(currentPage, pageSize,
                criterion);
    }

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
            int pageSize, Map<String, Object> condition) {
        String jpaQuery = "SELECT f FROM FileBean f "
                + getJPAQueryString(condition);

        if (null != condition) {
            String fileName = (String) condition.get(Constant.FILENAME);
            if (null != fileName) {
                fileName = "%" + fileName + "%";
                condition.put(Constant.FILENAME, fileName);
            }
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
        StringBuffer jpqQuery = new StringBuffer("WHERE 1=1 AND f.folder.folderShare=true ");

        if (null != condition) {
            String fileName = (String) condition.get(Constant.FILENAME);
            if (null != fileName && !"".equals(fileName)) {
                jpqQuery.append("AND f.fileName LIKE :fileName ");
            }

            Long minFileSize = (Long) condition.get(Constant.MIN_FILE_SIZE);
            Long maxFileSize = (Long) condition.get(Constant.MAX_FILE_SIZE);
            if (null != minFileSize && null != maxFileSize) {
                jpqQuery
                        .append("AND f.size BETWEEN :minFileSize AND :maxFileSize ");
            }

            Date minModifyTime = (Date) condition.get(Constant.MIN_MODIFY_TIME);
            Date maxModifyTime = (Date) condition.get(Constant.MAX_MODIFY_TIME);
            if (null != maxModifyTime) {
                if (null != minModifyTime) {
                    jpqQuery
                            .append("AND f.modifyTime BETWEEN :minModifyTime AND :maxModifyTime ");
                } else {
                    jpqQuery.append("AND f.modifyTime <= :maxModifyTime ");
                }
            }

            logger.debug("Method: getJPAQueryString, Param:{fileName: "
                    + fileName + " , minFileSize: " + minFileSize
                    + " , maxFileSize: " + maxFileSize + " , minModifyTime: "
                    + minModifyTime + " , maxModifyTime: " + maxModifyTime
                    + "}");
        }

        StringBuffer order = new StringBuffer("ORDER BY f.modifyTime DESC");
        jpqQuery.append(order);

        return jpqQuery.toString();
    }

    /**
     * this method is find total size with file according user id.
     * 
     * @param userId
     *            user's id
     * @return Long total size
     * @author skyqiang
     */
    @SuppressWarnings("unchecked")
    public Long findTotalSizeWithFileByUser(Long userId) {
        logger.debug("Method: findTotalSizeWithFileByUser, Param:{userId: "
                + userId + "}");

        List<Long> list = EntityManagerUtil.getEntityManager()
                .createNamedQuery("File.findTotalSizeWithFileByUser")
                .setParameter("userId", userId).getResultList();
        logger
                .debug("Execute find total size with file according user id method is success!");
        return list.get(0);
    }

}
