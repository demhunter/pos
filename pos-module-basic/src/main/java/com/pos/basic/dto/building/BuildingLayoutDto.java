/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.building;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 楼盘户型DTO.
 *
 * @author wayne
 * @version 1.0, 2017/3/15
 */
@ApiModel
public class BuildingLayoutDto implements Serializable {

    @ApiModelProperty("户型ID")
    private Long id;

    @ApiModelProperty("户型名称")
    private String name;

    @ApiModelProperty("户型-室（int）")
    private Byte bedroom;

    @ApiModelProperty("户型-厅（int）")
    private Byte livingRoom;

    @ApiModelProperty("户型-卫（int）")
    private Byte toilet;

    @ApiModelProperty("户型面积（BigDecimal）")
    private BigDecimal houseArea;

    @ApiModelProperty("户型图")
    private String layoutImage;

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

    public Byte getBedroom() {
        return bedroom;
    }

    public void setBedroom(Byte bedroom) {
        this.bedroom = bedroom;
    }

    public Byte getLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(Byte livingRoom) {
        this.livingRoom = livingRoom;
    }

    public Byte getToilet() {
        return toilet;
    }

    public void setToilet(Byte toilet) {
        this.toilet = toilet;
    }

    public BigDecimal getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(BigDecimal houseArea) {
        this.houseArea = houseArea;
    }

    public String getLayoutImage() {
        return layoutImage;
    }

    public void setLayoutImage(String layoutImage) {
        this.layoutImage = layoutImage;
    }

    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(name, fieldPrefix + "name");
        FieldChecker.checkEmpty(houseArea, fieldPrefix + "houseArea");
    }

}