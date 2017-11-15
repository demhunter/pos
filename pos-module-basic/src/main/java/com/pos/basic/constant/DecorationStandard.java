/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

/**
 * 装修标准定义.
 *
 * @author wayne
 * @version 1.0, 2017/3/15
 */
public enum DecorationStandard implements CommonByteEnum {

    ROUGH_ROOM((byte) 1, "毛坯"),

    HARDBOUND_ROOM((byte) 2, "精装");

    private final byte code;

    private final String desc;

    @Override
    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    DecorationStandard(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DecorationStandard getEnum(byte code) {
        for (DecorationStandard type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

}
