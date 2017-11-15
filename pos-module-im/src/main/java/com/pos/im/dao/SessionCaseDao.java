/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.SessionCase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 与IM会话关联的案例DAO.
 *
 * @author wayne
 * @version 1.0, 2016/7/11
 */
@Repository
public interface SessionCaseDao {

    SessionCase get(@Param("id") Long id);

    void save(@Param("sc") SessionCase sessionCase);

    SessionCase findBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 查询指定用户发起的针对指定案例的会话.
     *
     * @param userId    用户ID
     * @param userType  用户类型
     * @param caseId    案例ID
     * @param available 会话是否关闭
     * @return
     */
    SessionCase findUserCaseSession(
            @Param("userId") Long userId, @Param("userType") String userType,
            @Param("caseId") Long caseId, @Param("available") boolean available);

}
