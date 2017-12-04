/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.support.relation;

import com.pos.authority.service.support.relation.CustomerRelationNode;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * 客户关系树
 *
 * @author wangbing
 * @version 1.0, 2017/11/27
 */
public class CustomerRelationTree implements Serializable {

    @ApiModelProperty("完整关系树")
    private CustomerRelationNode relationTree;

    @ApiModelProperty("树节点信息（包含节点自身信息[parent]，及其直接下级信息集合[children]。PS：直接下级[child]不再包含其本身[child]的直接下级集合）")
    private Map<Long, CustomerRelationNode> treeNodeMap;

    public CustomerRelationNode getRelationTree() {
        return relationTree;
    }

    public void setRelationTree(CustomerRelationNode relationTree) {
        this.relationTree = relationTree;
    }

    public Map<Long, CustomerRelationNode> getTreeNodeMap() {
        return treeNodeMap;
    }

    public void setTreeNodeMap(Map<Long, CustomerRelationNode> treeNodeMap) {
        this.treeNodeMap = treeNodeMap;
    }
}
