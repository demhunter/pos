/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 调试HttpRequest的过滤器, 用于输出每次请求的相关信息, 方便在开发环境下进行调试.
 * <p>
 * 调试包含的信息: URI, Parameters, Headers, Sessions
 *
 * @author wayne
 * @version 1.0, 2016/6/27
 */
public class RequestDebugFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestDebugFilter.class);

    private boolean enable;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (enable) {
            LOG.info(HttpRequestUtils.getRealRemoteAddr(request)
                    + " " + request.getMethod() + " " + request.getRequestURI());
            LOG.info("Parameters: " + HttpRequestUtils.getParameters2Json(request));
            LOG.info("Headers: " + HttpRequestUtils.getHeaders2Json(request));
            LOG.info("Sessions: " + HttpRequestUtils.getSessionAttributes2Json(request));
        }
        filterChain.doFilter(request, response);
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}