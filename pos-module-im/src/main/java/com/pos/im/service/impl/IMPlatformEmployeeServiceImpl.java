/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.impl;

import com.pos.common.util.validation.FieldChecker;
import com.pos.im.dao.SessionPlatformEmployeeDao;
import com.pos.im.service.IMPlatformEmployeeService;
import com.pos.im.domain.SessionPlatformEmployee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * IM家居顾关联ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/7/10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IMPlatformEmployeeServiceImpl implements IMPlatformEmployeeService {

    @Resource
    private SessionPlatformEmployeeDao sessionPlatformEmployeeDao;

    @Override
    public SessionPlatformEmployee getBySessionId(Long sessionId) {
        FieldChecker.checkEmpty(sessionId, "sessionId");

        return sessionPlatformEmployeeDao.getBySessionId(sessionId);
    }
}
