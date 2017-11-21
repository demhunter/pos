/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access.handler;

import com.pos.common.util.web.access.RejectedAccessHandler;
import com.pos.common.util.mvc.support.ApiError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * WEB拒绝访问时返回API调用错误的Handler.
 *
 * @author wayne
 * @version 1.0, 2016/6/29
 */
public class ApiErrorHandler implements RejectedAccessHandler {

    private ApiError error;

    @Override
    public void rejectedAccess(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.getWriter().write(error.getJsonString());
    }

    public void setError(ApiError error) {
        this.error = error;
    }

}