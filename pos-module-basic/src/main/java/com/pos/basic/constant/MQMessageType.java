/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * MQMessageType
 *
 * @author cc
 * @version 1.0, 2017/1/17
 */
public enum MQMessageType implements CommonByteEnum {

    PRODUCER((byte) 1, "生产者"),

    CONSUMER((byte) 2, "消费者");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    MQMessageType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MQMessageType getEnum(byte code) {
        for (MQMessageType type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalParamException("非法的code值！");
    }

    @Override
    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
