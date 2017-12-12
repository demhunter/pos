/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * 枚举类型通用Dto
 *
 * @author cc
 * @version 1.0, 16/9/2
 */
public class CommonEnumDto implements Serializable {

    private static final long serialVersionUID = -6742491189670719708L;
    @ApiModelProperty("编码（int）")
    private Byte code;

    @ApiModelProperty("分类编码(int)")
    private Byte categoryCode;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("v1.3.4 * 数量")
    private Integer count;

    public CommonEnumDto() {
    }

    public CommonEnumDto(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public CommonEnumDto(Byte code, Byte categoryCode, String desc) {
        this.code = code;
        this.categoryCode = categoryCode;
        this.desc = desc;
    }

    public CommonEnumDto(Byte code, String desc, Integer count) {
        this.code = code;
        this.desc = desc;
        this.count = count;
    }

    public CommonEnumDto(Byte code, Byte categoryCode, String desc, Integer count) {
        this.code = code;
        this.categoryCode = categoryCode;
        this.desc = desc;
        this.count = count;
    }

    public Byte getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Byte categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
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
