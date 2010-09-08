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

				public int compare(FileOrFolderJsonEntity a,
						FileOrFolderJsonEntity b) {
					if (a == null || b == null) {
						return 0;
					} else {
						int flag = 0;
						try {
							Object as = method.invoke(a, new Object[] {});
							Object bs = method.invoke(b, new Object[] {});
							if (as == null && bs == null)
								return 0;
							else if (as == null)
								return -1;
							else if (bs == null)
								return 1;
							else {
								if (as instanceof String) {
									String va = (String) as;
									String vb = (String) bs;
									if (sortWay.equals("asc")) {
										flag = va.compareTo(vb);
									} else {
										flag = vb.compareTo(va);
									}
								}
								if (as instanceof Date) {
									Date va = (Date) as;
									Date vb = (Date) bs;
									if (sortWay.equals("asc")) {
										if (va.before(vb)) {
											return -1;
										} else {
											return 1;
										}
									} else {
										if (va.before(vb)) {
											return 1;
										} else {
											return -1;
										}
									}
								}
								if (as instanceof Integer) {
									int ai = (Integer) as;
									int bi = (Integer) bs;
									if (sortWay.equals("asc")) {
										if (ai >= bi) {
											return 1;
										} else {
											return -1;
										}
									} else {
										if (ai >= bi) {
											return -1;
										} else {
											return 1;
										}
									}

								}

							}

						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						return flag;
					}
				}
			});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return;
		}
	}

}
