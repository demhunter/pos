/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access.handler;

import com.pos.common.util.web.access.RejectedAccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * WEB拒绝访问时返回HTTP响应错误的Handler.
 *
 * @author wayne
 * @version 1.0, 2016/6/29
 */
public class HttpErrorHandler implements RejectedAccessHandler {

    /**
     * @see HttpServletResponse SC_*
     */
    private int httpStatus;

    private String message;

    @Override
    public void rejectedAccess(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendError(httpStatus, message);
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}