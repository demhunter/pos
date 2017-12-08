/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.impl;

import com.google.common.collect.Lists;
import com.pos.authority.dao.CustomerStatisticsDao;
import com.pos.authority.domain.CustomerLevelConfig;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.dto.statistics.CustomerStatisticsDto;
import com.pos.authority.dto.statistics.DescendantLevelStatisticsDto;
import com.pos.authority.dto.statistics.DescendantStatisticsDto;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.authority.service.support.CustomerLevelSupport;
import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.authority.service.support.relation.CustomerRelationTree;
import com.pos.common.util.validation.FieldChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 客户统计ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerStatisticsServiceImpl implements CustomerStatisticsService {

    @Resource
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @Resource
    private CustomerLevelSupport customerLevelSupport;

    @Resource
    private CustomerStatisticsDao customerStatisticsDao;

    @Resource
    private CustomerAuthorityService customerAuthorityService;

    @Override
    public CustomerStatisticsDto getStatistics(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return customerStatisticsDao.getByUserId(userId);
    }

    @Override
    public DescendantStatisticsDto getDescendantStatistics(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        DescendantStatisticsDto descendantStatistics = new DescendantStatisticsDto();

        CustomerRelationTree relationTree = customerRelationPoolSupport.getCustomerRelationTree(userId);
        if (relationTree != null && !CollectionUtils.isEmpty(relationTree.getChildrenTrees())) {
            Map<Integer, DescendantLevelStatisticsDto> levelStatisticsMap = new HashMap<>();

            // 广度优先遍历（BFS）统计下级数量
            LinkedList<CustomerRelationTree> breadthList = Lists.newLinkedList();
            pushAllChildren(breadthList, relationTree);
            while (!CollectionUtils.isEmpty(breadthList)) {
                CustomerRelationTree tree = breadthList.poll();
                pushAllChildren(breadthList, tree);
                DescendantLevelStatisticsDto levelStatistics = levelStatisticsMap.get(tree.getLevel());
                if (levelStatistics == null) {
                    levelStatisticsMap.putIfAbsent(tree.getLevel(), new DescendantLevelStatisticsDto(tree.getLevel()));
                    levelStatistics = levelStatisticsMap.get(tree.getLevel());
                }
                // 统计直接下级和间接下级
                if (userId.equals(tree.getParentUserId())) {
                    levelStatistics.incrementChild();
                } else {
                    levelStatistics.incremnetDescendant();
                }
            }
            List<DescendantLevelStatisticsDto> levelStatistics = Lists.newArrayList(levelStatisticsMap.values());
            Collections.sort(levelStatistics);
            descendantStatistics.setLevelStatistics(levelStatistics);
            // 累计下级总数
            descendantStatistics.statisticsDescendant();
        }

        return descendantStatistics;
    }

    private void pushAllChildren(LinkedList<CustomerRelationTree> breadthList, CustomerRelationTree parentTree) {
        if (!CollectionUtils.isEmpty(parentTree.getChildrenTrees())) {
            parentTree.getChildrenTrees().values().forEach(childTree -> breadthList.push(childTree));
        }
    }

    @Override
    public void incrementChildrenCount(Long parentUserId) {
        FieldChecker.checkEmpty(parentUserId, "parentUserId");

        CustomerStatisticsDto statistics = customerStatisticsDao.getByUserId(parentUserId);
        CustomerPermissionDto permission = customerAuthorityService.getPermission(parentUserId);

        statistics.setChildrenCount(statistics.getChildrenCount() + 1);

        Integer maxLevel = customerLevelSupport.getMaxLevel();
        CustomerLevelConfig nextLevelConfig = null;
        if (permission.getLevel() < maxLevel) {
            nextLevelConfig = customerLevelSupport.getLevelConfig(permission.getLevel() + 1);
        }
        if (nextLevelConfig != null) {
            if (statistics.getChildrenCount() >= nextLevelConfig.getChildrenLimit()
                    && nextLevelConfig.getChildrenLimit() > 0) {
                // 已支付等级晋升服务费达到限额，晋升等级
                customerAuthorityService.upgradeLevel(permission, nextLevelConfig, parentUserId);
            }
        }
    }

    @Override
    public void incrementUpgradeCharge(Long userId, BigDecimal serviceCharge) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(serviceCharge, "serviceCharge");

        CustomerStatisticsDto statistics = customerStatisticsDao.getByUserId(userId);
        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);

        statistics.setPaidCharge(statistics.getPaidCharge().add(serviceCharge));
        Integer maxLevel = customerLevelSupport.getMaxLevel();

        CustomerLevelConfig nextLevelConfig = null;
        if (permission.getLevel() < maxLevel) {
            nextLevelConfig = customerLevelSupport.getLevelConfig(permission.getLevel() + 1);
        }
        if (nextLevelConfig != null) {
            if (statistics.getPaidCharge().compareTo(nextLevelConfig.getChargeLimit()) >= 0
                    && nextLevelConfig.getChargeLimit().compareTo(BigDecimal.ZERO) > 0) {
                // 已支付等级晋升服务费达到限额，晋升等级
                customerAuthorityService.upgradeLevel(permission, nextLevelConfig, userId);
            }
        }

        customerStatisticsDao.incrementPaidCharge(userId, serviceCharge);
    }

    @Override
    public void incrementWithdrawAmount(Long userId, BigDecimal withdrawAmount) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(withdrawAmount, "withdrawAmount");

        CustomerStatisticsDto statistics = customerStatisticsDao.getByUserId(userId);
        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);

        statistics.setWithdrawAmount(statistics.getWithdrawAmount().add(withdrawAmount));

        Integer maxLevel = customerLevelSupport.getMaxLevel();

        CustomerLevelConfig nextLevelConfig = null;
        if (permission.getLevel() < maxLevel) {
            nextLevelConfig = customerLevelSupport.getLevelConfig(permission.getLevel() + 1);
        }

        if (nextLevelConfig != null) {
            if (statistics.getWithdrawAmount().compareTo(nextLevelConfig.getWithdrawAmountLimit()) >= 0
                    && nextLevelConfig.getWithdrawAmountLimit().compareTo(BigDecimal.ZERO) > 0) {
                // 已支付等级晋升服务费达到限额，晋升等级
                customerAuthorityService.upgradeLevel(permission, nextLevelConfig, userId);
            }
        }

        customerStatisticsDao.incrementWithdrawAmount(userId, withdrawAmount);
    }

    @Override
    public void incrementWithdrawalBrokerage(Long userId, BigDecimal brokerage) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(brokerage, "brokerage");

        customerStatisticsDao.incrementWithdrawalBrokerage(userId, brokerage);
    }
}
