/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.condition.orderby;

import com.pos.common.util.mvc.support.OrderField;
import com.pos.common.util.mvc.support.OrderHelper;

import java.util.stream.Stream;

/**
 * 交易记录排序参数
 *
 * @author wangbing
 * @version 1.0, 2017/10/20
 */
public enum PosTransactionOrderField implements OrderField {

    CREATE_TIME("createDate"),

    PAY_TIME("pay_date");

    private String value;

    public String getValue() {
        return value;
    }

    PosTransactionOrderField(String value) {
        this.value = value;
    }

    public static OrderField getInstance() {
        return values()[0];
    }

    public static OrderHelper getDefaultOrderHelper() {
        return new OrderHelper(CREATE_TIME.getValue(), false);
    }

    public static OrderHelper getPayTimeOrderHelper() {
        return new OrderHelper(PAY_TIME.getValue(), false);
    }

    @Override
    public Stream<String> getNames() {
        return Stream.of(values()).map(PosTransactionOrderField::getValue);
    }

    @Override
    public boolean check(String fieldName) {
        return OrderField.check(this, fieldName);
    }
}
