/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 平台数据统计-业者类别统计元数据项DTO
 *
 * @author wangbing
 * @version 1.0, 2017/03/31
 */
@Deprecated
public class EmployeeTypeItemStatisticsDto implements Serializable {

    @ApiModelProperty("设计师数量")
    private int designerCount;

    @ApiModelProperty("项目经理数量")
    private int pmCount;

    @ApiModelProperty("商务代表数量")
    private int advisorCount;

    @ApiModelProperty("业者总数")
    public int getTotalCount() {
        return designerCount + pmCount + advisorCount;
    }

    public EmployeeTypeItemStatisticsDto() {
        designerCount = 0;
        pmCount = 0;
        advisorCount = 0;
    }

    public int getDesignerCount() {
        return designerCount;
    }

    public void setDesignerCount(int designerCount) {
        this.designerCount = designerCount;
    }

    public int getPmCount() {
        return pmCount;
    }

    public void setPmCount(int pmCount) {
        this.pmCount = pmCount;
    }

    public int getAdvisorCount() {
        return advisorCount;
    }

    public void setAdvisorCount(int advisorCount) {
        this.advisorCount = advisorCount;
    }
}
