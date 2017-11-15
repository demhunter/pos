/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.building.BuildingLayoutDto;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;

import java.util.List;

/**
 * 楼盘户型相关的服务接口.
 *
 * @author wayne
 * @version 1.0, 2017/3/15
 */
public interface BuildingLayoutService {

    /**
     * 查询楼盘的户型列表
     *
     * @param buildingId 楼盘id
     * @return 户型列表
     */
    List<BuildingLayoutDto> queryBuildingLayouts(Long buildingId);

    /**
     * 新增或更新楼盘的户型信息
     *
     * @param buildingId 楼盘id
     * @param layoutDto  户型信息
     * @param user       用户标识
     * @return
     */
    ApiResult<NullObject> addOrUpdateBuildingLayout(Long buildingId, BuildingLayoutDto layoutDto, UserIdentifier user);

    /**
     * 查询指定的户型信息
     *
     * @param layoutId 户型id
     * @return 户型信息
     */
    BuildingLayoutDto getBuildingLayout(Long layoutId);

    /**
     * 删除指定的户型ID
     *
     * @param buildingId 楼盘id
     * @param layoutId   户型id
     * @param user       用户标识
     * @return
     */
    ApiResult<NullObject> deleteBuildingLayout(Long buildingId, Long layoutId, UserIdentifier user);

    /**
     * 删除楼盘的所有户型信息
     *
     * @param buildingId 楼盘iD
     * @param user       用户标识
     * @return
     */
    ApiResult<NullObject> deleteBuildingLatoutByBuildingId(Long buildingId, UserIdentifier user);
}
