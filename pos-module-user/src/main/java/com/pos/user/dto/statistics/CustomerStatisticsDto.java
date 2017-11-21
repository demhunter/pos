/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 平台统计数据-C端用户统计信息DTO
 *
 * @author wangbing
 * @version 1.0, 2017/03/31
 */
@Deprecated
public class CustomerStatisticsDto implements Serializable, Comparable<CustomerStatisticsDto> {

    @ApiModelProperty("数据所属日期点（map的Key）")
    private LocalDate dateKey;

    @ApiModelProperty("当天的新增用户数")
    private Integer newCount;

    @ApiModelProperty("当天的注册用户数")
    private Integer registerCount;

    @ApiModelProperty("当天的活跃用户数")
    private Integer activeCount;

    @ApiModelProperty("转化率（注册/新增）")
    public Integer getConversionPercent() {
        if (newCount <= 0) {
            return 0;
        }
        Float percent = ((float) registerCount / newCount) * 100;
        return percent.intValue();
    }

    public CustomerStatisticsDto(LocalDate dateKey) {
        this.dateKey = dateKey;
        newCount = 0;
        registerCount = 0;
        activeCount = 0;
    }

    public LocalDate getDateKey() {
        return dateKey;
    }

    public void setDateKey(LocalDate dateKey) {
        this.dateKey = dateKey;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    @Override
    public int compareTo(CustomerStatisticsDto o) {
        if (o == null) {
            return 0;
        }
        if (this.getDateKey().isBefore(o.getDateKey())) {
            return -1;
        } else if (this.getDateKey().equals(o.getDateKey())) {
            return 0;
        } else {
            return 1;
        }
    }
}
