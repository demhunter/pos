/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.orderby;

import com.pos.common.util.mvc.support.OrderField;

import java.util.stream.Stream;

/**
 * 业者的排序条件定义
 *
 * @author wangbing
 * @version 1.0, 2016/12/02
 */
public enum EmployeeOrderField implements OrderField{
    CREATE_TIME("createTime"), UPDATE_TIME("updateTime");

    private final String value;

    EmployeeOrderField(final String value) {
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
