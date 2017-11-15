/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.condition.orderby;

import com.pos.common.util.mvc.support.OrderField;

import java.util.stream.Stream;

/**
 * @author wangbing
 * @version 1.0, 2017/03/18
 */
public enum BuildingOrderField implements OrderField {

    UPDATE_TIME("updateTime"), CREATE_TIME("createTime");

    private final String value;

    BuildingOrderField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BuildingOrderField getInterface() {
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
