/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * WEB拒绝访问的处理器接口.
 *
 * @author wayne
 * @version 1.0, 2016/6/21
 */
public interface RejectedAccessHandler {

    /**
     * 当WEB拒绝访问时的执行策略.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException
     */
    void rejectedAccess(HttpServletRequest request, HttpServletResponse response) throws IOException;

}