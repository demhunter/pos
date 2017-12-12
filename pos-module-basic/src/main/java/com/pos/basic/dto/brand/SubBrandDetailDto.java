/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.brand;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 二级品牌Dto
 *
 * @author cc
 * @version 1.0, 16/8/29
 */
public class SubBrandDetailDto implements Serializable {

    private static final long serialVersionUID = -3483373040991536907L;
    /**
     * 二级品牌名
     */
    @ApiModelProperty(value = "二级品牌名")
    private String name;

    /**
     * 二级品牌列表
     */
    @ApiModelProperty(value = "二级品牌列表")
    List<BrandSimpleDto> brands;

    public List<BrandSimpleDto> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandSimpleDto> brands) {
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
