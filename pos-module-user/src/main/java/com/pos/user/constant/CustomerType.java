/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * @author lifei
 * @version 1.0, 2017/9/6
 */
public enum CustomerType implements CommonByteEnum {
    /**
     * 自然客户
     */
    NATURE((byte) 1, "其他客户"),
    /**
     * 报备客户
     */
    RECORD((byte) 2, "报备客户"),
    /**
     * B端从业者
     */
    TWITTER((byte) 3, "推客");

    private final byte code;

    private final String value;

    CustomerType(final byte code, final String value) {
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

    public static CustomerType getEnum(Byte code) {
        for (CustomerType type : CustomerType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalParamException("非法的value值！");
    }
}
