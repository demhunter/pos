/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.support;

import com.google.common.collect.Lists;
import com.pos.authority.dao.CustomerPermissionDao;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.dto.relation.CustomerRelationNode;
import com.pos.authority.dto.relation.CustomerRelationTree;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户关系树支持
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
@Component
public class CustomerRelationTreeSupport {

    private static CustomerRelationNode relationTree = null;

    private Map<Long, CustomerRelationNode> relationNodeMap = new HashMap<>();

    @Resource
    private CustomerPermissionDao customerPermissionDao;

    public CustomerRelationTree initializeRelationTree() {
        Map<Long, CustomerRelationNode> relationMap = new HashMap<>();
        List<CustomerRelationNode> relations = Lists.newArrayList();

        relationNodeMap.clear();
        CustomerRelationNode rootNode = initializeRootNode(); // 初始化根节点信息
        relationNodeMap.put(rootNode.getUserId(), rootNode);
        relationTree = rootNode.copy();

        List<CustomerPermissionDto> permissions = customerPermissionDao.query();
        if (!CollectionUtils.isEmpty(permissions)) {
            permissions.forEach(e -> {
                CustomerRelationNode node = buildCustomerRelation(e);
                relationNodeMap.put(node.getUserId(), node);

                relationMap.put(node.getUserId(), node.copy());
                relations.add(node.copy());
            });
            // 构建节点信息（节点自身信息和其直接子节点信息）
            relationNodeMap.forEach((Long key, CustomerRelationNode value) -> {
                if (value != null && value.getParentUserId() != null) {
                    CustomerRelationNode child = value.copy();
                    relationNodeMap.get(value.getParentUserId()).getChildren().put(child.getUserId(), child);
                }
            });
            // 根据节点信息构建关系树
            for (CustomerRelationNode relation : relations) {
                buildRelationTree(relation.copy(), relationMap);
            }
        }

        CustomerRelationTree result = new CustomerRelationTree();
        result.setRelationTree(relationTree);
        result.setTreeNodeMap(relationNodeMap);
        return result;
    }

    private void buildRelationTree(CustomerRelationNode relation, Map<Long, CustomerRelationNode> relationMap) {
        CustomerRelationNode parentInfo = relationMap.get(relation.getParentUserId());
        // 父节点是否加入关系树（没有加入，父节点先加入关系树）
        if (parentInfo != null) {
            buildRelationTree(parentInfo, relationMap);
        }
        // 查询当前节点是否已经加入关系树，已加入不做任何处理
        CustomerRelationNode childNode = relationMap.get(relation.getUserId());
        if (childNode != null) {
            // 当前节点没有加入关系树，遍历树，查找到父节点
            CustomerRelationNode parentNode = getParentNodeByDFS(relationTree, relation);
            if (parentNode == null) {
                throw new IllegalStateException("关系树初始化错误，用户" + relation.getUserId() +"的上级" + relation.getParentUserId() + "不再关系树中！");
            }
            parentNode.getChildren().put(relation.getUserId(), relation);
            relationMap.remove(relation.getUserId());
        }
    }

    // 深度优先遍历子树，查找父节点
    private CustomerRelationNode getParentNodeByDFS(CustomerRelationNode parentTree, CustomerRelationNode childInfo) {
        if (parentTree.getUserId().equals(childInfo.getParentUserId())) {
            return parentTree;
        } else {
            // 深度优先遍历子树
            if (!CollectionUtils.isEmpty(parentTree.getChildren())) {
                for(CustomerRelationNode childTree : parentTree.getChildren().values()) {
                    CustomerRelationNode parentNode = getParentNodeByDFS(childTree, childInfo);
                    if (parentNode != null) {
                        return parentNode;
                    }
                }
            }
        }
        return null;
    }

    private CustomerRelationNode initializeRootNode() {
        CustomerRelationNode rootNode = new CustomerRelationNode();

        rootNode.setUserId(0L);
        rootNode.setLevel(100);
        rootNode.setWithdrawPermission(2);
        rootNode.setWithdrawRate(BigDecimal.ZERO);

        return rootNode;
    }


    private CustomerRelationNode buildCustomerRelation(CustomerPermissionDto permission) {
        CustomerRelationNode relationNode = new CustomerRelationNode();

        BeanUtils.copyProperties(permission, relationNode);

        return relationNode;
    }

}
