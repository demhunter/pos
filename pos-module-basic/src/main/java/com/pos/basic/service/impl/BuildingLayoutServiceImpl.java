/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.pos.basic.converter.BuildingConverter;
import com.pos.basic.dao.BuildingLayoutDao;
import com.pos.basic.domain.BuildingLayout;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.building.BuildingLayoutDto;
import com.pos.basic.exception.BuildingErrorCode;
import com.pos.basic.service.BuildingLayoutService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description.
 *
 * @author wayne
 * @version 1.0, 2017/3/15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BuildingLayoutServiceImpl implements BuildingLayoutService {

    @Resource
    private BuildingLayoutDao buildingLayoutDao;

    @Override
    public List<BuildingLayoutDto> queryBuildingLayouts(Long buildingId) {
        FieldChecker.checkEmpty(buildingId, "buildingId");

        return buildingLayoutDao.queryBuildingLayouts(buildingId);
    }

    @Override
    public ApiResult<NullObject> addOrUpdateBuildingLayout(Long buildingId, BuildingLayoutDto layoutDto, UserIdentifier user) {
        FieldChecker.checkEmpty(buildingId, "buildingId");
        FieldChecker.checkEmpty(layoutDto, "buildingLayout");
        FieldChecker.checkEmpty(user, "user");
        layoutDto.check("layoutDto");

        BuildingLayout newLayout = BuildingConverter.toBuildingLayout(layoutDto);
        if (newLayout.getId() == null) {
            // save
            Date now = new Date();
            newLayout.setBuildingId(buildingId);
            newLayout.setCreateUserId(user.getUserId());
            newLayout.setCreateTime(now);
            newLayout.setUpdateUserId(user.getUserId());
            newLayout.setUpdateTime(now);
            buildingLayoutDao.saveBuildingLayout(newLayout);
        } else {
            // update
            BuildingLayout originalLayout = buildingLayoutDao.getBuildingLayout(newLayout.getId());
            if (originalLayout == null || !originalLayout.getAvailable()) {
                return ApiResult.fail(BuildingErrorCode.BUILDING_LAYOUT_NOT_EXIST);
            }
            newLayout.setAvailable(originalLayout.getAvailable());
            newLayout.setUpdateUserId(user.getUserId());
            newLayout.setUpdateTime(new Date());
            buildingLayoutDao.updateBuildingLayout(newLayout);
        }

        return ApiResult.succ();
    }

    @Override
    public BuildingLayoutDto getBuildingLayout(Long layoutId) {
        FieldChecker.checkEmpty(layoutId, "layoutId");

        return buildingLayoutDao.getBuildingLayoutDto(layoutId);
    }

    @Override
    public ApiResult<NullObject> deleteBuildingLayout(Long buildingId, Long layoutId, UserIdentifier user) {
        FieldChecker.checkEmpty(buildingId, "buildingId");
        FieldChecker.checkEmpty(layoutId, "layoutId");
        FieldChecker.checkEmpty(user, "user");

        BuildingLayout buildingLayout = buildingLayoutDao.getBuildingLayout(layoutId);
        if (buildingLayout == null || (!buildingLayout.getAvailable() && buildingLayout.getBuildingId().equals(buildingId))) {
            return ApiResult.fail(BuildingErrorCode.BUILDING_LAYOUT_NOT_EXIST);
        }
        // delete，逻辑删除
        buildingLayout.setAvailable(Boolean.FALSE);
        buildingLayout.setUpdateUserId(user.getUserId());
        buildingLayout.setUpdateTime(new Date());
        buildingLayoutDao.updateBuildingLayout(buildingLayout);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> deleteBuildingLatoutByBuildingId(Long buildingId, UserIdentifier user) {
        FieldChecker.checkEmpty(buildingId, "buildingId");
        FieldChecker.checkEmpty(user, "user");

        buildingLayoutDao.deleteBuildingLatoutByBuildingId(buildingId, user);

        return ApiResult.succ();
    }
}