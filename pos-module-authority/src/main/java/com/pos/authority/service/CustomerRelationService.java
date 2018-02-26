/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service;

import com.pos.authority.condition.query.ChildrenCondition;
import com.pos.authority.dto.relation.ChildInfoDto;
import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.user.dto.customer.CustomerDto;

import java.util.Queue;

/**
 * 客户关系Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public interface CustomerRelationService {

    /**
     * 获取指定用户的上下级关系
     *
     * @param childUserId 用户id
     * @return 上下级客户关系
     */
    CustomerRelationDto getRelation(Long childUserId);

    /**
     * 获取上级用户的客户信息
     *
     * @param childUserId 用户id
     * @return 用户的上级用户信息
     */
    CustomerDto getParentCustomer(Long childUserId);

    /**
     * 生成参与分佣队列，队列头为交易用户信息<br>
     * PS：当队列长度大于1时，才有相应参与分佣的上级客户<br>
     *
     * @param userId 交易产生用户id
     * @return 参与分佣队列
     */
    Queue<CustomerRelationDto> generateBrokerageParticipatorQueue(Long userId);

    /**
     * 保存下级客户备注
     *
     * @param userId      上级用户id
     * @param childUserId 下级用户id
     * @param remark      备注信息
     * @return 操作结果
     */
    ApiResult<NullObject> saveChildRemark(Long userId, Long childUserId, String remark);

    /**
     * 查询用户的直接下级信息
     *
     * @param condition   查询条件
     * @param limitHelper 分页参数
     * @return 查询结果
     */
    ApiResult<Pagination<ChildInfoDto>> queryChildren(ChildrenCondition condition, LimitHelper limitHelper);

    /**
     * 更改客户上下级关系
     *
     * @param childUserId  下级用户id
     * @param parentUserId 上级用户id
     * @param operator     更新操作用户标识
     * @return 操作结果
     */
    ApiResult<NullObject> modifyCustomerRelation(Long childUserId, Long parentUserId, UserIdentifier operator);
}
