/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.support;

import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.dao.CustomerRelationDao;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.authority.service.support.relation.CustomerRelationNode;
import com.pos.authority.service.support.relation.CustomerRelationTree;
import com.pos.basic.constant.RedisConstants;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.validation.FieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 客户关系树支持
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
@Component
public class CustomerRelationPoolSupport {

    private final static Logger LOG = LoggerFactory.getLogger(CustomerRelationPoolSupport.class);

    private final static Long CUSTOMER_ROOT_NODE_USER_ID = 0L; // 根节点用户id

    private final static long CUSTOMER_RELATION_TREE_CACHE_TIME = 300L; // 关系树缓存时间

    @Resource
    private CustomerRelationDao customerRelationDao;

    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    @Resource
    private RedisTemplate<String, CustomerRelationTree> relationTreeTemplate;

    @Resource
    private CustomerLevelSupport customerLevelSupport;

    @PostConstruct
    private void initializeCustomerRoot() {
        CustomerRelationNode rootNode = getNodeInfo(CUSTOMER_ROOT_NODE_USER_ID);
        if (rootNode == null) {
            rootNode = initializeRootNode();
            saveNodeSelfInfo(rootNode);
        }
    }

    /**
     * 初始化客户关系树
     */
    public void initialize() {

        Map<Long, CustomerRelationNode> relationMap = new HashMap<>();

        // 初始化根节点信息
        CustomerRelationNode rootNode = initializeRootNode();
        relationMap.put(rootNode.getUserId(), rootNode);

        // 查询现有关系
        LimitHelper limitHelper = LimitHelper.create(1, Integer.MAX_VALUE, false);
        List<CustomerRelationDto> relations = customerRelationDao.getRelations(limitHelper);

        // 构建节点自身信息
        if (!CollectionUtils.isEmpty(relations)) {
            relations.forEach(relation -> {
                CustomerRelationNode node = new CustomerRelationNode(relation);
                relationMap.put(node.getUserId(), node);
            });
        }

        // 构建节点的直接子节点信息，形成关系树
        relationMap.forEach((Long key, CustomerRelationNode value) -> {
            if (value != null && value.getParentUserId() != null) {
                relationMap.get(value.getParentUserId()).getChildren().add(value.getUserId());
            }
        });

        // 保存节点信息
        relationMap.forEach((key, value) -> {
            saveNodeSelfInfo(value);
        });
    }

    /**
     * 添加一个新用户关系到用户关系池中
     *
     * @param relation 用户关系
     */
    public void addCustomerRelation(CustomerRelationDto relation) {
        CustomerRelationNode node = new CustomerRelationNode(relation);

        // 保存节点自身信息
        saveNodeSelfInfo(node);
        // 把子节点添加到父节点的直接下级集合中
        addChildToParent(node);
    }

    /**
     * 把子节点添加到父节点的直接下级集合中
     *
     * @param childNode 子节点信息
     */
    private void addChildToParent(CustomerRelationNode childNode) {
        // 根节点没有父节点
        if (childNode.getParentUserId() != null) {
            redisTemplate.opsForSet().add(
                    RedisConstants.POS_CUSTOMER_RELATION_NODE_CHILDREN + childNode.getParentUserId(),
                    childNode.getUserId().toString());
        }
    }

    /**
     * 保存节点自身信息
     *
     * @param node 节点信息
     */
    private void saveNodeSelfInfo(CustomerRelationNode node) {
        // 保存节点自身信息
        Map<String, Object> nodeInfo = new HashMap<>();
        nodeInfo.put("level", String.valueOf(node.getLevel()));
        nodeInfo.put("withdrawRate", node.getWithdrawRate().toPlainString());
        nodeInfo.put("extraServiceCharge", node.getExtraServiceCharge().toPlainString());
        nodeInfo.put("auditStatus", node.getAuditStatus().toString());
        if (node.getParentUserId() != null) {
            nodeInfo.put("parentUserId", node.getParentUserId().toString());
        }
        if (node.getRelationTime() != null) {
            nodeInfo.put("relationTime", SimpleDateUtils.formatDate(node.getRelationTime(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()));
        }
        redisTemplate.opsForHash().putAll(RedisConstants.POS_CUSTOMER_RELATION_NODE + node.getUserId(), nodeInfo);

        // 保存直接子节点信息
        if (!CollectionUtils.isEmpty(node.getChildren())) {
            node.getChildren().forEach(e -> {
                redisTemplate.opsForSet().add(
                        RedisConstants.POS_CUSTOMER_RELATION_NODE_CHILDREN + node.getUserId(),
                        e.toString());
            });
        }
    }

    /**
     * 获取用户节点信息
     *
     * @param userId 用户id
     */
    private CustomerRelationNode getNodeInfo(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");
        Map<Object, Object> data = redisTemplate.opsForHash().entries(RedisConstants.POS_CUSTOMER_RELATION_NODE + userId);
        if (CollectionUtils.isEmpty(data)) {
            LOG.error("用户{}在关系池中不存在", userId);
            return null;
        }

        CustomerRelationNode node = new CustomerRelationNode();
        node.setUserId(userId);
        node.setLevel(Integer.valueOf((String) data.get("level")));
        node.setWithdrawRate(new BigDecimal((String) data.get("withdrawRate")));
        node.setExtraServiceCharge(new BigDecimal((String) data.get("extraServiceCharge")));
        node.setAuditStatus(Integer.valueOf((String) data.get("auditStatus")));
        if (data.get("parentUserId") != null) {
            node.setParentUserId(Long.valueOf((String) data.get("parentUserId")));
        }
        if (data.get("relationTime") != null) {
            node.setRelationTime(SimpleDateUtils.parseDate(
                    (String) data.get("relationTime"),
                    SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()));
        }
        Set<Serializable> childrenSet = redisTemplate.opsForSet().members(RedisConstants.POS_CUSTOMER_RELATION_NODE_CHILDREN + node.getUserId());
        if (!CollectionUtils.isEmpty(childrenSet)) {
            node.setChildren(childrenSet.stream().map(child -> Long.valueOf((String) child)).collect(Collectors.toSet()));
        }

        return node;
    }

    /**
     * 从关系池中获取指定用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public CustomerRelationDto getCustomerRelation(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");
        Map<Object, Object> data = redisTemplate.opsForHash().entries(RedisConstants.POS_CUSTOMER_RELATION_NODE + userId);
        if (CollectionUtils.isEmpty(data)) {
            LOG.error("用户{}在关系池中不存在，查询数据库...", userId);
            CustomerRelationDto relationDto = customerRelationDao.getByChildUserId(userId);
            if (relationDto != null) {
                LOG.error("查找到用户{}的信息，加入关系池中");
                addCustomerRelation(relationDto);
                return relationDto;
            }
            LOG.error("用户{}不存在", userId);
            return null;
        }
        CustomerRelationDto customerRelation = new CustomerRelationDto();
        customerRelation.setUserId(userId);
        customerRelation.setLevel(Integer.valueOf((String) data.get("level")));
        customerRelation.setWithdrawRate(new BigDecimal((String) data.get("withdrawRate")));
        customerRelation.setExtraServiceCharge(new BigDecimal((String) data.get("extraServiceCharge")));
        customerRelation.setAuditStatus(Integer.valueOf((String) data.get("auditStatus")));
        customerRelation.setParentUserId(Long.valueOf((String) data.get("parentUserId")));
        customerRelation.setRelationTime(SimpleDateUtils.parseDate(
                (String) data.get("relationTime"),
                SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()));

        return customerRelation;
    }

    /**
     * 更新缓存中用户的身份认证审核状态
     *
     * @param userId      用户id
     * @param auditStatus 目标状态
     * @return 更新结果
     */
    public boolean updateAuditStatus(Long userId, CustomerAuditStatus auditStatus) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(auditStatus, "auditStatus");

        redisTemplate.opsForHash().put(RedisConstants.POS_CUSTOMER_RELATION_NODE + userId, "auditStatus", String.valueOf(auditStatus.getCode()));

        return true;
    }

    /**
     * 更新用户等级收款配置信息
     *
     * @param permission 等级收款配置信息
     * @return 更新结果
     */
    public boolean updateLevelConfig(CustomerPermissionDto permission) {
        FieldChecker.checkEmpty(permission, "permission");

        Map<String, Object> nodeInfo = new HashMap<>();
        nodeInfo.put("level", permission.getLevel().toString());
        nodeInfo.put("withdrawRate", permission.getWithdrawRate().toPlainString());
        nodeInfo.put("extraServiceCharge", permission.getExtraServiceCharge().toPlainString());

        redisTemplate.opsForHash().putAll(RedisConstants.POS_CUSTOMER_RELATION_NODE + permission.getUserId(), nodeInfo);

        return true;
    }

    /**
     * 生成参与分佣队列，队列头为交易用户信息<br>
     * PS：当队列长度大于2时，才有相应参与分佣的上级客户<br>
     * 分佣参与者队列生成规则：[ROOT_USER_ID：关系树最顶层用户id；MAX_LEVEL：分佣等级限制]<br>
     * 1、当前交易用户直接加入队列，作为队列头，记录当前等级游标[cursorLevel]和父级用户id[parentUserId]；<br>
     * 2、是否需要往上层搜索：expression = cursorLevel < MAX_LEVEL && parentUserId != ROOT_USER_ID；<br>
     * 3、CASE_A：当expression = true时，往上搜索获取下个可能参与分佣的用户[nextParticipator]，
     * 3.1、更新parentUserId = nextParticipator.parentUserId，
     * 3.2、判断levelExpression = nextParticipator.level > cursorLevel，当levelExpression = true时，更新cursorLevel = nextParticipator.level
     * 3.3、跳转至第【2】步；
     * CASE_B：当expression = false时，结束搜索；
     * 4、返回分佣参与队列
     *
     * @param userId 交易用户id
     * @return 分佣参与者队列
     */
    public Queue<CustomerRelationDto> generateBrokerageParticipatorQueue(Long userId) {
        Queue<CustomerRelationDto> participatorQueue = new ArrayDeque<>();

        // 交易所属用户加入队列
        CustomerRelationDto transactionUser = getCustomerRelation(userId);
        if (transactionUser == null) {
            return participatorQueue;
        }
        participatorQueue.add(transactionUser);

        final long ROOT_USER_ID = 0; // 关系树最顶层用户id
        final int MAX_LEVEL = customerLevelSupport.getMaxLevelConfig().getLevel(); // 分佣等级限制
        int cursorLevel = transactionUser.getLevel();
        long parentUserId = transactionUser.getParentUserId();

        while (cursorLevel < MAX_LEVEL && parentUserId != ROOT_USER_ID) {
            CustomerRelationDto nextParticipator = getCustomerRelation(parentUserId);
            if (nextParticipator == null) {
                break;
            }
            parentUserId = nextParticipator.getParentUserId();
            if (nextParticipator.getLevel() > cursorLevel) {
                participatorQueue.add(nextParticipator);
                cursorLevel = nextParticipator.getLevel();
            }
        }

        return participatorQueue;
    }

    /**
     * 获取以用户为根节点的关系树
     *
     * @param userId 用户id
     * @return 关系树
     */
    public CustomerRelationTree getCustomerRelationTree(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        // 查看缓存是否存在用户的关系树，存在则直接返回
        CustomerRelationTree tree = relationTreeTemplate.opsForValue().get(RedisConstants.POS_CUSTOMER_RELATION_TREE + userId);
        if (tree != null) {
            return tree;
        }
        // 不存在，生成用户的关系树
        return buildRelationTree(userId);
    }

    private CustomerRelationTree buildRelationTree(Long userId) {
        CustomerRelationNode rootNode = getNodeInfo(userId);
        if (rootNode == null) {
            LOG.error("用户{}在关系池中不存在，无法生成其关系树", userId);
            return null;
        }
        // 生成关系树
        CustomerRelationTree parentTree = new CustomerRelationTree();
        BeanUtils.copyProperties(rootNode, parentTree);
        if (!CollectionUtils.isEmpty(rootNode.getChildren())) {
            Map<Long, CustomerRelationTree> childrenTrees = new HashMap<>();
            rootNode.getChildren().forEach(childUserId -> {
                CustomerRelationTree childTree = getCustomerRelationTree(childUserId);
                if (childTree != null) {
                    childrenTrees.put(childUserId, childTree);
                }
            });
            parentTree.setChildrenTrees(childrenTrees);
        }
        // 缓存生成的关系树（5 min）
        relationTreeTemplate.opsForValue().set(
                RedisConstants.POS_CUSTOMER_RELATION_TREE + userId, parentTree,
                CUSTOMER_RELATION_TREE_CACHE_TIME, TimeUnit.SECONDS);

        return parentTree;
    }

    /*private void addNodeToTree(CustomerRelationNode node) {
        Serializable serializable = redisTemplate.opsForValue().get(RedisConstants.POS_CUSTOMER_RELATION_TREE);
        if (serializable == null) {
            LOG.error("用户关系添加错误：关系树不存在");
            throw new IllegalStateException("用户关系添加错误：关系树不存在");
        }
        CustomerRelationNode relationTree = (CustomerRelationNode) serializable;

        CustomerRelationNode parentNode = getParentNodeByDFS(relationTree, node);
        if (parentNode == null) {
            LOG.error("用户关系添加错误：节点[{}]的父节点在关系树中不存在", node);
            throw new IllegalStateException("用户关系添加错误：节点{" + node.getUserId() + "}的父节点在关系树中不存在");
        }
        parentNode.getChildren().add(node.getUserId());

        // 保存完整关系树
        redisTemplate.opsForValue().set(RedisConstants.POS_CUSTOMER_RELATION_TREE, relationTree);
    }

    private void buildRelationTree(CustomerRelationNode tree, CustomerRelationNode node, Map<Long, CustomerRelationNode> nodeMap) {
        CustomerRelationNode parentInfo = nodeMap.get(node.getParentUserId());
        // 父节点是否加入关系树（没有加入，父节点先加入关系树）
        if (parentInfo != null) {
            buildRelationTree(tree, parentInfo, nodeMap);
        }
        // 查询当前节点是否已经加入关系树，已加入不做任何处理
        CustomerRelationNode childNode = nodeMap.get(node.getUserId());
        if (childNode != null) {
            // 当前节点没有加入关系树，遍历树，查找到父节点
            CustomerRelationNode parentNode = getParentNodeByDFS(tree, node);
            if (parentNode == null) {
                throw new IllegalStateException("关系树初始化错误，用户" + node.getUserId() + "的上级" + node.getParentUserId() + "不再关系树中！");
            }
            parentNode.getChildren().add(node.getUserId());
            nodeMap.remove(node.getUserId());
        }
    }*/

    /*public CustomerRelationTree initializeRelationTree() {
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
    }*/

    /*// 深度优先遍历子树，查找父节点
    private CustomerRelationNode getParentNodeByDFS(CustomerRelationNode parentTree, CustomerRelationNode childInfo) {
        if (parentTree.getUserId().equals(childInfo.getParentUserId())) {
            return parentTree;
        } else {
            // 深度优先遍历子树
            if (!CollectionUtils.isEmpty(parentTree.getChildren())) {
                *//*for(CustomerRelationNode childTree : parentTree.getChildren().values()) {
                    CustomerRelationNode parentNode = getParentNodeByDFS(childTree, childInfo);
                    if (parentNode != null) {
                        return parentNode;
                    }
                }*//*
            }
        }
        return null;
    }

    // 广度优先搜索叶子节点的父节点
    private CustomerRelationNode getParentNodeByBFS(CustomerRelationNode leafNode) {
        LinkedList<CustomerRelationNode> breadthList = Lists.newLinkedList();
        breadthList.push(relationTree);
        while (!CollectionUtils.isEmpty(breadthList)) {
            CustomerRelationNode parentNode = breadthList.pop();
            if (parentNode.getUserId().equals(leafNode.getParentUserId())) {
                return parentNode;
            }
            if (!CollectionUtils.isEmpty(parentNode.getChildren())) {
                // parentNode.getChildren().values().forEach(e -> breadthList.push(e.copyContainDescendant(true)));
            }
        }

        return null;
    }

    // 广度优先搜索参与分佣的用户链表，搜索成功则返回一个链表：其中链表头为交易用户信息，链表尾为根节点信息
    public Stack<CustomerRelationNode> getParticipationForBrokerage(Long userId) {
        Stack<CustomerRelationNode> participationStack = new Stack<>();
        // 根节点始终参与结算
        participationStack.push(relationTree.copyContainDescendant(false));
        // 广度优先搜索列表（根节点不参与交易，直接从第二层级开始搜索）
        LinkedList<CustomerRelationNode> breadthList = Lists.newLinkedList();
        // Queue<CustomerRelationNode> breadthQueue = new LinkedList<>();
        if (!CollectionUtils.isEmpty(relationTree.getChildren())) {
            // relationTree.getChildren().values().forEach(e -> breadthList.add(e.copyContainDescendant(true)));
        }
        while (!CollectionUtils.isEmpty(breadthList)) {
            CustomerRelationNode node = breadthList.pop();
            // 判断是否进入下一层级
            if (!participationStack.peek().getUserId().equals(node.getParentUserId())) {
                CustomerRelationNode parentNode = getParentNodeByBFS(node);
                if (parentNode == null) {
                    throw new IllegalStateException("用户" + node.getUserId() + "的上级" + node.getParentUserId() + "不再关系树中！");
                }
                if (!participationStack.peek().getUserId().equals(parentNode.getParentUserId())) {
                    participationStack.pop();
                }
                participationStack.push(parentNode.copy());
            }
            // 判断当前节点信息
            if (node.getUserId().equals(userId)) {
                participationStack.push(node.copyContainDescendant(false));
                break;
            }
            if (!CollectionUtils.isEmpty(node.getChildren())) {
                // node.getChildren().values().forEach(e -> breadthList.add(e.copyContainDescendant(true)));
            }
        }

        return participationStack;
    }*/

    private CustomerRelationNode initializeRootNode() {
        CustomerRelationNode rootNode = new CustomerRelationNode();

        rootNode.setUserId(0L);
        rootNode.setLevel(100);
        rootNode.setWithdrawRate(BigDecimal.ZERO);
        rootNode.setExtraServiceCharge(BigDecimal.ZERO);
        rootNode.setAuditStatus(CustomerAuditStatus.AUDITED.getCode());

        return rootNode;
    }

}
