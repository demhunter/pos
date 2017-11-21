/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.basic.condition.query.PlatformStatisticsCondition;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.user.domain.UserClass;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.statistics.EmployeeInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台数据统计-用户统计DAO
 *
 * @author wangbing
 * @version 1.0, 2017/03/31
 */
public interface UserPlatformStatisticsDao {

    /**
     * 查询C端用户数据统计
     *
     * @param condition 查询条件
     * @return
     */
    List<CustomerDto> queryCustomerStatistics(@Param("condition") PlatformStatisticsCondition condition);

    /**
     * 查询用户分类统计信息
     *
     * @param condition 查询条件
     * @param userType  用户类型
     * @return
     */
    List<UserClass> queryUserClassSatistics(
            @Param("condition") PlatformStatisticsCondition condition,
            @Param("userType") String userType);

    /**
     * 查询业者信息
     *
     * @param loginedHistoryType 登录历史类型(1 = 登录过，2 = 从未登录过，3 = 最近(7天)未登录过)
     * @param hasCase            是否关联作品(true = 已关联，false = 不限)
     * @param employeeType       业者类型(1 = 主创设计，2 = 项目经理， 3 = 商务代表)
     * @param cityId             城市id
     * @param limitHelper        分页参数
     * @return
     */
    List<EmployeeInfoDto> queryEmployeeInfo(
            @Param("loginedHistoryType") Byte loginedHistoryType,
            @Param("hasCase") Boolean hasCase,
            @Param("employeeType") Byte employeeType,
            @Param("cityId") Long cityId,
            @Param("limitHelper") LimitHelper limitHelper);
}
