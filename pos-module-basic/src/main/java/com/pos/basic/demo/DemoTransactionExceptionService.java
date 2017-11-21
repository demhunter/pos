/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.demo;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;

/**
 * 事务异常Demo Service
 *
 * @author wangbing
 * @version 1.0, 2017/11/6
 */
public interface DemoTransactionExceptionService {

    /**
     * 事务异常导致回滚
     */
    ApiResult<NullObject> exceptionRollback();

    /**
     * 事务异常不会导致回滚
     */
    ApiResult<NullObject> exceptionNoRollback();
}
