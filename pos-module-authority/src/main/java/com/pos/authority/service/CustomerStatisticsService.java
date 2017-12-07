/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service;

import com.pos.authority.dto.statistics.CustomerStatisticsDto;
import com.pos.authority.dto.statistics.DescendantStatisticsDto;

/**
 * 客户统计Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public interface CustomerStatisticsService {

    /**
     * 获取指定用户的统计 信息
     *
     * @param userId 用户id
     * @return 客户统计信息
     */
    CustomerStatisticsDto getStatistics(Long userId);

    /**
     * 获取用户的下级统计信息
     *
     * @param userId 用户id
     * @return 下级统计信息
     */
    DescendantStatisticsDto getDescendantStatistics(Long userId);
}
