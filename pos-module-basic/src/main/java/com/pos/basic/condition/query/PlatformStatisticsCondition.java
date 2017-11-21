/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.condition.query;

import com.pos.basic.constant.PhoneSystemType;
import com.pos.basic.constant.StatisticsTimeRangeType;
import org.joda.time.LocalDateTime;

import java.util.Date;

/**
 * 平台数据统计Condition
 *
 * @author wangbing
 * @version 1.0, 2017/03/28
 */
@Deprecated
public class PlatformStatisticsCondition {

    /**
     * 时间段类型 {@link StatisticsTimeRangeType}
     */
    private Byte timeRangeType;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 时间搜索类型(1 = 按日, 2 = 按周, 3 = 按月)
     */
    private Byte timeSearchType;

    /**
     * 手机系统类型 {@link PhoneSystemType}
     */
    private Byte systemType;

    public Byte getTimeSearchType() {
        return timeSearchType;
    }

    public void setTimeSearchType(Byte timeSearchType) {
        this.timeSearchType = timeSearchType;
    }

    public Byte getSystemType() {
        return systemType;
    }

    public void setSystemType(Byte systemType) {
        this.systemType = systemType;
    }

    public Byte getTimeRangeType() {
        return timeRangeType;
    }

    public void setTimeRangeType(Byte timeRangeType) {
        this.timeRangeType = timeRangeType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    // 时间设置存在bug，需要修复
    public void checkAndFormatTime() {
        if (timeRangeType == null) {
            setTimeDefault();
        } else {
            switch (StatisticsTimeRangeType.getEnum(timeRangeType)) {
                case LATEST_WEEK:
                    setTime7();
                    break;
                case LATEST_MONTH:
                    setTime30();
                    break;
                case LATEST_TWO_MONTH:
                    setTime60();
                    break;
                default:
            }
        }
    }

    private void setTimeDefault() {
        setTime30();
    }

    private void setTime7() {
        LocalDateTime now = LocalDateTime.now();
        setEndTime(now.toDate());
        setBeginTime(now.minusDays(6).toDate());
    }

    private void setTime30() {
        LocalDateTime now = LocalDateTime.now();
        setEndTime(now.toDate());
        setBeginTime(now.minusDays(29).toDate());
    }

    private void setTime60() {
        LocalDateTime now = LocalDateTime.now();
        setEndTime(now.toDate());
        setBeginTime(now.minusDays(59).toDate());
    }
}
