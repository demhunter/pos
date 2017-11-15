/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.SessionPlatformEmployee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * IM家居顾关联Dao
 *
 * @author wangbing
 * @version 1.0, 2017/7/10
 */
@Repository
public interface SessionPlatformEmployeeDao {

    /**
     * 保存IM家居顾问关联关系
     *
     * @param relation 关联关系
     */
    void save(@Param("relation") SessionPlatformEmployee relation);

    /**
     * 根据sessionId查询关联关系
     *
     * @param sessionId im会话id
     * @return 关联关系
     */
    SessionPlatformEmployee getBySessionId(@Param("sessionId") Long sessionId);
}
