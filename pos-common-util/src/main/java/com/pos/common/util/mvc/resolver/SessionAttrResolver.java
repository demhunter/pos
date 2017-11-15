/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller的方法参数如果有@FromSession注解, 将根据参数类型从Session中获取其值, 并在SpringMVC解析后自动为该参数赋值.
 * <p>
 * 支持配置多个Class来自动注入不同功用的Session值, 但必须保证写入Session Attribute时的key为类名称, 即Class.getName().
 *
 * @author wayne
 * @version 1.0, 2016/8/25
 */
public class SessionAttrResolver implements HandlerMethodArgumentResolver {

    private List<Class<?>> classList;

    public SessionAttrResolver(List<String> classNames) {
        classList = new ArrayList<>();
        for (String className : classNames) {
            try {
                classList.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(FromSession.class) != null
                && isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request != null) {
            for (Class<?> clazz : classList) {
                if (request.getSession().getAttribute(clazz.getName()) != null
                        && clazz.isAssignableFrom(parameter.getParameterType())) {
                    return request.getSession().getAttribute(clazz.getName());
                }
            }
        }
        return null;
    }

    private boolean isAssignableFrom(Class<?> parameterClass) {
        for (Class<?> clazz : classList) {
            if (clazz.isAssignableFrom(parameterClass)) {
                return true;
            }
        }
        return false;
    }

}