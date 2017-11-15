/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

import java.util.Objects;

/**
 * DemoByteEnum
 *
 * @author cc
 * @version 1.0, 2017/3/24
 */
public enum DemoByteEnum implements CommonByteEnum {

    DEMO_ONE((byte) 1, "demo one");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    DemoByteEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DemoByteEnum getEnum(byte code) {
        for (DemoByteEnum type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalStateException("非法的code值！");
    }

    @Override
    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
