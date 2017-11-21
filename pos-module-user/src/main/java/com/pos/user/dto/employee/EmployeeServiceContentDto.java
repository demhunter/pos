/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.employee;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * EmployeeServiceContentDto
 *
 * @author cc
 * @version 1.0, 2017/2/19
 */
public class EmployeeServiceContentDto implements Serializable {

    /**
     * 是否开启硬装设计服务
     */
    @ApiModelProperty("是否开启硬装设计服务")
    private Boolean hardSelected;

    /**
     * 硬装设计服务价格区间开始
     */
    @ApiModelProperty("硬装设计服务价格区间开始")
    private Integer hardStart;

    /**
     * 硬装设计服务价格区间结束
     */
    @ApiModelProperty("硬装设计服务价格区间结束")
    private Integer hardEnd;

    /**
     * 是否开启软装设计服务
     */
    @ApiModelProperty("是否开启软装设计服务")
    private Boolean softSelected;

    /**
     * 软装设计服务价格区间开始
     */
    @ApiModelProperty("软装设计服务价格区间开始")
    private Integer softStart;

    /**
     * 软装设计服务价格区间结束
     */
    @ApiModelProperty("软装设计服务价格区间结束")
    private Integer softEnd;

    public Integer getHardEnd() {
        return hardEnd;
    }

    public void setHardEnd(Integer hardEnd) {
        this.hardEnd = hardEnd;
    }

    public Integer getHardStart() {
        return hardStart;
    }

    public void setHardStart(Integer hardStart) {
        this.hardStart = hardStart;
    }

    public Integer getSoftEnd() {
        return softEnd;
    }

    public void setSoftEnd(Integer softEnd) {
        this.softEnd = softEnd;
    }

    public Integer getSoftStart() {
        return softStart;
    }

    public void setSoftStart(Integer softStart) {
        this.softStart = softStart;
    }

    public Boolean getHardSelected() {
        return hardSelected;
    }

    public void setHardSelected(Boolean hardSelected) {
        this.hardSelected = hardSelected;
    }

    public Boolean getSoftSelected() {
        return softSelected;
    }

    public void setSoftSelected(Boolean softSelected) {
        this.softSelected = softSelected;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
