/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 家居顾问客户统计DTO
 *
 * @author wangbing
 * @version 1.0, 2017/7/7
 */
public class PECustomerStatisticsDto implements Serializable {

    @ApiModelProperty("家居顾问id")
    private Long peUserId;

    @ApiModelProperty("谈单中客户数")
    private Integer talkingCount;

    @ApiModelProperty("已成单客户数")
    private Integer completeCount;

    @ApiModelProperty("已飞单客户数")
    private Integer refuseCount;

    @ApiModelProperty("v1.10.0 已完结客户数")
    private Integer finishCount;

    @ApiModelProperty("客户总量")
    public Integer getCustomerCount() {
        return talkingCount + completeCount + refuseCount + finishCount;
    }

    public Integer getTalkingCount() {
        return talkingCount;
    }

    public void setTalkingCount(Integer talkingCount) {
        this.talkingCount = talkingCount;
    }

    public Integer getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(Integer completeCount) {
        this.completeCount = completeCount;
    }

    public Integer getRefuseCount() {
        return refuseCount;
    }

    public void setRefuseCount(Integer refuseCount) {
        this.refuseCount = refuseCount;
    }

    public Long getPeUserId() {
        return peUserId;
    }

    public void setPeUserId(Long peUserId) {
        this.peUserId = peUserId;
    }

    public Integer getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(Integer finishCount) {
        this.finishCount = finishCount;
    }
}
