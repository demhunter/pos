/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.condition.oerderby;

import com.pos.common.util.mvc.support.OrderField;
import com.pos.common.util.mvc.support.OrderHelper;

import java.util.stream.Stream;

/**
 * 聚合用户信息排序条件
 *
 * @author wangbing
 * @version 1.0, 2017/12/11
 */
public enum CustomerIntegrateOrderField implements OrderField {

    REGISTER_TIME("registerTime");

    private String value;

    public String getValue() {
        return value;
    }

    CustomerIntegrateOrderField(String value) {
        this.value = value;
    }

    public static OrderField getInstance() {
        return values()[0];
    }

    public static OrderHelper getDefaultOrderHelper() {
        return new OrderHelper(REGISTER_TIME.getValue(), false);
    }

    @Override
    public Stream<String> getNames() {
        return Stream.of(values()).map(CustomerIntegrateOrderField::getValue);
    }

    @Override
    public boolean check(String fieldName) {
        return OrderField.check(this, fieldName);
    }
}
