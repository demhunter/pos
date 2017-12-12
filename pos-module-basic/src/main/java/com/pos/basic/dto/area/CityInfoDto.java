/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.area;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 城市信息
 *
 * @author lifei
 * @version 1.0, 2017/8/2
 */
public class CityInfoDto implements Serializable {

    private static final long serialVersionUID = -5674783859967596075L;
    @ApiModelProperty("城市id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    public CityInfoDto(){
        this.id = 0L;
        this.name = "全部";
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
}
