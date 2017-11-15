/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

/**
 * 可复制的对象接口, 因Cloneable需要捕获异常且不支持泛型, 导致使用不方便, 故提供一个接口以替代.
 *
 * @author wayne
 * @version 1.0, 2016/8/4
 */
public interface Copyable<T> {

    /**
     * 创建并返回当前对象的一个副本, 是否支持深度拷贝不作强制要求, 由实现类自行决定.
     *
     * @return 当前对象的一个副本
     */
    T copy();

}
