/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.employee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 设计费用区间信息
 *
 * @author lifei
 * @version 1.0, 2017/8/7
 */
public class DesignPriceRangeDto implements Serializable {

    @ApiModelProperty("设计类型（int 1=硬装， 2=软装）")
    private Byte type;

    @ApiModelProperty("设计名")
    private String name;

    @ApiModelProperty("费用区间最低价")
    private Integer priceStart;

    @ApiModelProperty("费用区间最高价")
    private Integer priceEnd;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(Integer priceStart) {
        this.priceStart = priceStart;
    }

    public Integer getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(Integer priceEnd) {
        this.priceEnd = priceEnd;
    }
}
