/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access;

import com.google.common.base.Strings;
import com.pos.common.util.exception.InitializationException;
import com.pos.common.util.web.http.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * WEB访问控制的过滤器, 可以通过配置不同的PermissionValidator和RejectedAccessHandler来实现多种不同的访问策略.
 *
 * @author wayne
 * @version 1.0, 2016/6/21
 */
public class AccessControlFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(AccessControlFilter.class);

    private PermissionValidator validator;

    private RejectedAccessHandler rejectedHandler;

    private RejectedAccessHandler tokenExpiredHandler;

    private RejectedAccessHandler tokenRefreshedHandler;

    private RejectedAccessHandler tokenLoggedOtherHandler;

    /**
     * 如果不为空且不等于/, 将替换配置中所有以/开头的URL根路径, 用于适应不同部署环境
     */
    private String basePath;

    /**
     * 禁止访问的URL列表, 用于生产环境屏蔽某些工具入口, 如在线API查询入口
     */
    private String[] forbiddenUrls;

    /**
     * 不论URL为何, 指定的Request Method不用通过验证即可访问
     */
    private String[] excludeMethods;

    /**
     * 白名单, 指定的URL不用通过验证即可访问
     */
    private String[] excludeUrls;

    /**
     * 黑名单, 指定的URL必须通过验证才能访问
     */
    private String[] includeUrls;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        initialize();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isForbidden(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else if (isSkipValidation(request)) {
            filterChain.doFilter(request, response);
        } else {
            doValidate(request, response, filterChain);
        }
    }

    private void doValidate(HttpServletRequest request, HttpServletResponse response,
                            FilterChain filterChain) throws ServletException, IOException {
        try {
            if (validator.isValidated(request)) {
                filterChain.doFilter(request, response);
            } else {
                LOG.info("请求被拒绝: " + HttpRequestUtils.getRealRemoteAddr(request)
                        + " " + request.getMethod() + " " + request.getRequestURI());
                rejectedHandler.rejectedAccess(request, response);
            }
        } catch (TokenExpiredException e) {
            LOG.info("[TokenExpiredException] " + e.getMessage());
            tokenExpiredHandler.rejectedAccess(request, response);
        } catch (TokenRefreshedException e) {
            LOG.info("[TokenRefreshedException] " + e.getMessage());
            tokenRefreshedHandler.rejectedAccess(request, response);
        } catch (TokenLoggedOtherException e) {
            LOG.info("[TokenLoggedOtherException]" + e.toString());
            tokenLoggedOtherHandler.rejectedAccess(request, response);
        }
    }

    private boolean isForbidden(HttpServletRequest request) {
        if (forbiddenUrls == null || forbiddenUrls.length == 0) {
            return false;
        }
        String currentUri = request.getRequestURI();
        for (String forbiddenUri : forbiddenUrls) {
            if (currentUri.matches(forbiddenUri)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSkipValidation(HttpServletRequest request) {
        if (excludeUrls != null) {
            return isExcludeMethod(request) || isExcludeUrl(request);
        } else {
            return isExcludeMethod(request) || !isIncludeUrl(request);
        }
    }

    private boolean isExcludeMethod(HttpServletRequest request) {
        if (excludeMethods == null || excludeMethods.length == 0) {
            return false;
        }
        String method = request.getMethod();
        for (String excludeMethod : excludeMethods) {
            if (method.equalsIgnoreCase(excludeMethod)) {
                return true;
            }
        }
        return false;
    }

    private boolean isExcludeUrl(HttpServletRequest request) {
        if (excludeUrls == null || excludeUrls.length == 0) {
            return false;
        }
        String currentUri = request.getRequestURI();
        for (String excludeUri : excludeUrls) {
            if (currentUri.matches(excludeUri)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIncludeUrl(HttpServletRequest request) {
        if (includeUrls == null || includeUrls.length == 0) {
            return false;
        }
        String currentUri = request.getRequestURI();
        for (String includeUri : includeUrls) {
            if (currentUri.matches(includeUri)) {
                return true;
            }
        }
        return false;
    }

    private void initialize() {
        if (forbiddenUrls != null && forbiddenUrls.length > 0) {
            if (!Strings.isNullOrEmpty(basePath) && !basePath.equals("/")) {
                LOG.info("替换forbiddenUrls根路径, basePath = " + basePath);
                for (int i = 0; i < forbiddenUrls.length; i++) {
                    if (forbiddenUrls[i].startsWith("/")) {
                        forbiddenUrls[i] = basePath + forbiddenUrls[i];
                    }
                }
            }
            LOG.info("forbiddenUrls: " + Arrays.toString(forbiddenUrls));
        }

        LOG.info("excludeMethods: " + Arrays.toString(excludeMethods));

        if (excludeUrls != null && includeUrls != null) {
            throw new InitializationException("不能同时配置黑、白名单(includeUrls、excludeUrls)！");
        }

        if (excludeUrls != null && excludeUrls.length > 0) {
            if (!Strings.isNullOrEmpty(basePath) && !basePath.equals("/")) {
                LOG.info("替换excludeUrls根路径, basePath = " + basePath);
                for (int i = 0; i < excludeUrls.length; i++) {
                    if (excludeUrls[i].startsWith("/")) {
                        excludeUrls[i] = basePath + excludeUrls[i];
                    }
                }
            }
            LOG.info("excludeUrls: " + Arrays.toString(excludeUrls));
        } else {
            if (!Strings.isNullOrEmpty(basePath) && !basePath.equals("/")) {
                LOG.info("替换includeUrls根路径, basePath = " + basePath);
                for (int i = 0; i < includeUrls.length; i++) {
                    if (includeUrls[i].startsWith("/")) {
                        includeUrls[i] = basePath + includeUrls[i];
                    }
                }
            }
            LOG.info("includeUrls: " + Arrays.toString(includeUrls));
        }
    }

    public void setValidator(PermissionValidator validator) {
        this.validator = validator;
    }

    public void setRejectedHandler(RejectedAccessHandler rejectedHandler) {
        this.rejectedHandler = rejectedHandler;
    }

    public void setTokenExpiredHandler(RejectedAccessHandler tokenExpiredHandler) {
        this.tokenExpiredHandler = tokenExpiredHandler;
    }

    public void setTokenRefreshedHandler(RejectedAccessHandler tokenRefreshedHandler) {
        this.tokenRefreshedHandler = tokenRefreshedHandler;
    }

    public void setTokenLoggedOtherHandler(RejectedAccessHandler tokenLoggedOtherHandler) {
        this.tokenLoggedOtherHandler = tokenLoggedOtherHandler;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setForbiddenUrls(String[] forbiddenUrls) {
        this.forbiddenUrls = forbiddenUrls;
    }

    public void setExcludeMethods(String[] excludeMethods) {
        this.excludeMethods = excludeMethods;
    }

    public void setExcludeUrls(String[] excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    public void setIncludeUrls(String[] includeUrls) {
        this.includeUrls = includeUrls;
    }

}