/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 设计费用类型
 *
 * @author lifei
 * @version 1.0, 2017/8/7
 */
public enum DesignType implements CommonByteEnum {
    HARD_PRICE((byte) 1, "硬装设计费"),

    SOFT_PRICE((byte) 2, "软装设计费");

    private final byte code;

    private final String value;

    DesignType(final byte code, final String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public byte getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean compare(String value) {
        return this.value.equals(value);
    }

    public static DesignType getEnum(String value) {
        for (DesignType type : DesignType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalParamException("非法的value值！");
    }
}
