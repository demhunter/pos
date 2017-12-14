/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.transaction.dto.statistics.PosGeneralStatisticsDto;
import com.pos.transaction.dto.statistics.TransactionDailyStatisticsDto;

import java.util.Date;

/**
 * 交易统计Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/13
 */
public interface PosStatisticsService {

    /**
     * 查询每日数据统计
     *
     * @param beginTime   查询开始时间
     * @param endTime     查询截止时间
     * @param limitHelper 分页参数
     * @return 查询结果
     */
    ApiResult<Pagination<TransactionDailyStatisticsDto>> queryDailyStatistics(
            Date beginTime, Date endTime, LimitHelper limitHelper);

    /**
     * 查询POS概要统计信息
     *
     * @return 概要统计信息
     */
    ApiResult<PosGeneralStatisticsDto> queryGeneralStatistics();

    /**
     * 每日定时统计任务执行程序
     */
    void dailyStatisticsSchedule();

    /**
     * 初始化每日数据统计
     */
    void initializeDailyStatistics();
}
