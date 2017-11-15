/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.dto.area.CitySupportDto;
import com.pos.basic.domain.Area;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 地区相关DAO.
 *
 * @author wayne
 * @version 1.0, 2016/6/15
 */
@Repository
public interface AreaDao {

    void saveBatchArea(@Param("list") List<Area> areaList);

    long getTotal();

    List<Area> findAreaListByLevel(@Param("level") byte level);

    Area getById(@Param("id") Long id);

    Area getCityInfo(@Param("cityName") String cityName);

    /**
     * 获取作品全部城市
     *
     * @param caseCityIds
     * @return
     */
    List<CitySupportDto> getCityList(@Param("caseCityIds") List<Long> caseCityIds);
}
