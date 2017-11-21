/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 平台数据统计-支持时间搜索类型
 *
 * @author wangbing
 * @version 1.0, 2017/03/28
 */
public class TimeSearchTypeSupportDto implements Serializable {

    @ApiModelProperty("开始时间(Date)")
    private Date beginTime;

    @ApiModelProperty("截止时间(Date)")
    private Date endTime;

    @ApiModelProperty("是否支持按日搜索（TRUE = 支持，FALSE = 不支持）")
    private boolean daySupported;

    @ApiModelProperty("是否支持按周搜索（TRUE = 支持，FALSE = 不支持）")
    private boolean weekSupported;

    @ApiModelProperty("是否支持按月搜索（TRUE = 支持，FALSE = 不支持）")
    private boolean monthSupported;

    public TimeSearchTypeSupportDto(Date beginTime, Date endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        checkDate();
        calculateSupported();
    }

    /**
     * 检查日期的合法性
     */
    private void checkDate() {
        FieldChecker.checkEmpty(beginTime, "TimeSearchTypeSupportDto.beginTime");
        FieldChecker.checkEmpty(endTime, "TimeSearchTypeSupportDto.endTime");
        if (beginTime.after(endTime)) {
            throw new IllegalParamException("开始时间不能大于结束时间");
        }
    }

    /**
     * 根据开始截止日期计算所支持的搜索方式
     */
    private void calculateSupported() {
        daySupported = Boolean.TRUE;
        LocalDateTime begin = LocalDateTime.ofInstant(beginTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        Period period = Period.between(begin.toLocalDate(), end.toLocalDate());
        if (period.get(ChronoUnit.YEARS) >= 1 || period.get(ChronoUnit.MONTHS) >= 1) {
            monthSupported = Boolean.TRUE;
            weekSupported = Boolean.TRUE;
        } else {
            monthSupported = Boolean.FALSE;
            int beginWeek = begin.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
            int endWeek = end.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
            if (endWeek <= beginWeek) {
                weekSupported = Boolean.FALSE;
            } else {
                if (endWeek - beginWeek == 1) {
                    int endDayOfWeek = end.get(ChronoField.DAY_OF_WEEK);
                    if (endDayOfWeek >= 6) {
                        weekSupported = Boolean.TRUE;
                    } else {
                        weekSupported = Boolean.FALSE;
                    }
                } else {
                    weekSupported = Boolean.TRUE;
                }
            }
        }
    }

    public boolean isDaySupported() {
        return daySupported;
    }

    public boolean isWeekSupported() {
        return weekSupported;
    }

    public boolean isMonthSupported() {
        return monthSupported;
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
}
