/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.user.condition.query.RecommendCondition;
import com.pos.user.constant.InvitationChannel;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.dto.employee.EmployeeMaterialRelationDto;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.user.dto.employee.RecommendDto;

/**
 * 客户业者主材邀请关系相关Service
 *
 * @author lifei
 * @version 1.0, 2017/7/5
 */
public interface EmployeeMaterialService {

    /**
     * 查询客户主材内购邀请关联信息
     *
     * @param customerId
     * @return 关联信息
     */
    EmployeeMaterialRelationDto findRelationByCustomerId(Long customerId);

    /**
     * 保存业者客户主材推荐关系
     *
     * @param customerDto 用户信息
     * @param employeeDto 业者信息
     * @return Boolean
     */
    void saveEmployeeMaterial(CustomerDto customerDto, EmployeeDto employeeDto, InvitationChannel channel);

    /**
     * 获取业者和客户之间关于主材内购的推荐关系
     * @param recommendCondition
     * @return
     */
    Pagination<RecommendDto> getRecommendList(RecommendCondition recommendCondition, LimitHelper limitHelper, OrderHelper orderHelper);
}
