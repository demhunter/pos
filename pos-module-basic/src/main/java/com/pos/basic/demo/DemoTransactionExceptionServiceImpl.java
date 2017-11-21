/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.demo;

import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 事务异常Demo ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/11/6
 */
@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings("all")
public class DemoTransactionExceptionServiceImpl implements DemoTransactionExceptionService {

    private final static Logger LOG = LoggerFactory.getLogger(DemoTransactionExceptionServiceImpl.class);

    @Resource
    private DemoTransactionExceptionDao demoTransactionExceptionDao;

    private static Long COUNT = 1L;

    private static final String NAME = "Name:";

    @Override
    public ApiResult<NullObject> exceptionRollback() {

        succSave();

        try {
            exceptionSave();
        } catch (Exception e) {
            LOG.error("------------------------>添加用户信息发生异常，exception={}", e);
        }

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> exceptionNoRollback() {
        succSave();

        try {
            exceptionSave();
        } catch (Exception e) {
            LOG.error("------------------------>添加用户信息发生异常，exception={}", e);
        }

        return ApiResult.succ();
    }

    private void succSave() {
        LOG.info("------------------------>开始添加第{}个用户", COUNT);
        DemoUser demoUser = new DemoUser();
        demoUser.setName(NAME + COUNT);
        demoUser.setPhone("13888888888");

        LOG.info("------------------------>用户信息={}", JsonUtils.objectToJson(demoUser));
        demoTransactionExceptionDao.save(demoUser);
        LOG.info("------------------------>第{}个用户添加成功", COUNT);

        COUNT++;
    }

    private void exceptionSave() throws Exception {
        LOG.info("------------------------>开始添加第{}个用户", COUNT);
        DemoUser demoUser = new DemoUser();
        demoUser.setName(NAME + COUNT);
        demoUser.setPhone("13888888888");

        LOG.info("------------------------>用户信息={}", JsonUtils.objectToJson(demoUser));
        try {
            throw new Exception("添加用户信息发生不可测异常！");
        } catch (Exception e) {
            LOG.error("------------------------>第{}个用户添加失败", COUNT);
            throw e;
        }
    }
}
