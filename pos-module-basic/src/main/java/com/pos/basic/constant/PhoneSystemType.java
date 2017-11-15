/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 手机系统类型
 *
 * @author wangbing
 * @version 1.0, 2017/03/27
 */
public enum PhoneSystemType implements CommonByteEnum {

    ALL((byte) 0, "全部"),

    IOS((byte) 1, "IOS"),

    ANDROID((byte) 2, "安卓");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    PhoneSystemType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PhoneSystemType getEnum(byte code) {
        for (PhoneSystemType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的手机系统类型code值");
    }
}
