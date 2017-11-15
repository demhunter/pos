/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.building;

import com.pos.basic.constant.DecorationStandard;
import com.pos.basic.dto.CommonEnumDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 楼盘相关枚举定义的DTO.
 *
 * @author wayne
 * @version 1.0, 2017/3/16
 */
public class BuildingEnumsDto implements Serializable {

    /**
     * {@link DecorationStandard}
     */
    @ApiModelProperty(value = "装修标准的类型定义")
    private List<CommonEnumDto> decorationStandard;

    private static final BuildingEnumsDto instance = new BuildingEnumsDto();

    private BuildingEnumsDto() {
        decorationStandard = Stream.of(DecorationStandard.values())
                .map(e -> new CommonEnumDto(e.getCode(), e.getDesc())).collect(Collectors.toList());
    }

    public static BuildingEnumsDto getInstance() {
        return instance;
    }

    public List<CommonEnumDto> getDecorationStandard() {
        return decorationStandard;
    }

}