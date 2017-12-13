/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.scheduler;

import com.pos.transaction.service.support.TransactionScheduleSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 快捷收款交易状态查询定时任务<br>
 * 轮询查看队列中交易的状态，每两分钟轮询一次队列<br>
 * 1、交易成功：更新交易状态-交易成功，计算交易佣金<br>
 * 2、交易失败：更新交易状态-交易失败
 *
 * @author wangbing
 * @version 1.0, 2017/12/13
 */
@Component
public class PosTransactionScheduler {

    private static final Logger log = LoggerFactory.getLogger(PosTransactionScheduler.class);

    @Resource
    private TransactionScheduleSupport transactionScheduleSupport;

    @Scheduled(cron = "0 */2 * * * ?")
    public void execute() {
        log.info("-------------------->快捷收款轮询交易状态定时任务启动");
        try {
            transactionScheduleSupport.queryTransactionStatus();
        } catch (Exception e) {
            log.error("快捷收款交易状态查询定时任务异常，Exception={}", e);
        } finally {
            log.info("-------------------->快捷收款轮询交易状态定时任务结束");
        }
    }
}
