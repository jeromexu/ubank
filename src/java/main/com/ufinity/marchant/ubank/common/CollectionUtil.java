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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.ufinity.marchant.ubank.vo.FileOrFolderJsonEntity;

/**
 * CollectionUtil
 * 
 * @version 1.0 - 2010-9-6
 * @author zdxue
 */
public class CollectionUtil {
	// Logger for this class
	protected final static Logger LOGGER = Logger
			.getInstance(CollectionUtil.class);

	/**
	 * Add a String array to the String List
	 * 
	 * @param list
	 *            list of String
	 * @param array
	 *            array of String
	 * @return list of String
	 * @author zdxue
	 */
	public static List<String> add(List<String> list, String[] array) {
		if (list == null || array == null) {
			return list;
		}

		for (String str : array) {
			if (str != null) {
				list.add(str.trim());
			} else {
				list.add(str);
			}
		}

		return list;
	}

	/**
	 * sort the given list by property
	 * 
	 * @param list
	 *            targeted list
	 * @param prop
	 *            the property of the element in list
	 * @param sortType
	 *            the way of sort:asc ,desc
	 * @author yonghui
	 */
	public static void getSortedList(List<FileOrFolderJsonEntity> list,
			String prop, String sortType) {

		if (null == prop || null == sortType) {
			return;
		}
		if (null == list || list.size() == 0) {
			return;
		}
		final String sortWay = sortType;
		final String property = StringUtil.makeFirstCharUpper(prop);
		try {
			Collections.sort(list, new Comparator<FileOrFolderJsonEntity>() {
				String name = "get" + property;
				Method method = FileOrFolderJsonEntity.class.getMethod(name,
						new Class[] {});
				int flag = 0;
				public int compare(FileOrFolderJsonEntity a,
						FileOrFolderJsonEntity b) {
					if (a == null || b == null) {
						return flag;
					} else {

						try {
							Object as = method.invoke(a, new Object[] {});
							Object bs = method.invoke(b, new Object[] {});
							if (as == null && bs == null) {
								flag = 0;
							} else if (as == null) {
								flag = -1;
							} else if (bs == null) {
								flag = 1;
							} else {
								if (as instanceof String) {
									String va = (String) as;
									String vb = (String) bs;
									if (sortWay.equalsIgnoreCase("asc")) {
										flag = va.compareTo(vb);
									} else {
										flag = vb.compareTo(va);
									}
								}
								if (as instanceof Date) {
									Date va = (Date) as;
									Date vb = (Date) bs;
									if (sortWay.equalsIgnoreCase("asc")) {
										flag = va.compareTo(vb);
									} else {
										flag = vb.compareTo(va);
									}
								}
								if (as instanceof Integer) {
									Integer ai = (Integer) as;
									Integer bi = (Integer) bs;
									if (sortWay.equalsIgnoreCase("asc")) {
										flag = ai.compareTo(bi);
									} else {
										flag = bi.compareTo(ai);
									}

								}

							}
						} catch (IllegalArgumentException e) {
							LOGGER.error(
									"Sort list : IllegalArgumentException ", e);
						} catch (IllegalAccessException e) {
							LOGGER.error("Sort list : IllegalAccessException ",
									e);
						} catch (InvocationTargetException e) {
							LOGGER
									.error(
											"Sort list : InvocationTargetException ",
											e);
						}
						return flag;
					}

				}
			});
		} catch (NoSuchMethodException e) {
			LOGGER.error("Sort list : NoSuchMethodException ", e);
			return;
		}
	}

}
