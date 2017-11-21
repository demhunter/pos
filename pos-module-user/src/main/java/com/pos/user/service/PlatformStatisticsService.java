/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

/**
 * @author 睿智
 * @version 1.0, 2017/7/12
 */
public interface PlatformStatisticsService {

    /**
     * 查询数据之前更新家居顾问 的统计数据
     * @param userId 需要更新数据的userId
     */
    void updatePlatformEmployeeStatistics(long userId,long peId);

    /**
     * 查询数据之前更新家居顾问 客户的统计数据
     * @param userId 需要更新数据的userId
     */
    void updatePlatformCustomerStatistics(long userId);
}
