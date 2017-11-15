/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.exception;

/**
 * 错误码接口.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public interface ErrorCode {

    /**
     * 获取错误码.
     *
     * @return 错误码必须大于0
     */
    int getCode();

    /**
     * 获取错误信息.
     *
     * @return 错误信息
     */
    String getMessage();

}
