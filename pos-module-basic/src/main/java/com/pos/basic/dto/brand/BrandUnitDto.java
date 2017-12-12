/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.brand;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * Description.
 *
 * @author wayne
 * @version 1.0, 2017/2/23
 */
@ApiModel
public class BrandUnitDto implements Serializable {

    private static final long serialVersionUID = 6304270747689134561L;
    @ApiModelProperty("品牌编号")
    private Long id;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌LOGO图片URL")
    private String logo;

    @ApiModelProperty("品牌官网")
    private String website;

    @ApiModelProperty("是否可用")
    private boolean available;

    @ApiModelProperty("品牌子类编号")
    private Integer subType;

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

}