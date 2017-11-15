/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.mq;

import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * 消息队列（容器）类型
 *
 * @author cc
 * @version 1.0, 2017/1/16
 */
public enum MQReceiverType {

    CONSOLE((byte) 1, "console"),

    CUSTOMER((byte) 2, "customer"),

    EMPLOYEE((byte) 3, "employee"),

    BUSINESS((byte) 4, "business");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    MQReceiverType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MQReceiverType getEnum(byte code) {
        for (MQReceiverType type : values()) {
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
