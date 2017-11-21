/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.building;

import com.pos.basic.constant.DecorationStandard;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;
import java.util.List;

/**
 * BuildingDto
 *
 * @author cc
 * @version 1.0, 2017/2/21
 */
@ApiModel
public class BuildingDto implements Serializable {

    @ApiModelProperty("楼盘ID")
    private Long id;

    @ApiModelProperty("楼盘名称")
    private String name;

    @ApiModelProperty("地区ID（叶子节点）")
    private Long areaId;

    @ApiModelProperty("楼盘地址")
    private String address;

    @ApiModelProperty("开发商")
    private String developer;

    @ApiModelProperty("装修标准（int）：1 = 毛坯，2 = 精装")
    /** {@link DecorationStandard} */
    private Byte decoration;

    @ApiModelProperty("楼盘户型列表（可空，且在需要时才返回）")
    private List<BuildingLayoutDto> buildingLayouts;

    @ApiModelProperty("装修标准的值描述")
    public String getDecorationDesc() {
        DecorationStandard type = parseDecoration();
        return type != null ? type.getDesc() : null;
    }

    public DecorationStandard parseDecoration() {
        return decoration != null ? DecorationStandard.getEnum(decoration) : null;
    }

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

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Byte getDecoration() {
        return decoration;
    }

    public void setDecoration(Byte decoration) {
        this.decoration = decoration;
    }

    public List<BuildingLayoutDto> getBuildingLayouts() {
        return buildingLayouts;
    }

    public void setBuildingLayouts(List<BuildingLayoutDto> buildingLayouts) {
        this.buildingLayouts = buildingLayouts;
    }

    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(name, fieldPrefix + "name");
        FieldChecker.checkEmpty(areaId, fieldPrefix + "areaId");
    }

}
