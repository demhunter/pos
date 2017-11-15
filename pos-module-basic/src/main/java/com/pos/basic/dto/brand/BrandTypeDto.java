/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.brand;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌类型DTO.
 *
 * @author wayne
 * @version 1.0, 2016/8/29
 */
@ApiModel
public class BrandTypeDto implements Serializable {

    private static final long serialVersionUID = -6464246420298264212L;

    @ApiModelProperty("类型编号")
    private short id;

    @ApiModelProperty("类型名称")
    private String name;

    @ApiModelProperty("子类列表")
    private List<BrandTypeDto> subTypes;

    public BrandTypeDto(short id, String name) {
        this.id = id;
        this.name = name;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BrandTypeDto> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(List<BrandTypeDto> subTypes) {
        this.subTypes = subTypes;
    }

}