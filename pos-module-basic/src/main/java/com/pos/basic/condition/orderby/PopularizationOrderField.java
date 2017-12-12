/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.condition.orderby;

import com.pos.common.util.mvc.support.OrderField;
import com.pos.common.util.mvc.support.OrderHelper;

import java.util.stream.Stream;

/**
 * 推广排序参数定义
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
public enum PopularizationOrderField implements OrderField {

    CREATE_TIME("create_time");

    private final String value;

    public String getValue() {
        return value;
    }

    PopularizationOrderField(String value) {
        this.value = value;
    }

    public static OrderField getInterface() {
        return values()[0];
    }

    public static OrderHelper getDefaultOrderHelper() {
        return new OrderHelper(CREATE_TIME.getValue(), false);
    }

    @Override
    public Stream<String> getNames() {
        return Stream.of(values()).map(PopularizationOrderField::getValue);
    }

    @Override
    public boolean check(String fieldName) {
        return OrderField.check(this, fieldName);
    }
}
