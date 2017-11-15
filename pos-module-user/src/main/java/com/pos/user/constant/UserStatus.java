/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

/**
 * 用户状态Status
 *
 * @author cc
 * @version 1.0, 16/8/9
 */
public enum UserStatus implements CommonByteEnum {

    DISABLE((byte) 0, "禁用"), ENABLE((byte) 1, "启用");

    private final byte code;

    private final String desc;

    UserStatus(final byte code, final String desc) {
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

    public static UserStatus getEnum(byte code) {
        for (UserStatus status : UserStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
