/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.condition.orderby;

import com.pos.common.util.mvc.support.OrderField;

import java.util.stream.Stream;

/**
 * 品牌排序条件定义.
 *
 * @author wayne
 * @version 1.0, 2016/9/29
 */
public enum BrandOrderField implements OrderField {

    CREATE_TIME("createTime"), AVAILABLE("available");

    private final String value;

    BrandOrderField(final String value) {
        this.value = value;
    }

    public static OrderField getInterface() {
        return values()[0];
    }

    public String getValue() {
        return value;
    }

    @Override
    public Stream<String> getNames() {
        return Stream.of(values()).map(val -> val.getValue());
    }

    @Override
    public boolean check(String fieldName) {
        return OrderField.check(this, fieldName);
    }

}