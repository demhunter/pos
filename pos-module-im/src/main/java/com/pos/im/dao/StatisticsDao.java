/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * IM数据统计DAO
 *
 * @author cc
 * @version 1.0, 16/7/18
 */
@Repository
public interface StatisticsDao {

    /**
     * 批量接口——查询商家的聊天数
     *
     * @param companyIds 商家id列表
     * @return 查询结果
     */
    List<Map<String, Object>> queryCompanyChatCount(@Param("companyIds") List<Long> companyIds);

    /**
     * 批量接口——查询商家案例的聊天数
     *
     * @param caseIds   案例id列表
     * @param companyId 公司id
     * @return 查询结果
     */
    List<Map<String, Object>> queryCaseCurrentChatCount(
            @Param("caseIds") List<Long> caseIds, @Param("companyId") Long companyId);

    /**
     * 批量接口——查询商家案例的总联系数以及总聊天数
     *
     * @param caseIds   案例id列表
     * @param companyId 公司id
     * @return 查询结果
     */
    List<Map<String, Object>> queryCaseContactAndChatTotal(
            @Param("caseIds") List<Long> caseIds, @Param("companyId") Long companyId);


    /**
     * 批量接口——查询业者的总联系数以及总聊天数（以案例为维度）
     *
     * @param caseIds 案例id列表
     * @param userId  业者id
     * @return 查询结果
     */
    List<Map<String, Object>> queryEmployeeContactAndChatTotalByCase(
            @Param("caseIds") List<Long> caseIds, @Param("userId") Long userId);

    /**
     * 批量接口——查询用户的总联系数以及总聊天数
     *
     * @param userIds  用户id列表
     * @param userType 用户类型
     * @param companyId 会话所属的公司ID(可空)
     * @return 查询结果
     */
    List<Map<String, Object>> queryUserContactAndChatTotal(@Param("userIds") List<Long> userIds,
                                                           @Param("userType") String userType,
                                                           @Param("companyId") Long companyId);

    /**
     * 批量接口——查询用户的当前聊天数
     *
     * @param userIds   用户id列表
     * @param userType  用户类型
     * @param companyId 会话所属的公司ID(可空)
     * @return 查询结果
     */
    List<Map<String, Object>> queryUserCurrentChatCount(@Param("userIds") List<Long> userIds,
                                                        @Param("userType") String userType,
                                                        @Param("companyId") Long companyId);

    /**
     * 获取当前会话总数
     *
     * @param available 是否可用(可空)
     * @return 查询结果
     */
    int querySessionCount(@Param("available") Boolean available);
}
