/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 来自Session的参数值注解, 结合SessionAttrResolver来实现自动从Session读取参数值并赋值.
 * <p>
 * 值来源并不仅限于Session, 你可以自定义数据来源和类型(比如用redis实现分布式用户会话), 为此只需要实现你自己的XXXResolver即可.
 *
 * @author wayne
 * @version 1.0, 2016/8/25
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromSession {
}
