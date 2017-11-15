/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.http;

/**
 * 跨域访问的配置对象.
 *
 * @author wayne
 * @version 1.0, 2016/7/5
 */
public final class CorsConfigurer {

    private String allowedOrigins;

    private String allowedMethods;

    private String allowedHeaders;

    private String exposedHeaders;

    private boolean allowCredentials;

    private long maxAge;

    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public String getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public String getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public String getExposedHeaders() {
        return exposedHeaders;
    }

    public void setExposedHeaders(String exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

}