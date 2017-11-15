/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.area;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 系统当前支持的城市信息
 *
 * @author wangbing
 * @version 1.0, 2017/6/30
 */
public class CitySupportDto implements Serializable {

    @ApiModelProperty("城市id")
    private Long id;

    @ApiModelProperty("名称（成都市）")
    private String name;

    @ApiModelProperty("短名称（成都）")
    private String shortName;

    public CitySupportDto(Long id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
