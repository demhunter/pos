/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.domain.BuildingLayout;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.building.BuildingLayoutDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 楼盘户型相关的DAO.
 *
 * @author wayne
 * @version 1.0, 2017/3/15
 */
@Repository
public interface BuildingLayoutDao {

    /**
     * 新增楼盘户型信息
     *
     * @param buildingLayout 户型信息
     */
    void saveBuildingLayout(@Param("layout") BuildingLayout buildingLayout);

    /**
     * 更新楼盘户型信息
     *
     * @param buildingLayout 户型信息
     */
    void updateBuildingLayout(@Param("layout") BuildingLayout buildingLayout);

    /**
     * 查询楼盘户型列表
     *
     * @param buildingId 楼盘id
     * @return 户型列表
     */
    List<BuildingLayoutDto> queryBuildingLayouts(@Param("buildingId") Long buildingId);

    /**
     * 查询指定的户型信息
     *
     * @param layoutId 户型id
     * @return 户型信息
     */
    BuildingLayoutDto getBuildingLayoutDto(@Param("layoutId") Long layoutId);

    /**
     * 查询指定的户型信息
     *
     * @param layoutId 户型id
     * @return 户型信息
     */
    BuildingLayout getBuildingLayout(@Param("layoutId") Long layoutId);

    /**
     * 删除楼盘所对应的户型信息
     *
     * @param buildingId 楼盘id
     * @param user       用户标识
     */
    void deleteBuildingLatoutByBuildingId(@Param("buildingId") Long buildingId, @Param("user") UserIdentifier user);
}
