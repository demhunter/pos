/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.support;

import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.exception.ErrorCode;

/**
 * 接口调用失败的错误对象.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public abstract class ApiError {

    private String jsonString;

    private ApiResult<NullObject> result;

    protected ApiError(ErrorCode error, String message) {
        result = new ApiResult<>(error, message);
        jsonString = JsonUtils.objectToJsonIgnoreNull(result);
    }

    public String getJsonString() {
        return jsonString;
    }

    public ApiResult<NullObject> getResult() {
        return result;
    }

}