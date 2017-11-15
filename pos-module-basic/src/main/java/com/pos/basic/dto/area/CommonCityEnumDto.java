/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.area;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * @author lifei
 * @version 1.0, 2017/8/1
 */
public class CommonCityEnumDto implements Serializable {

    @ApiModelProperty("编码（int）")
    private Long code;

    @ApiModelProperty("分类编码(int)")
    private Long categoryCode;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("v1.8.0 * 数量")
    private Integer count;

    public CommonCityEnumDto() {
    }

    public CommonCityEnumDto(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public CommonCityEnumDto(Long code, Long categoryCode, String desc) {
        this.code = code;
        this.categoryCode = categoryCode;
        this.desc = desc;
    }

    public CommonCityEnumDto(Long code, String desc, Integer count) {
        this.code = code;
        this.desc = desc;
        this.count = count;
    }

    public CommonCityEnumDto(Long code, Long categoryCode, String desc, Integer count) {
        this.code = code;
        this.categoryCode = categoryCode;
        this.desc = desc;
        this.count = count;
    }

    public Long getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Long categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
