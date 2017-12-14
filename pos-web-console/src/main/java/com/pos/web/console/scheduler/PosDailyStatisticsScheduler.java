/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.scheduler;

import com.pos.transaction.service.PosStatisticsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 钱刷刷每日数据统计
 *
 * @author wangbing
 * @version 1.0, 2017/12/14
 */
@Component
public class PosDailyStatisticsScheduler {

    @Resource
    private PosStatisticsService posStatisticsService;

    @Scheduled(cron = "* * 1 * * ?")
    public void execute() {
        posStatisticsService.dailyStatisticsSchedule();
    }
}
