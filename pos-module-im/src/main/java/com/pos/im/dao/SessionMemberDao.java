/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.basic.dto.UserIdentifier;
import com.pos.im.domain.SessionMember;
import com.pos.im.dto.session.SessionMemberDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * IM会话成员DAO.
 *
 * @author wayne
 * @version 1.0, 2016/7/11
 */
@Repository
public interface SessionMemberDao {

    void save(@Param("member") SessionMember member);

    void saveBatch(@Param("list") List<SessionMember> memberList);

    void update(@Param("member") SessionMember member);

    SessionMember find(@Param("sessionId") Long sessionId,
                       @Param("userId") Long userId, @Param("userType") String userType);

    List<SessionMember> findBySessionId(
            @Param("sessionId") Long sessionId, @Param("available") Boolean available);

    List<SessionMember> findInSessionIds(
            @Param("sessionIds") Long[] sessionIds, @Param("available") Boolean available);

    List<SessionMemberDto> findMembersBySessionId(@Param("sessionId") Long sessionId,
                                                @Param("available") Boolean available,
                                                @Param("queryCompany") boolean queryCompany);

    List<SessionMemberDto> findMembersInSessionIds(@Param("sessionIds") Long[] sessionIds,
                                                   @Param("available") Boolean available,
                                                   @Param("queryCompany") boolean queryCompany);

    List<UserIdentifier> findMembersIdentifier(@Param("sessionId") Long sessionId);

    /**
     * 以原子方式将指定成员在指定会话中的呼叫次数+1.
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @param userType  用户类型
     * @return 更新的行数
     */
    int incrementCallTotal(@Param("sessionId") Long sessionId,
                           @Param("userId") Long userId, @Param("userType") String userType);

    /**
     * 获取用户加入了的会话id列表
     *
     * @param user      用户标识
     * @param available 是否退出会话，可空
     * @return 会话id列表
     */
    List<Long> querySessionIdsByUser(@Param("user") UserIdentifier user, @Param("available") Boolean available);

    /**
     * 根据sessionid查询会话中包含的用户的id
     * @param sessionId
     * @return
     */
    List<Long> findUserIdBySessionId(@Param("sessionId") Long sessionId);

}
