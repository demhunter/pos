/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.pos.basic.condition.orderby.BuildingOrderField;
import com.pos.basic.converter.BuildingConverter;
import com.pos.basic.dao.BuildingDao;
import com.pos.basic.data.AreaCacheContainer;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.area.AreaDto;
import com.pos.basic.exception.BuildingErrorCode;
import com.pos.basic.service.BuildingLayoutService;
import com.pos.basic.service.BuildingService;
import com.pos.common.util.mvc.support.*;
import com.pos.basic.domain.Building;
import com.pos.basic.dto.building.BuildingDto;
import com.pos.common.util.validation.FieldChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BuildingServiceImpl
 *
 * @author cc
 * @version 1.0, 16/8/29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BuildingServiceImpl implements BuildingService {

    @Resource
    private BuildingDao buildingDao;

    @Resource
    private AreaCacheContainer areaCacheContainer;

    @Resource
    private BuildingLayoutService buildingLayoutService;

    @Override
    public Building queryBuildingById(Long buildingId) {
        return buildingDao.queryBuildingById(buildingId);
    }

    @Override
    public String queryBuildingAddress(Long buildingId, String replaceStr) {
        Building building = buildingDao.queryBuildingById(buildingId);

        AreaDto areaDto = areaCacheContainer.getLeaf(building.getAreaId());
        return areaDto.getNamePath().replaceAll("\\.", replaceStr) + building.getName();
    }

    @Override
    public String queryBuildingArea(Long buildingId, String replaceStr) {
        Building building = buildingDao.queryBuildingById(buildingId);

        AreaDto areaDto = areaCacheContainer.getLeaf(building.getAreaId());
        return areaDto.getNamePath().replaceAll("\\.", replaceStr);
    }

    @Override
    public BuildingDto queryBuilding(Long buildingId) {
        FieldChecker.checkEmpty(buildingId, "buildingId");

        Building building = buildingDao.queryBuildingById(buildingId);

        return building == null ? null : BuildingConverter.toBuildingDto(building);
    }

    @Override
    public BuildingDto queryAvailableBuilding(Long buildingId) {
        FieldChecker.checkEmpty(buildingId, "buildingId");

        Building building = buildingDao.queryBuildingById(buildingId);

        return (building != null && building.isAvailable()) ? BuildingConverter.toBuildingDto(building) : null;
    }

    @Override
    public ApiResult<Long> addOrUpdateBuilding(BuildingDto buildingDto, UserIdentifier user) {
        FieldChecker.checkEmpty(buildingDto, "buildingDto");
        FieldChecker.checkEmpty(user, "user");
        buildingDto.check("buildingDto");

        Building newBuilding = BuildingConverter.toBuilding(buildingDto);
        if (newBuilding.getId() == null) {
            // save
            Date now = new Date();
            newBuilding.setCreateUserId(user.getUserId());
            newBuilding.setCreateTime(now);
            newBuilding.setUpdateUserId(user.getUserId());
            newBuilding.setUpdateTime(now);
            buildingDao.saveBuilding(newBuilding);
        } else {
            // update
            Building originalBuilding = buildingDao.queryBuildingById(newBuilding.getId());
            if (originalBuilding == null || !originalBuilding.isAvailable()) {
                return ApiResult.fail(BuildingErrorCode.BUILDING_NOT_EXIST);
            }
            newBuilding.setAvailable(originalBuilding.isAvailable());
            newBuilding.setUpdateUserId(user.getUserId());
            newBuilding.setUpdateTime(new Date());
            buildingDao.updateBuilding(newBuilding);
        }

        return ApiResult.succ(newBuilding.getId());
    }

    @Override
    public ApiResult<NullObject> deleteBuilding(Long buildingId, UserIdentifier user) {
        FieldChecker.checkEmpty(buildingId, "buildingId");
        FieldChecker.checkEmpty(user, "user");

        Building building = buildingDao.queryBuildingById(buildingId);
        if (building == null || !building.isAvailable()) {
            return ApiResult.fail(BuildingErrorCode.BUILDING_NOT_EXIST);
        }
        // 逻辑删除楼盘信息
        building.setAvailable(Boolean.FALSE);
        building.setUpdateUserId(user.getUserId());
        building.setUpdateTime(new Date());
        buildingDao.updateBuilding(building);
        // 逻辑删除楼盘所属户型信息
        return buildingLayoutService.deleteBuildingLatoutByBuildingId(buildingId, user);
    }

    @Override
    public ApiResult<Pagination<BuildingDto>> queryBuildings(String searchKey, LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(limitHelper,"limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(BuildingOrderField.getInterface());
        }

        int totalCount = buildingDao.queryBuildingCount(searchKey);
        Pagination<BuildingDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<Building> buildings = buildingDao.queryBuildings(searchKey, limitHelper, orderHelper);
            pagination.setResult(buildings.stream().map(building ->
                    BuildingConverter.toBuildingDto(building)).distinct().collect(Collectors.toList()));
        }

        return ApiResult.succ(pagination);
    }
}
