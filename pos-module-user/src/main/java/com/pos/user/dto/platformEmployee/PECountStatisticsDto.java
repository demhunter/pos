/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 家居顾问数量统计
 *
 * @author wangbing
 * @version 1.0, 2017/7/7
 */
public class PECountStatisticsDto implements Serializable {

    @ApiModelProperty("接受派单的家居顾问数量")
    private Integer acceptDistribution;

    @ApiModelProperty("不接受派单的家居顾问数量")
    private Integer refuseDistribution;

    public Integer getAcceptDistribution() {
        return acceptDistribution;
    }

    public void setAcceptDistribution(Integer acceptDistribution) {
        this.acceptDistribution = acceptDistribution;
    }

    public Integer getRefuseDistribution() {
        return refuseDistribution;
    }

    public void setRefuseDistribution(Integer refuseDistribution) {
        this.refuseDistribution = refuseDistribution;
    }
}
