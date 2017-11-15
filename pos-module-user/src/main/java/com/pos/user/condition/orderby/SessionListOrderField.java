/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.orderby;

import com.pos.common.util.mvc.support.OrderField;

import java.util.stream.Stream;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public enum SessionListOrderField implements OrderField{
    LAST_DATE("lastDate");

    private final String value;

    SessionListOrderField(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderField getInterface() {
        return values()[0];
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
