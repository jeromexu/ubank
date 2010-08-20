// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:bixiang Ji
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
package com.ufinity.marchant.ubank.service;

import com.ufinity.marchant.ubank.bean.Folder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class is a node of in directory tree
 * 
 * @author bxji
 * @version 2010-8-17
 */
public class FolderNode {
    private Folder folder;
    private List<FolderNode> subNodes = new ArrayList<FolderNode>();

    /**
     * Generate a directory Tree
     * 
     * @param folders
     *            In accordance with the directory hierarchy of folders sorted
     *            collection
     * @return return a FolderNode
     */
    public static FolderNode GenerateFolderTree(List<Folder> folders) {
        FolderNode rootNode = null;
        Map<Long, FolderNode> nodes = new HashMap<Long, FolderNode>();

        if (folders == null || folders.isEmpty()) {
            return rootNode;
        }

        // if contains top root directory then the root directory is last one
        Folder lastFolder = folders.get(folders.size() - 1);
        if (lastFolder.getParent() == null) {
            rootNode = new FolderNode();
            rootNode.setFolder(lastFolder);
            nodes.put(lastFolder.getFolderId(), rootNode);

            for (int i = 0; i < folders.size() - 1; i++) {
                Folder folder = folders.get(i);
                FolderNode node = new FolderNode();
                node.setFolder(folder);
                nodes.put(folder.getFolderId(), node);
                FolderNode parentNode = null;
                if (folder.getParent() != null) {
                    parentNode = nodes.get(folder.getParent().getFolderId());
                }
                if (parentNode != null) {
                    parentNode.getSubNodes().add(node);
                }
            }
        }
        else { // part of the tree
            for (int i = 0; i < folders.size(); i++) {
                Folder folder = folders.get(i);
                FolderNode node = new FolderNode();
                node.setFolder(folder);
                if (i == 0) {
                    rootNode = node;
                }
                nodes.put(folder.getFolderId(), node);
                FolderNode parentNode = null;
                if (folder.getParent() != null) {
                    parentNode = nodes.get(folder.getParent().getFolderId());
                }
                if (parentNode != null) {
                    parentNode.getSubNodes().add(node);
                }
            }
        }
        return rootNode;
    }

    /**
     * the getter method of folder
     * 
     * @return the folder
     */
    public Folder getFolder() {
        return folder;
    }

    /**
     * the setter method of the folder
     * 
     * @param folder
     *            the folder to set
     */
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    /**
     * the getter method of subNodes
     * 
     * @return the subNodes
     */
    public List<FolderNode> getSubNodes() {
        return subNodes;
    }

    /**
     * the setter method of the subNodes
     * 
     * @param subNodes
     *            the subNodes to set
     */
    public void setSubNodes(List<FolderNode> subNodes) {
        this.subNodes = subNodes;
    }

}
