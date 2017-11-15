/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.brand;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * 品牌（三级）Dto
 *
 * @author cc
 * @version 1.0, 16/8/29
 */
public class BrandSimpleDto implements Serializable {

    /**
     * 品牌logo（url）
     */
    @ApiModelProperty(value = "品牌logo（url）")
    private String logo;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String name;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
