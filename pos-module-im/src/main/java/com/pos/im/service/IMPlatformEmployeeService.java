/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service;

import com.pos.im.domain.SessionPlatformEmployee;

/**
 * IM家居顾关联Service
 *
 * @author wangbing
 * @version 1.0, 2017/7/10
 */
public interface IMPlatformEmployeeService {

    /**
     * 根据sessionId查询关联关系
     *
     * @param sessionId sessionId
     * @return 关联关系
     */
    SessionPlatformEmployee getBySessionId(Long sessionId);
}
