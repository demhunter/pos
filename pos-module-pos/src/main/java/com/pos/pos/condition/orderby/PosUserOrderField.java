/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.condition.orderby;

import com.pos.common.util.mvc.support.OrderField;
import com.pos.common.util.mvc.support.OrderHelper;

import java.util.stream.Stream;

/**
 * 快捷收款用户排序条件
 *
 * @author wangbing
 * @version 1.0, 2017/8/25
 */
public enum PosUserOrderField implements OrderField {

    CREATE_TIME("createDate");

    private String value;

    public String getValue() {
        return value;
    }

    PosUserOrderField(String value) {
        this.value = value;
    }

    public static OrderField getInstance() {
        return values()[0];
    }

    public static OrderHelper getDefaultOrderHelper() {
        return new OrderHelper(CREATE_TIME.getValue(), false);
    }

    @Override
    public Stream<String> getNames() {
        return Stream.of(values()).map(PosUserOrderField::getValue);
    }

    @Override
    public boolean check(String fieldName) {
        return OrderField.check(this, fieldName);
    }
}
