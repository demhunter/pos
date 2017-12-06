/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service;

import com.pos.authority.dto.relation.CustomerRelationDto;
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
     * PS：当队列长度大于2时，才有相应参与分佣的上级客户<br>
     *
     * @param userId 交易产生用户id
     * @return 参与分佣队列
     */
    Queue<CustomerRelationDto> generateBrokerageParticipatorQueue(Long userId);
}
