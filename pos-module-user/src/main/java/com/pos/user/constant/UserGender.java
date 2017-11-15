/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 用户性别定义.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public enum UserGender implements CommonByteEnum {

    SECRECY((byte) 0, "保密"), MALE((byte) 1, "男"), FEMALE((byte) 2, "女");

    private final byte code;

    private final String desc;

    UserGender(final byte code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static UserGender getEnum(byte code) {
        for (UserGender type : UserGender.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalParamException("系统不支持您输入的性别选项。");
    }

}