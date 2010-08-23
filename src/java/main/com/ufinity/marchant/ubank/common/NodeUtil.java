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
     * @param folderNode FolderNode
     * @return return copied JsonNode
     */
    public static JsonNode copyFolderNodeToJsonNode(FolderNode folderNode) {
        if (folderNode == null) {
            return null;
        }
        JsonNode jsonNode = new JsonNode();
        List<JsonNode> children = null;
        jsonNode.setId(folderNode.getFolderId());
        jsonNode.setState("close");
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

}
