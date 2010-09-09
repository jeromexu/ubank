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
package com.ufinity.marchant.ubank.common;

import java.util.Collection;
import java.util.List;

/**
 * General input/data validation methods Utility methods for validating data,
 * especially input
 * 
 * @author WenQiang Wu
 * @version Aug 20, 2010
 */
public class Validity {

    // boolean specifying by default whether or not it is okay for a String to
    // be empty

    public static final boolean DEFAULTEMPTYOK = true;

    // decimal point character differs by language and culture
    public static final String DECIMALPOINTDELIMITER = ".";

    public static final String SPECIAL_CHARACTER = "~!@#$%^&()-_=+]\\|:;\"\\'<,>?/";

    /**
     * Check whether string s is null
     * 
     * @param s
     *            any String
     * @return boolean string is equals null,return true
     */
    public boolean isNull(String s) {
        return (s == null);
    }

    /**
     * Check whether string s is not null
     * 
     * @param s
     *            any String
     * @return boolean return not equlas null
     */
    public boolean isNotNull(String s) {
        return (s != null);
    }

    /**
     * Check whether string s is empty.
     * 
     * @param s
     *            any String
     * @return boolean return string is null or length equlas zero
     */
    public static boolean isEmpty(String s) {
        return ((s == null) || (s.length() == 0));
    }

    /**
     * Check whether collection c is empty.
     * 
     * @param c
     *            any Collection
     * @return boolean return collenction is null or no elements
     */
    public static boolean isEmpty(Collection c) {
        return ((c == null) || (c.size() == 0));
    }

    /**
     * Check whether List list is empty.
     * 
     * @param list
     *            any List
     * @return boolean return list is null or no elements
     */
    public static boolean isEmpty(List list) {
        return ((list == null) || (list.size() == 0));
    }

    /**
     * Check whether Object obj is empty.
     * 
     * @param obj
     *            any Object
     * @return boolean return object is null
     */
    public static boolean isEmpty(Object obj) {
        if (null == obj)
            return true;
        if (obj instanceof String && ((String) obj).trim().equals(""))
            return true;
        if (obj instanceof Collection && ((Collection) obj).isEmpty())
            return true;
        return false;
    }

    /**
     * Check whether Object obj is empty.
     * 
     * @param ids
     *            any String[]
     * @return boolean return string arrays is null or no elements
     */

    public static boolean isEmpty(String[] ids) {
        return ((ids == null) || ids.length == 0);
    }

    /**
     * Check whether string s is NOT empty.
     * 
     * @param s
     *            any String
     * @return boolean return string is not null or length is great than zero
     */
    public static boolean isNotEmpty(String s) {
        return ((s != null) && (s.length() > 0));
    }

    /**
     * Check whether collection c is NOT empty.
     * 
     * @param c
     *            any Collection
     * @return boolean return not null
     */
    public static boolean isNotEmpty(Collection c) {
        return ((c != null) && (c.size() > 0));
    }

    /**
     * Check whether collection c is String.
     * 
     * @param obj
     *            any Object
     * @return boolean return not null
     */
    public static boolean isString(Object obj) {
        return ((obj != null) && (obj instanceof java.lang.String));
    }

    /**
     * Check whether String is null and empty.
     * 
     * @param s
     *            any String
     * @return boolean return not null and not empty
     */
    public static boolean isNullAndEmpty(String s) {
        if (null != s && !"".equals(s.trim())) {
            return false;
        }
        return true;
    }

    /**
     * Check whether String is Date.
     * 
     * @param param
     *            any String
     * @return boolean return is date or not
     */
    public static boolean isDate(String param) {
        String pattern = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|"
                + "(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]"
                + "?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}"
                + "(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])"
                + "|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if (param.matches(pattern)) {
            return true;
        }
        return false;
    }

    /**
     * Check whether String is time.
     * 
     * @param param
     *            any String
     * @return boolean return is time or not
     */
    public static boolean isTime(String param) {
        String timepattern = "(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        if (param.matches(timepattern)) {
            return true;
        }
        return false;
    }

    /**
     * Check whether String is PhoneNumber.
     * 
     * @param s
     *            any String
     * @return boolean return is phone number or not
     */
    public static boolean isPhoneNumber(String s) {
        if (s.matches("^[0-9\\-()+]+$")) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if character c is a digit (0 .. 9).
     * 
     * @param c
     *            any char
     * @return boolean return is digit or not
     */
    public static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    /**
     * Returns true if all characters in string s are numbers. Accepts
     * non-signed integers only. Does not accept floating point, exponential
     * notation, etc.
     * 
     * @param s
     *            any String
     * @return boolean return is integer or not
     */

    public static boolean isInteger(String s) {
        if (isEmpty(s))
            return DEFAULTEMPTYOK;

        // Search through string's characters one by one
        // until we find a non-numeric character.
        // When we do, return false; if we don't, return true.
        for (int i = 0; i < s.length(); i++) {
            // Check that current character is number.
            char c = s.charAt(i);

            if (!isDigit(c))
                return false;
        }

        // All characters are numbers.
        return true;
    }

    /**
     * True if string s is an unsigned floating point(real) number. Also returns
     * true for unsigned integers. If you wish to distinguish between integers
     * and floating point numbers, first call isInteger, then call isFloat. Does
     * not accept exponential notation.
     * 
     * @param s
     *            any String
     * @return boolean return is float or not
     */

    public static boolean isFloat(String s) {
        if (isEmpty(s))
            return DEFAULTEMPTYOK;

        boolean seenDecimalPoint = false;

        if (s.startsWith(DECIMALPOINTDELIMITER))
            return false;

        // Search through string's characters one by one
        // until we find a non-numeric character.
        // When we do, return false; if we don't, return true.
        for (int i = 0; i < s.length(); i++) {
            // Check that current character is number.
            char c = s.charAt(i);

            if (c == DECIMALPOINTDELIMITER.charAt(0)) {
                if (!seenDecimalPoint) {
                    seenDecimalPoint = true;
                }
                else {
                    return false;
                }
            }
            else {
                if (!isDigit(c))
                    return false;
            }
        }
        // All characters are numbers.
        return true;
    }

    /**
     * Validate email format
     * 
     * @param email
     *            any String
     * @return boolean return is email fomat or not
     */
    public static boolean isEmailFormat(String email) {
        return email != null
                && email
                        .matches("(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)");
    }

    /**
     * Validate Less Length
     * 
     * @param input
     *            any String
     * @param length
     *            any int
     * @return boolean return is less length or not
     */
    public static boolean isLessLength(String input, int length) {
        if (isEmpty(input)) {
            return false;
        }
        return (input.length() <= length);
    }

    /**
     * Validate Number
     * 
     * @param s
     *            any String
     * @return boolean return is number or not
     */
    public static boolean isNumber(String s) {
        return s != null && s.matches("[0-9]+");
    }

    /**
     * Validate include special
     * 
     * @param s
     *            an string
     * @return boolean return is include special or not
     */
    public static boolean isSpecial(String s) {
        int index = -1;
        for (int i = 0; i < s.length(); i++) {
            index = SPECIAL_CHARACTER.indexOf(s.charAt(i));
            if (index >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate include digit
     * 
     * @param s
     *            any string
     * @return boolean return is include digit
     */
    public static boolean isDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * is name
     * 
     * @param name
     *            name
     * @return if matche return true, else return false
     * @author zdxue
     */
    public static boolean isName(String name) {
        String regex = "[a-zA-Z0-9[\u4e00-\u9fa5]]+";
        return name != null && name.matches(regex);
    }

    /**
     * is CDN code
     * 
     * @param cdn
     *            cdn code
     * @return if matched return true
     * @author zdxue
     */
    public static boolean isCDN(String cdn) {
        return cdn != null && cdn.matches("[a-zA-Z0-9]+");
    }

    /**
     * Check 'longObj' whether is not null or zero.
     * 
     * @param longObj
     *            Long object
     * @return return boolean value that after checked
     */
    public static boolean isNullOrZero(Long longObj) {
        if (longObj == null || 0l == longObj) {
            return true;
        }
        return false;
    }
}
