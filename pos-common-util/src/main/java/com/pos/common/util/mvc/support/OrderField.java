/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.support;

import java.util.stream.Stream;

/**
 * 允许进行排序的字段/别名接口.
 *
 * @author wayne
 * @version 1.0, 2016/7/12
 */
public interface OrderField {

    /**
     * 获取允许进行排序的字段/别名.
     */
    Stream<String> getNames();

    /**
     * 检查是否允许指定字段/别名来排序.
     *
     * @param fieldName 指定字段/别名
     */
    boolean check(String fieldName);

    /**
     * 检查是否允许指定字段/别名来排序.
     *
     * @param orderField 要比较的接口实现类
     * @param fieldName  指定字段/别名
     */
    static boolean check(OrderField orderField, String fieldName) {
        return orderField.getNames().filter(name -> name.equals(fieldName)).count() > 0;
    }

}
