/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.http;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.cors.*;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 跨域访问的全局过滤器, 验证跨域请求并设置跨域header信息, 但只支持统一的全局CORS配置, 不支持多个CORS配置.
 *
 * @author wayne
 * @version 1.0, 2016/7/5
 */
public class CorsGlobalFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(CorsGlobalFilter.class);

    private CorsConfiguration configuration;

    private CorsProcessor processor = new DefaultCorsProcessor();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(configuration != null && CorsUtils.isCorsRequest(request)) {
            boolean isValid = this.processor.processRequest(configuration, request, response);
            if(!isValid || CorsUtils.isPreFlightRequest(request)) {
                LOG.debug("isValid: " + isValid + " " + PrintableBeanUtils.toString(configuration));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setConfigurer(CorsConfigurer configurer) throws ValidationException {
        if (configurer != null) {
            FieldChecker.checkEmpty(configurer.getAllowedOrigins(), "allowedOrigins");
            FieldChecker.checkEmpty(configurer.getAllowedHeaders(), "allowedHeaders");
            FieldChecker.checkEmpty(configurer.getAllowedMethods(), "allowedMethods");

            configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(parseList(configurer.getAllowedOrigins()));
            configuration.setAllowedHeaders(parseList(configurer.getAllowedHeaders()));
            configuration.setAllowedMethods(parseList(configurer.getAllowedMethods()));
            configuration.setAllowCredentials(configurer.isAllowCredentials());
            if (!Strings.isNullOrEmpty(configurer.getExposedHeaders())) {
                configuration.setExposedHeaders(parseList(configurer.getExposedHeaders()));
            }
            if (configurer.getMaxAge() > 0) {
                configuration.setMaxAge(configurer.getMaxAge());
            }
        }
    }

    public void setCorsProcessor(CorsProcessor processor) {
        Assert.notNull(processor, "CorsProcessor must not be null");
        this.processor = processor;
    }

    private List<String> parseList(String str) {
        return Lists.newArrayList(Splitter.on(',').trimResults().omitEmptyStrings().split(str));
    }

}