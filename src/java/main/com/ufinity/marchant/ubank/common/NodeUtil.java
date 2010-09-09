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

import java.util.ArrayList;
import java.util.List;

import com.ufinity.marchant.ubank.bean.Folder;
import com.ufinity.marchant.ubank.vo.FileOrFolderJsonEntity;
import com.ufinity.marchant.ubank.vo.FolderNode;
import com.ufinity.marchant.ubank.vo.JsonNode;

/**
 * this is JsonData util class
 * 
 * @author bxji
 * @version 2010-8-23
 */
public class NodeUtil {
	/**
	 * this method copy folderNode property to JsonNode and return this JsonNode
	 * 
	 * @param folderNode
	 *            FolderNode
	 * @return return copied JsonNode
	 * @author bxji
	 */
	public static JsonNode copyFolderNodeToJsonNode(FolderNode folderNode) {
		if (folderNode == null) {
			return null;
		}
		JsonNode jsonNode = new JsonNode();
		List<JsonNode> children = null;
		jsonNode.setId(folderNode.getFolderId());
		jsonNode.setState("closed");
		jsonNode.getAttributes().put("uid", folderNode.getUserId() + "");
		jsonNode.getAttributes().put("type", folderNode.getFolderType());
		jsonNode.getAttributes().put("layer", folderNode.getLayer() + "");
		jsonNode.getAttributes().put("pid", folderNode.getParentId() + "");
		if (folderNode.getParentId() == null || 0l == folderNode.getParentId()) {
			jsonNode.setState("open");
		}
		jsonNode.setText(folderNode.getFolderName());
		List<FolderNode> subNodes = folderNode.getSubNodes();
		if (subNodes.size() > 0) {
			children = new ArrayList<JsonNode>();
			JsonNode nodeTemp = null;

			for (int i = 0; i < subNodes.size(); i++) {
				nodeTemp = copyFolderNodeToJsonNode(subNodes.get(i));
				children.add(nodeTemp);
			}
			jsonNode.setChildren(children);
		}
		return jsonNode;
	};

	/**
	 * get target folder layer
	 * 
	 * @param folder
	 *            target folder
	 * @return the layer that in directory tree
	 * @author bxji
	 */
	public static Long getLayer(Folder folder) {
		if (folder == null) {
			return 0l;
		}
		Long layer = 0l;
		Folder temp = folder;
		while (true) {
			if (temp.getParent() != null) {
				layer++;
				temp = temp.getParent();
			} else {
				return layer;
			}
		}
	}

	/**
	 * sort the json entity by given sort conditions
	 * 
	 * @param entitys
	 *            targeted list
	 * @param sortBy
	 *            sort by what
	 * @param sortType
	 *            sort type: asc ,desc
	 * @return List<FileOrFolderJsonEntity>
	 * @author yonghui
	 */
	public static List<FileOrFolderJsonEntity> sortJsonObjs(
			List<FileOrFolderJsonEntity> entitys, String sortBy, String sortType) {
		if (null == entitys || entitys.size() == 0) {
			return entitys;
		}
		if (sortBy == null || sortType == null) {
			return entitys;
		}
		CollectionUtil.getSortedList(entitys, sortBy, sortType);
		return entitys;

	}

}
