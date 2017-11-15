/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * 订单类型
 *
 * @author cc
 * @version 1.0, 2016/11/8
 */
public enum OrderType implements CommonByteEnum {

    BOOKING_ORDER((byte) 1, "预约订单"),

    CONTRACT_ORDER((byte) 2, "合同订单");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    OrderType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderType getEnum(byte code) {
        for (OrderType type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalParamException("非法的code值！");
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
