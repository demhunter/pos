/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.user.dto.statistics.CountDto;
import com.pos.user.dto.statistics.PlatformCustomerStatisticsDto;
import com.pos.user.dto.statistics.PlatformEmployeeStatisticsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/7/12
 */
@Repository
public interface PlatformStatisticsDao {

    void addPlatformEmployeeStatistics(@Param("pe") PlatformEmployeeStatisticsDto platformEmployeeStatisticsDto);

    void updatePlatformEmployeeStatistics(@Param("pe") PlatformEmployeeStatisticsDto platformEmployeeStatisticsDto);

    void addPlatformCustomerStatistics(@Param("pc") PlatformCustomerStatisticsDto platformCustomerStatisticsDto);

    void updatePlatformCustomerStatistics(@Param("pc") PlatformCustomerStatisticsDto platformCustomerStatisticsDto);

    PlatformEmployeeStatisticsDto getPlatformEmployeeStatisticsByUserId(@Param("userId") Long userId);

    PlatformCustomerStatisticsDto getPlatformCustomerStatisticsByUserId(@Param("userId") Long userId);

    List<CountDto> queryCustomerCount(@Param("userId") Long userId);

    List<CountDto> querySessionCount(@Param("userId") Long userId);

    List<CountDto> queryOrderCount(@Param("userId") Long userId);

    List<CountDto> queryCustomerOrderCount (@Param("userId") Long userId);

    int queryTwitterCount(@Param("userId") Long userId);

}
