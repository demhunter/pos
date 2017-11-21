/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.converter;

import com.pos.basic.dto.building.BuildingDto;
import com.pos.basic.domain.Building;
import com.pos.basic.domain.BuildingLayout;
import com.pos.basic.dto.building.BuildingLayoutDto;
import org.springframework.beans.BeanUtils;

/**
 * 楼盘Converter
 *
 * @author wangbing
 * @version 1.0, 2017/03/17
 */
public class BuildingConverter {

    public static BuildingDto toBuildingDto(Building building) {
        BuildingDto buildingDto = new BuildingDto();

        BeanUtils.copyProperties(building, buildingDto);

        return buildingDto;
    }

    public static Building toBuilding(BuildingDto buildingDto) {
        Building building = new Building();

        BeanUtils.copyProperties(buildingDto, building);

        return building;
    }

    public static BuildingLayout toBuildingLayout(BuildingLayoutDto buildingLayoutDto) {
        BuildingLayout buildingLayout = new BuildingLayout();

        BeanUtils.copyProperties(buildingLayoutDto, buildingLayout);

        return buildingLayout;
    }
}
