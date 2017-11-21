/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.user.condition.query.*;
import com.pos.user.dto.platformEmployee.*;
import com.pos.user.domain.PlatformEmployee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 家居顾问Dao
 *
 * @author wangbing
 * @version 1.0, 2017/7/6
 */
@Repository
public interface PlatformEmployeeDao {

    /**
     * 根据用户id查询平台家居顾问信息
     *
     * @param userId 用户id
     * @return 平台家居顾问信息
     */
    PlatformEmployeeDetailDto queryByUserId(@Param("userId") Long userId);

    /**
     * 更新家居顾问派单状态
     *
     * @param id           主键id
     * @param distribution 是否可接受派单
     */
    void updateDistribution(@Param("id") Long id, @Param("distribution") Boolean distribution);

    /**
     * 查询符合条件的家居顾问数量
     *
     * @param condition 查询条件
     * @return 符合条件的家居顾问数量
     */
    int queryCount(@Param("condition") PlatformEmployeeCondition condition);

    /**
     * 查询符合条件的家居顾问数量统计
     *
     * @param condition 查询条件
     * @return 符合条件的家居顾问数量统计
     */
    PECountStatisticsDto queryCountStatistics(@Param("condition") PlatformEmployeeCondition condition);

    /**
     * 查询符合条件的家居顾问列表
     *
     * @param condition   查询条件
     * @param limitHelper 分页参数
     * @return 家居顾问列表
     */
    List<PlatformEmployeeDetailDto> queryPlatformEmployees(
            @Param("condition") PlatformEmployeeCondition condition,
            @Param("limitHelper") LimitHelper limitHelper);


    /**
     * 修改家居顾问启用禁用
     * @param id
     * @param enable
     */
    void updateEnable(@Param("id") Long id,@Param("enable") Boolean enable);

    /**
     * 新增
     * @param platformEmployee
     */
    void addPlatformEmployee(@Param("platformEmployee") PlatformEmployee platformEmployee);

    /**
     * 修改
     * @param platformEmployee
     */
    void updatePlatformEmployee(@Param("platformEmployee") PlatformEmployee platformEmployee);

    /**
     * 查询某个区域的客服经理
     * @param areaId
     * @param userDetailType
     * @return
     */
    PlatformEmployeeDetailDto queryManagerByAreaId(
            @Param("areaId") Long areaId,
            @Param("userDetailType") byte userDetailType);

    PlatformEmployee queryById(@Param("id") Long id);

    PlatformEmployee queryPeUserId(@Param("peUserId") Long peUserId);

    List<PlatformEmployeeDetailDto> queryPEByAreaId(@Param("areaId") Long areaId, @Param("enable") boolean enable, @Param("id") Long id,@Param("distribution") Boolean distribution);


    List<RelationTwitterDto> getRelationTwitter(
            @Param("condition") RelationTwitterCondition relationTwitterCondition,
            @Param("limitHelper") LimitHelper limitHelper,
            @Param("orderHelper") OrderHelper orderHelper);

    int getRelationTwitterCount(@Param("condition") RelationTwitterCondition relationTwitterCondition);

    List<TwitterDto> queryTwitter(@Param("condition") TwitterCondition twitterCondition);

    List<TwitterChildDto> queryChildTwitter(@Param("condition") TwitterChildrenCondition twitterCondition);

    void updateTwitterRelation(@Param("peId") Long peId,
                               @Param("twitterIds") List<Long> twitterIds);

    List<CustomerListUserIdsDto> queryCustomerUserIds(
            @Param("condition") RelationCustomerCondition condition,
            @Param("limitHelper") LimitHelper limitHelper,
            @Param("orderHelpers") List<OrderHelper> orderHelpers);

    List<CustomerListDto> queryCustomer(
            @Param("condition") RelationCustomerCondition condition,
            @Param("limitHelper") LimitHelper limitHelper,
            @Param("orderHelpers") List<OrderHelper> orderHelpers);

    int queryCustomerCount(
            @Param("condition") RelationCustomerCondition condition);

    void updateCustomerRelation(@Param("peUserId") Long peUserId,@Param("customerIds") List<Long> customerIds, @Param("areaId") Long areaId);

    int querySessionCount(@Param("customerIds") List<Long> customerIds,@Param("newPeId") Long newPeId);

    List<Long> queryCustomerSession(@Param("customerIds") List<Long> customerIds, @Param("peUserId") Long peUserId);

    void updateImSessionMember(@Param("peId") Long peId,@Param("customerIds") List<Long> customerIds,@Param("oldPeUserId") Long oldPeUserId);

    List<Long> querySessionByUserId(@Param("customerIds") List<Long> customerIds);

    void updateImSessionPlatformEmployee(@Param("peId") Long peId,@Param("sessionIds") List<Long> sessionIds, @Param("oldPeUserId") Long oldPeUserId);

    void updateOrderPlatformEmployee(@Param("peId") Long peUserId, @Param("orderIds") List<Long> orderIds);

    List<Long> queryNeedUpdateOrderIds(@Param("customerIds") List<Long> customerIds, @Param("oldPeUserId") Long oldPeUserId);

    void updateOrderEmployeeRelation(@Param("peUserId") Long peUserId,@Param("employeeType") Byte employeeType,@Param("orderIds") List<Long> orderIds);

    List<SessionListDto> querySessionList(@Param("condition") SessionListCondition condition,@Param("limitHelper") LimitHelper limitHelper,@Param("orderHelper") OrderHelper orderHelper);

    int querySessionListCount(@Param("condition") SessionListCondition condition);

    List<OrderListDto> queryOrderList(@Param("condition") OrderListCondition condition, @Param("limitHelper") LimitHelper limitHelper, @Param("orderHelper") OrderHelper orderHelper);

    int queryOrderListCount(@Param("condition") OrderListCondition condition);

    PlatformEmployeeAllInfoDto queryPlatformEmployeeAllInfo(@Param("peId") Long peId);

    List<PlatEmployeeListDto> queryPEList(@Param("condition") PEListCondition condition, @Param("limitHelper") LimitHelper limitHelper, @Param("orderHelper") OrderHelper orderHelper);

    List<PlatEmployeeListUserIdsDto> queryPEListUserIds(@Param("condition") PEListCondition condition, @Param("limitHelper") LimitHelper limitHelper, @Param("orderHelper") OrderHelper orderHelper);

    int queryPEListCount(@Param("condition") PEListCondition condition );

    /**
     * 查询公司信息
     *
     * @param companyId 公司id
     * @return 公司信息
     */
    CompanyInfoDto queryCompanyInfo(@Param("companyId") Long companyId);

    /**
     * 查询监理公司信息
     *
     * @param companyId 公司id
     * @return 公司信息
     */
    CompanyInfoDto querySupervisionCompanyInfo(@Param("companyId") Long companyId);

    /**
     * 获取未关闭会话数最少的家居顾问
     *
     * @param userIds 家居顾问id列表
     * @return 家居顾问信息
     */
    PlatformEmployeeDetailDto getIMAvailableCountMinPE(@Param("userIds") List<Long> userIds);

    /**
     * 获取未结束的订单数最少的家居顾问
     *
     * @param userIds 家居顾问id列表
     * @return 家居顾问信息
     */
    PlatformEmployeeDetailDto getOrderCountMinPE(@Param("userIds") List<Long> userIds);

    /**
     * 获取客户数最少的家居顾问
     *
     * @param userIds 家居顾问id列表
     * @return 家居顾问信息
     */
    PlatformEmployeeDetailDto getCustomerCountMinPE(@Param("userIds") List<Long> userIds);

    /**
     * 查询家居顾问简要信息
     * @param userId
     * @return
     */
    List<PlatformEmployeeInfoDto> getSimpleInfo(@Param("userId") Long userId);
}
