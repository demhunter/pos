/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.impl;

import com.pos.authority.condition.query.ChildrenCondition;
import com.pos.authority.dao.CustomerRelationDao;
import com.pos.authority.dto.relation.ChildInfoDto;
import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.authority.exception.AuthorityErrorCode;
import com.pos.authority.service.CustomerRelationService;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import org.springframework.data.redis.core.RedisTemplate;
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

    private final static Long MODIFY_TOKEN_EXPIRE_TIME = 1L; // 客户关系更改Token时效(单位：分钟)

    @Resource
    private CustomerService customerService;

    @Resource
    private CustomerStatisticsService customerStatisticsService;

    @Resource
    private CustomerRelationDao customerRelationDao;

    @Resource
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

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

    @Override
    public ApiResult<NullObject> modifyCustomerRelation(Long childUserId, Long parentUserId, UserIdentifier operator) {
        FieldChecker.checkEmpty(childUserId, "childUserId");
        FieldChecker.checkEmpty(operator, "operator");
        FieldChecker.checkEmpty(parentUserId, "parentUserId");
        if (parentUserId <= 0) {
            throw new IllegalParamException("无效的上级用户[" + parentUserId + "]");
        }
        if (childUserId.equals(parentUserId)) {
            throw new IllegalParamException("客户自己不能成为自己的上级");
        }

        // 获取下级客户的关系信息
        CustomerRelationDto oldChildRelation = customerRelationPoolSupport.getCustomerRelation(childUserId);
        if (oldChildRelation == null) {
            return ApiResult.fail(AuthorityErrorCode.CUSTOMER_RELATION_CHILD_NOT_EXISTED);
        }
        // 更改前后关系一致，不需要修改，返回成功
        if (parentUserId.equals(oldChildRelation.getParentUserId())) {
            return ApiResult.succ();
        }
        // 新上级是否是当前客户的直接或间接下级
        List<CustomerRelationDto> ancestors = customerRelationPoolSupport.getCustomerAncestors(parentUserId);
        if (CollectionUtils.isEmpty(ancestors)) {
            return ApiResult.fail(AuthorityErrorCode.CUSTOMER_RELATION_PARENT_NOT_EXISTED);
        } else {
            // 关系环校验
            for (CustomerRelationDto ancestor : ancestors) {
                if (ancestor.getUserId().equals(childUserId)) {
                    return ApiResult.fail(AuthorityErrorCode.CUSTOMER_RELATION_MODIFY_ERROR_FOR_CIRCLE);
                }
            }
        }
        // 更新关系
        // 1、更改用户上下级关系[DB]
        // 2、刷新用户上下级关系[cache]
        customerRelationDao.updateCustomerRelation(childUserId, parentUserId, operator);
        CustomerRelationDto newChildRelation = customerRelationDao.getByChildUserId(childUserId);
        customerRelationPoolSupport.updateCustomerRelation(oldChildRelation, newChildRelation);

        return ApiResult.succ();
    }
}
