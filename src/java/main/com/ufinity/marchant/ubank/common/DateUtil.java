// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:
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
package com.ufinity.marchant.ubank.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author WenQiang Wu
 * @version Mar 19, 2010
 */
public class DateUtil {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String MM_DD_YYYY_HH_MM_SS = "MM-dd-yyyy HH:mm:ss";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * format Date to String <br>
     * Note: if Date is null or occur exception, then return empty string
     * 
     * @param date
     *            Date obj
     * @param datePattern
     *            date string pattern
     * @return date string
     * @author zdxue
     */
    public static String format(Date date, String datePattern) {
        if (date == null) {
            return "";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * parse Date from date String <br>
     * Note: if dateString is null or occur exception, then return null
     * 
     * @param dateString
     *            date string
     * @return Date obj
     * @author zdxue
     */
    public static Date parse(String dateString, String datePattern) {
        if (dateString == null) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            return sdf.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * roll on the specified field of Calendar
     *
     * @param calendar Calender
     * @param field field of calendar
     * @param amount roll amount
     * @author zdxue
     * @see java.util.Calendar.roll(int field, int amount); 
     */
    public static void roll(Calendar calendar, int field, int amount) {
        if(calendar == null) {
            return;
        }
        
        calendar.roll(field, amount);
    }
}
