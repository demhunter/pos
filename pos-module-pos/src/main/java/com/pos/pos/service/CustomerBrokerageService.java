/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service;

import com.pos.pos.dto.brokerage.BrokerageGeneralInfoDto;

/**
 * 客户佣金Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public interface CustomerBrokerageService {

    /**
     * 获取用户的佣金概要统计信息
     *
     * @param userId 用户id
     * @return 佣金概要统计信息
     */
    BrokerageGeneralInfoDto getBrokerageGeneral(Long userId);
}
