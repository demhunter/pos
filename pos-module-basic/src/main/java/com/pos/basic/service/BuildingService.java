/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.common.util.mvc.support.*;
import com.pos.basic.domain.Building;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.building.BuildingDto;

/**
 * 楼盘相关Service
 *
 * @author cc
 * @version 1.0, 16/8/29
 */
public interface BuildingService {

    /**
     * 查询Building
     *
     * @param buildingId 楼盘id
     * @return 查询结果
     */
    Building queryBuildingById(Long buildingId);

    /**
     * 查询楼盘地址
     *
     * @param buildingId 楼盘id
     * @return 地址
     */
    String queryBuildingAddress(Long buildingId, String replaceStr);

    /**
     * 查询楼盘区域信息
     *
     * @param buildingId 楼盘id
     * @param replaceStr
     * @return 区域名称
     */
    String queryBuildingArea(Long buildingId, String replaceStr);

    /**
     * 查询楼盘信息
     *
     * @param buildingId 楼盘id
     * @return 楼盘信息
     */
    BuildingDto queryBuilding(Long buildingId);

    /**
     * 查询有效的楼盘信息
     *
     * @param buildingId 楼盘id
     * @return 楼盘信息
     */
    BuildingDto queryAvailableBuilding(Long buildingId);

    /**
     * 新增或更新楼盘信息
     *
     * @param buildingDto 楼盘信息
     * @param user        用户标识
     * @return 楼盘id
     */
    ApiResult<Long> addOrUpdateBuilding(BuildingDto buildingDto, UserIdentifier user);

    /**
     * 删除楼盘信息
     *
     * @param buildingId 楼盘id
     * @param user       用户标识
     * @return
     */
    ApiResult<NullObject> deleteBuilding(Long buildingId, UserIdentifier user);

    /**
     * 查询楼盘列表
     *
     * @param searchKey   搜索关键字
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return 楼盘列表
     */
    ApiResult<Pagination<BuildingDto>> queryBuildings(String searchKey, LimitHelper limitHelper, OrderHelper orderHelper);
}
