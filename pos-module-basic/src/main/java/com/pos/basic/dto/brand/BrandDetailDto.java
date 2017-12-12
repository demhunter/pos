/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.brand;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌细节Dto
 *
 * @author cc
 * @version 1.0, 16/8/29
 */
public class BrandDetailDto implements Serializable {

    private static final long serialVersionUID = -6477370138361905079L;
    /**
     * 一级品牌名
     */
    @ApiModelProperty(value = "一级品牌名")
    private String name;

    /**
     * 二级品牌列表
     */
    @ApiModelProperty(value = "二级品牌列表")
    private List<SubBrandDetailDto> brands;

    public List<SubBrandDetailDto> getBrands() {
        return brands;
    }

    public void setBrands(List<SubBrandDetailDto> brands) {
        this.brands = brands;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
