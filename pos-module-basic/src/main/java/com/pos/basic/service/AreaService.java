/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.basic.dto.area.AreaDto;
import com.pos.basic.dto.area.CitySupportDto;
import com.pos.common.util.mvc.support.ApiResult;

import java.util.List;

/**
 * 地区信息服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/6/15
 */
public interface AreaService {

    /**
     * 获取所有一级地区列表(省/直辖市).
     */
    ApiResult<List<AreaDto>> getProvinces();

    /**
     * 获取指定省/直辖市下属的所有二级地区列表(市/区).
     *
     * @param provinceId 一级地区ID(省/直辖市)
     * @param parent     是否包含父结点的信息, 当不需要获取/已知父结点的信息时, 可以选择减少传输数据量
     */
    ApiResult<List<AreaDto>> getCitiesByProvinceId(Long provinceId, boolean parent);

    /**
     * 获取指定市/区下属的所有三级地区列表(县/区等等).
     *
     * @param cityId 二级地区ID(市/区)
     * @param parent 是否包含父结点的信息, 当不需要获取/已知父结点的信息时, 可以选择减少传输数据量
     */
    ApiResult<List<AreaDto>> getCountiesByCityId(Long cityId, boolean parent);

    /**
     * 获取指定叶子节点的地区信息.
     *
     * @param areaId 地区ID
     * @return 克隆对象，如果没有找到或者该地区不是叶子节点都返回null
     */
    ApiResult<AreaDto> getLeaf(Long areaId);

    /**
     * 获取指定叶子节点的地区信息, 如果地区只有2级, 则自动设置其根节点, 如北京只有2级, 该方法将返回: 北京-北京-昌平区.
     *
     * @param areaId 地区ID
     * @return 克隆对象，如果没有找到或者该地区不是叶子节点都返回null
     */
    ApiResult<AreaDto> getLeafAndDefaultRoot(Long areaId);

    /**
     * 获取地点名
     *
     * @param id 主键id
     */
    String getNameById(Long id);

    /**
     * 产生三级区域json串
     *
     * @return 区域json串
     */
    String generateAreaJson();

    /**
     * 获取当前系统支持的城市列表，当前系统支持成都(2294)和重庆(2252)
     *
     * @return 城市列表
     */
    List<CitySupportDto> getCitySupport();

    /**
     * 获取作品城市列表
     * @param caseCityIds
     * @return
     */
    List<CitySupportDto> getCityList(List<Long> caseCityIds);

    /**
     * 判断地区是否是直辖市
     *
     * @param areaId
     * @return
     */
    Boolean IsCharteredCities(Long areaId);
}
