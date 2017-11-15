/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.im.condition.query.SessionCommonCondition;
import com.pos.im.condition.query.SessionConsoleCondition;
import com.pos.im.dto.session.SessionConsoleListDto;
import com.pos.im.dto.session.SessionDto;
import com.pos.im.domain.Session;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * IM会话DAO.
 *
 * @author wayne
 * @version 1.0, 2016/11/23
 */
@Repository
public interface SessionDao {

    Session get(@Param("id") Long id);

    Session getByGroupId(@Param("groupId") String groupId);

    void save(@Param("se") Session session);

    void update(@Param("se") Session session);

    /**
     * 以原子方式将指定会话的呼叫次数+1.
     *
     * @param id 会话ID
     * @return 更新的行数
     */
    int incrementCallTotal(@Param("id") Long id);

    /**
     * 查询指定用户已加入的会话总数.
     *
     * @param userId   用户ID
     * @param userType 用户类型
     * @return
     */
    int getTotalByUserJoined(@Param("userId") Long userId, @Param("userType") String userType);

    /**
     * 查询指定一组同类型的用户已加入的会话总数.
     *
     * @param userIdList 用户ID
     * @param userType   用户类型
     * @return key: userId, sessionTotal
     */
    List<Map<String, Object>> getTotalByMultiUserJoined(
            @Param("userIdList") List<Long> userIdList, @Param("userType") String userType);

    /**
     * 查询指定ID的会话信息.
     *
     * @param id        会话ID
     * @param available true表示只查询正在进行的会话, false表示只查询已经关闭的会话, null表示查询所有会话
     * @param warning   true表示只查询有风险的会话, false表示只查询无风险的会话, null表示查询所有会话
     * @return
     */
    SessionDto find(@Param("id") Long id,
                    @Param("available") Boolean available, @Param("warning") Boolean warning);

    /**
     * 查询指定groupId的会话信息.
     *
     * @param groupId        会话ID
     * @param available true表示只查询正在进行的会话, false表示只查询已经关闭的会话, null表示查询所有会话
     * @param warning   true表示只查询有风险的会话, false表示只查询无风险的会话, null表示查询所有会话
     * @return 会话信息
     */
    SessionDto findByGroupId(@Param("groupId") String groupId,
                    @Param("available") Boolean available, @Param("warning") Boolean warning);

    /**
     * 统计已关闭的会话总数.
     *
     * @param companyId 会话所属的公司ID(可空)
     * @param reason    关闭原因(可空)
     * @param warning   true表示只查询有风险的会话, false表示只查询无风险的会话, null表示查询所有会话
     * @return
     */
    int getClosedTotal(@Param("companyId") Long companyId,
                       @Param("reason") Byte reason, @Param("warning") Boolean warning);

    /**
     * 查询已关闭的会话列表.
     *
     * @param companyId   会话所属的公司ID(可空)
     * @param reason      关闭原因(可空)
     * @param warning     true表示只查询有风险的会话, false表示只查询无风险的会话, null表示查询所有会话
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return
     */
    List<SessionDto> findClosedList(@Param("companyId") Long companyId,
                                    @Param("reason") Byte reason,
                                    @Param("warning") Boolean warning,
                                    @Param("limitHelper") LimitHelper limitHelper,
                                    @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 统计指定符合条件的会话总数.
     *
     * @param condition 公共查询条件
     * @return 会话总数.
     */
    int getSessionCount(@Param("condition") SessionCommonCondition condition);

    /**
     * 查询指定符合条件的会话列表.
     *
     * @param condition   公共查询条件
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return
     */
    List<SessionDto> querySessions(
            @Param("condition") SessionCommonCondition condition,
            @Param("limitHelper") LimitHelper limitHelper,
            @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 统计指定用户发起的会话总数.
     *
     * @param user      用户标识
     * @param companyId 会话所属的公司ID(可空)
     * @param condition 公共查询条件
     * @return
     */
    int getTotalByUser(@Param("user") UserIdentifier user,
                       @Param("companyId") Long companyId,
                       @Param("condition") SessionCommonCondition condition);

    /**
     * 查询指定用户发起的会话列表.
     *
     * @param user        用户标识
     * @param companyId   会话所属的公司ID(可空)
     * @param condition   公共查询条件
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return
     */
    List<SessionDto> findListByUser(@Param("user") UserIdentifier user,
                                    @Param("companyId") Long companyId,
                                    @Param("condition") SessionCommonCondition condition,
                                    @Param("limitHelper") LimitHelper limitHelper,
                                    @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 统计指定成员参与的会话总数.
     *
     * @param user         用户标识
     * @param userJoinType 成员加入类型(可空)
     * @param condition    公共查询条件
     * @return
     */
    int getTotalByMember(@Param("user") UserIdentifier user,
                         @Param("userJoinType") Byte userJoinType,
                         @Param("condition") SessionCommonCondition condition);

    /**
     * 查询指定成员参与的会话列表.
     *
     * @param user         用户标识
     * @param userJoinType 成员加入类型(可空)
     * @param condition    公共查询条件
     * @param limitHelper  分页参数
     * @param orderHelper  排序参数
     * @return
     */
    List<SessionDto> findListByMember(@Param("user") UserIdentifier user,
                                      @Param("userJoinType") Byte userJoinType,
                                      @Param("condition") SessionCommonCondition condition,
                                      @Param("limitHelper") LimitHelper limitHelper,
                                      @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 统计没有会话跟踪者(客服参与)的会话总数.
     *
     * @param warning true表示只查询有风险的会话, false表示只查询无风险的会话, null表示查询所有会话
     * @return
     */
    int getTotalByNotServant(@Param("warning") Boolean warning);

    /**
     * 查询没有会话跟踪者(客服参与)的会话列表.
     *
     * @param warning     true表示只查询有风险的会话, false表示只查询无风险的会话, null表示查询所有会话
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return
     */
    List<SessionDto> findListNotServant(@Param("warning") Boolean warning,
                                         @Param("limitHelper") LimitHelper limitHelper,
                                         @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 查询临时会话的ID.
     * <p>
     * 会话创建于4小时前, 24小时之内, 且没有产生过有效消息(即不包括系统推送消息)的视为临时会话.
     *
     * @return
     */
    List<Long> findTempSessionIds();

    /**
     * 查询创建于x天之前(包含x天)且最近x天之内没有产生过消息的会话ID.
     *
     * @param day 指定天数
     * @return
     */
    List<Long> findInactiveSessionIds(@Param("day") int day);


    int findListConsoleCount(@Param("condition") SessionConsoleCondition condition);

    List<SessionConsoleListDto> findListConsole(@Param("condition") SessionConsoleCondition condition, @Param("limitHelper") LimitHelper limitHelper,
                                                @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 获取会话信息
     *
     * @param sessionId
     * @return
     */
    SessionConsoleListDto querySessionInfo(@Param("sessionId") Long sessionId);
}
