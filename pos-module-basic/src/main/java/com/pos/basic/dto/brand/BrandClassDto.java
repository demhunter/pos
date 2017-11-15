/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.brand;

import com.pos.basic.constant.BrandType;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.exception.ValidationException;

import java.io.Serializable;

/**
 * 品牌关联的分类DTO.
 *
 * @author wayne
 * @version 1.0, 2016/9/28
 */
@ApiModel
public class BrandClassDto implements Serializable {

    private static final long serialVersionUID = -3056042276696359822L;

    @ApiModelProperty("关联ID")
    private Long id;

    @ApiModelProperty("品牌编号")
    private Long brandId;

    @ApiModelProperty("品牌大类编号")
    private Integer type;

    @ApiModelProperty("品牌子类编号")
    private Integer subType;

    @ApiModelProperty("品牌子类名称")
    private String subTypeName;

    @ApiModelProperty("品牌大类名称")
    private String typeName;

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        if (typeName != null) {
            return typeName;
        } else {
            return type != null ? BrandType.getTypeName(type.shortValue()) : null;
        }
    }

    public String getSubTypeName() {
        if (subTypeName != null) {
            return subTypeName;
        } else {
            return type != null && subType != null ? BrandType.getSubTypeName(type.shortValue(), subType.shortValue()) : null;
        }
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    /**
     * 检查字段完整性.
     *
     * @param fieldPrefix 抛出异常时提示错误字段的前缀名, 可以为空
     * @throws ValidationException 未设置不能为空的字段, 或者字段值不符合约定
     */
    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        if (getTypeName() == null) {
            throw new ValidationException("'" + fieldPrefix + "type'无效值: " + type);
        }
        if (getSubTypeName() == null) {
            throw new ValidationException("'" + fieldPrefix + "subType'无效值: " + subType);
        }
    }

    public BrandClassDto() {
    }

    public BrandClassDto(Integer type, Integer subType) {
        this.type = type;
        this.subType = subType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

}