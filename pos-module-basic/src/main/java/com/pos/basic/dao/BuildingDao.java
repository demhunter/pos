/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.domain.Building;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 楼盘相关DAO
 * <p>
 * Created by cc on 16/6/19.
 */
@Repository
public interface BuildingDao {

    /**
     * 查询Building
     *
     * @param buildingId 楼盘id
     * @return 查询结果
     */
    Building queryBuildingById(@Param("buildingId") Long buildingId);

    /**
     * 新增楼盘
     *
     * @param building 楼盘信息
     */
    void saveBuilding(@Param("building") Building building);

    /**
     * 更新楼盘信息
     *
     * @param building 楼盘信息
     */
    void updateBuilding(@Param("building") Building building);

    /**
     * 查询楼盘数量
     *
     * @param queryKey 搜索关键字
     * @return 楼盘数量
     */
    int queryBuildingCount(@Param("searchKey") String queryKey);

    /**
     * 查询楼盘列表
     *
     * @param queryKey    查询关键字
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return 楼盘列表
     */
    List<Building> queryBuildings(
            @Param("searchKey") String queryKey,
            @Param("limitHelper") LimitHelper limitHelper,
            @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 查询楼盘是否存在
     *
     * @param areaId       区域id
     * @param buildingName 楼盘名称
     * @return 查询结果
     */
    List<Long> queryBuildingExistence(@Param("areaId") Long areaId, @Param("buildingName") String buildingName);

    /**
     * 首字匹配模糊查询楼盘列表
     *
     * @param buildingKey 楼盘名
     * @return 查询结果
     */
    List<Map<String, Object>> queryBuildingsByKey(
            @Param("buildingKey") String buildingKey,
            @Param("areaId") Long areaId);
}
