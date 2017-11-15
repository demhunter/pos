/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.orderby;

import com.pos.common.util.mvc.support.OrderField;

import java.util.stream.Stream;

/**
 * 用户反馈意见的排序条件定义.
 *
 * @author wayne
 * @version 1.0, 2016/10/9
 */
public enum RelationTwitterOrderField implements OrderField {

    RELATION_DATE("relationDate");

    private final String value;

    RelationTwitterOrderField(final String value) {
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
