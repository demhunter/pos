/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access.handler;

import com.google.common.base.Strings;
import com.pos.common.util.mvc.support.ApiError;
import com.pos.common.util.web.access.RejectedAccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * WEB拒绝访问时将请求的URL重定向的Handler.
 * <p>
 * 该类相当于ApiErrorHandler的加强版: 当设置了ApiError且客户端通过AJAX请求时, 直接Response ApiError, 否则才进行重定向.
 *
 * @author wayne
 * @version 1.0, 2016/6/29
 */
public class URLRedirectHandler implements RejectedAccessHandler {

    /**
     * 重定向的目标URL
     */
    private String redirectUrl;

    /**
     * 重定向时传递当前URL的参数名(只有GET请求才会传递, 如果不设置默认为from)
     */
    private String redirectParamName = "from";

    /**
     * 如果设置了ApiError且客户端通过AJAX请求时, 不进行重定向, 直接Response ApiError
     */
    private ApiError error;

    @Override
    public void rejectedAccess(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String requestedWith = request.getHeader("x-requested-with");
        if (error != null && "XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.getWriter().write(error.getJsonString());
        } else {
            String redirectUrl = this.redirectUrl;
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                String url = request.getRequestURL().toString();
                String queryString = request.getQueryString();
                if (!Strings.isNullOrEmpty(queryString)) {
                    url = url + "?" + queryString;
                }
                redirectUrl = redirectUrl + "?" + redirectParamName + "=" + URLEncoder.encode(url, "UTF-8");
            }
            response.sendRedirect(redirectUrl);
        }
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setRedirectParamName(String redirectParamName) {
        this.redirectParamName = redirectParamName;
    }

    public void setError(ApiError error) {
        this.error = error;
    }

}