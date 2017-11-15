/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service;

import com.pos.im.data.IMCompanyStatisticsData;

import java.util.List;
import java.util.Map;

/**
 * IM数据统计服务
 *
 * @author cc
 * @version 1.0, 16/7/18
 */
public interface IMStatisticsService {

    /**
     * 批量接口——查询商家的聊天数
     *
     * @param companyIds 商家id列表
     * @return 查询结果
     */
    List<Map<String, Object>> queryCompanyChatCount(List<Long> companyIds);

    /**
     * 批量接口——查询商家案例的当前聊天数
     *
     * @param caseIds 案例id列表
     * @param companyId 公司id
     * @return 查询结果
     */
    List<Map<String, Object>> queryCaseCurrentChatCount(List<Long> caseIds, Long companyId);

    /**
     * 批量接口——查询商家案例的总联系数以及总聊天数
     *
     * @param caseIds 案例id列表
     * @return 查询结果
     */
    List<Map<String, Object>> queryCaseContactAndChatTotal(List<Long> caseIds, Long companyId);

    /**
     * 批量接口——查询业者的总联系数以及总聊天数（以案例为维度）
     *
     * @param caseIds 案例id列表
     * @param userId  用户id
     * @return 查询结果
     */
    List<Map<String, Object>> queryEmployeeContactAndChatTotalByCase(List<Long> caseIds, Long userId);

    /**
     * 批量接口——查询用户的总联系数以及总聊天数
     *
     * @param userIds  用户id列表
     * @param userType 用户类型
     * @param companyId 会话所属的公司ID(可空)
     * @return 查询结果
     */
    List<Map<String, Object>> queryUserContactAndChatTotal(List<Long> userIds, String userType, Long companyId);

    /**
     * 批量接口——查询用户的当前聊天数
     *
     * @param userIds  用户id列表
     * @param userType 用户类型
     * @param companyId 会话所属的公司ID(可空)
     * @return 查询结果
     */
    List<Map<String, Object>> queryUserCurrentChatCount(List<Long> userIds, String userType, Long companyId);

    /**
     * 增加公司当天的会话数（以及总会话数）
     *
     * @param companyIds 公司ids
     */
    void incrementCompaniesIMSession(List<Long> companyIds);

    /**
     * 查询公司IM相关统计数据
     *
     * @param companyId 公司id
     * @return 统计数据
     */
    IMCompanyStatisticsData queryIMCompanyStatisticsData(Long companyId);

    /**
     * 获取当前会话总数
     *
     * @param available 是否可用(可空)
     * @return 查询结果
     */
    int querySessionCount(Boolean available);
}
