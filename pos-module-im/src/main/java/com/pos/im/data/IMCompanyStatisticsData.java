/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.data;

/**
 * 公司相关的IM统计数据（从Redis中查询的结果）
 *
 * @author cc
 * @version 1.0, 16/7/20
 */
public class IMCompanyStatisticsData {

    /**
     * 今日新增聊天数
     */
    private Long sessionCountToday;

    /**
     * 累计聊天总数
     */
    private Long sessionCountTotal;

    public Long getSessionCountToday() {
        return sessionCountToday;
    }

    public void setSessionCountToday(Long sessionCountToday) {
        this.sessionCountToday = sessionCountToday;
    }

    public Long getSessionCountTotal() {
        return sessionCountTotal;
    }

    public void setSessionCountTotal(Long sessionCountTotal) {
        this.sessionCountTotal = sessionCountTotal;
    }
}
