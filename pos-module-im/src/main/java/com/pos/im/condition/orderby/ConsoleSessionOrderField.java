/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.condition.orderby;

import com.pos.common.util.mvc.support.OrderField;

import java.util.stream.Stream;

/**
 * @author 睿智
 * @version 1.0, 2017/10/25
 */
public enum ConsoleSessionOrderField implements OrderField {

    CREATE_TIME("createTime"), LAST_TALK_TIME("lastTalkTime");

    private final String value;

    ConsoleSessionOrderField(final String value) {
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
