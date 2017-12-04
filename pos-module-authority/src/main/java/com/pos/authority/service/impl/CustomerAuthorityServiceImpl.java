/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.impl;

import com.pos.authority.dao.CustomerPermissionDao;
import com.pos.authority.dao.CustomerRelationDao;
import com.pos.authority.dao.CustomerStatisticsDao;
import com.pos.authority.domain.CustomerLevelConfig;
import com.pos.authority.domain.CustomerPermission;
import com.pos.authority.domain.CustomerRelation;
import com.pos.authority.domain.CustomerStatistics;
import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.authority.service.support.CustomerLevelSupport;
import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 客户权限ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerAuthorityServiceImpl implements CustomerAuthorityService {

    private final static Logger LOG = LoggerFactory.getLogger(CustomerAuthorityServiceImpl.class);

    @Resource
    private CustomerService customerService;

    @Resource
    private CustomerLevelSupport customerLevelSupport;

    @Resource
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @Resource
    private CustomerPermissionDao customerPermissionDao;

    @Resource
    private CustomerRelationDao customerRelationDao;

    @Resource
    private CustomerStatisticsDao customerStatisticsDao;

    @Override
    public void initialize(Long userId, Long parentUserId) {
        FieldChecker.checkEmpty(userId, "userId");

        CustomerDto newCustomer = customerService.findById(userId, true, true);
        if (newCustomer == null) {
            LOG.error("客户{}不存在，无法初始化权限和客户关系", userId);
        } else if (newCustomer.isAvailable()) {
            LOG.error("客户{}已被禁用，无法初始化权限和客户关系", userId);
        } else if (newCustomer.isDeleted()) {
            LOG.error("客户{}已被删除，无法初始化权限和客户关系", userId);
        } else {
            // 初始化客户收款权限并保存
            CustomerLevelConfig minLevelConfig = customerLevelSupport.getMinLevelConfig();
            CustomerPermission permission = new CustomerPermission(userId, minLevelConfig);
            customerPermissionDao.save(permission);

            // 初始化客户统计信息并保存
            CustomerStatistics statistics = new CustomerStatistics(userId);
            customerStatisticsDao.save(statistics);

            // 初始化客户关系信息
            CustomerDto parentCustomer = null;
            if (parentUserId != null) {
                parentCustomer = customerService.findById(parentUserId, false, false);
            }
            if (parentCustomer == null) {
                LOG.warn("客户{}的上级客户{}不存在", userId, parentUserId);
                parentUserId = 0L;

            }
            CustomerRelation relation = new CustomerRelation(userId, parentUserId);
            customerRelationDao.save(relation);

            // 往客户关系池中添加关系
            customerRelationPoolSupport.addCustomerRelation(buildRelationDto(permission, relation));

            // 更新父客户的直接下级客户数量统计
            customerStatisticsDao.incrementChildrenCount(parentUserId);
        }
    }

    private CustomerRelationDto buildRelationDto(CustomerPermission permission, CustomerRelation relation) {
        CustomerRelationDto relationDto = new CustomerRelationDto();
        relationDto.setId(relation.getId());
        relationDto.setUserId(relation.getUserId());
        relationDto.setLevel(permission.getLevel());
        relationDto.setWithdrawRate(permission.getWithdrawRate());
        relationDto.setExtraServiceCharge(permission.getExtraServiceCharge());
        relationDto.setAuditStatus(permission.getAuditStatus());
        relationDto.setParentUserId(relation.getParentUserId());
        relationDto.setRemark(relation.getRemark());
        relationDto.setRelationTime(new Date());

        return relationDto;
    }
}
