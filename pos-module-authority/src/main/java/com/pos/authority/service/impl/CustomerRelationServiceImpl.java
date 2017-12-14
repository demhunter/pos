/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.impl;

import com.pos.authority.condition.query.ChildrenCondition;
import com.pos.authority.dao.CustomerRelationDao;
import com.pos.authority.dto.relation.ChildInfoDto;
import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.authority.dto.statistics.DescendantStatisticsDto;
import com.pos.authority.service.CustomerRelationService;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Queue;

/**
 * 客户关系ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerRelationServiceImpl implements CustomerRelationService {

    @Resource
    private CustomerService customerService;

    @Resource
    private CustomerStatisticsService customerStatisticsService;

    @Resource
    private CustomerRelationDao customerRelationDao;

    @Resource
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @Override
    public CustomerRelationDto getRelation(Long childUserId) {
        FieldChecker.checkEmpty(childUserId, "childUserId");

        return customerRelationDao.getByChildUserId(childUserId);
    }

    @Override
    public CustomerDto getParentCustomer(Long childUserId) {
        FieldChecker.checkEmpty(childUserId, "childUserId");

        CustomerRelationDto relation = customerRelationDao.getByChildUserId(childUserId);
        CustomerDto parent = null;
        if (relation != null) {
            if (relation.getParentUserId().equals(0L)) {
                // 当前用户不存在上级
                parent = new CustomerDto();
                parent.setId(0L);
            } else {
                parent = customerService.findById(relation.getParentUserId(), true, true);
            }
        }

        return parent;
    }

    @Override
    public Queue<CustomerRelationDto> generateBrokerageParticipatorQueue(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return customerRelationPoolSupport.generateBrokerageParticipatorQueue(userId);
    }

    @Override
    public ApiResult<NullObject> saveChildRemark(Long userId, Long childUserId, String remark) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(childUserId, "childUserId");
        FieldChecker.checkMaxLength(remark, 30, "remark");

        CustomerRelationDto relation = customerRelationDao.getByChildUserId(childUserId);
        if (relation == null || !relation.getParentUserId().equals(userId)) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        relation.setRemark(remark);
        relation.setUpdateUserId(userId);
        customerRelationDao.updateChildRemark(relation);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<Pagination<ChildInfoDto>> queryChildren(ChildrenCondition condition, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(condition, "condition");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");

        int totalCount = customerRelationDao.queryChildrenCount(condition);
        Pagination<ChildInfoDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<ChildInfoDto> children = customerRelationDao.queryChildren(condition, limitHelper);
            if (!CollectionUtils.isEmpty(children)) {
                children.forEach(e -> {
                    e.setChildStatistics(customerStatisticsService.getDescendantStatistics(e.getUserId()));
                });

                pagination.setResult(children);
            }
        }
        return ApiResult.succ(pagination);
    }
}
