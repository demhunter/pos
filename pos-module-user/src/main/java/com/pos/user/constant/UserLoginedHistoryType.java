/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.common.util.exception.IllegalParamException;

/**
 * 用户登录历史记录类型
 *
 * @author wangbing
 * @version 1.0, 2017/03/28
 */
public enum UserLoginedHistoryType {

    ALL((byte) 0, "所有的账号"),

    LOGINED((byte) 1, "曾经登录过的账号"),

    NOT_LOGINED((byte) 2, "从未登录过的账号"),

    LATEST_NOT_LOGINED((byte) 3, "最近7天未登录过的账号");

    UserLoginedHistoryType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final byte code;

    private final String desc;

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public UserLoginedHistoryType getEnum(byte code) {
        for (UserLoginedHistoryType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的用户登录历史记录类型code值");
    }
}
