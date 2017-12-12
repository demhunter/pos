/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.brand;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * 简单的品牌Dto，只存储id，用于序列化冗余字段
 *
 * @author cc
 * @version 1.0, 16/9/7
 */
public class BrandIdsDto implements Serializable {

    private static final long serialVersionUID = 7363433470432271766L;
    /**
     * 品牌编号
     */
    @ApiModelProperty("品牌编号")
    private Long id;

    /**
     * 品牌大类编号(int)
     */
    @ApiModelProperty("品牌大类编号(int)")
    private Byte type;

    /**
     * 品牌子类编号
     */
    @ApiModelProperty("品牌子类编号")
    private Short subType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getSubType() {
        return subType;
    }

    public void setSubType(Short subType) {
        this.subType = subType;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
