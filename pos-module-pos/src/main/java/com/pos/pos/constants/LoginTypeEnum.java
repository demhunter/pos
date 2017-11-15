/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.constants;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * POS 第一次用户登录的登录类型
 *
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public enum LoginTypeEnum implements CommonByteEnum {

    SPREAD((byte) 1, "推广发展下级客户链接"),

    DEVELOP((byte) 2, "推广发展下级推客链接");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    LoginTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LoginTypeEnum getEnum(byte code) {
        for (LoginTypeEnum loginType : values()) {
            if (Objects.equals(loginType.code, code)) {
                return loginType;
            }
        }

        throw new IllegalParamException("非法的登录类型code值");
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }
}