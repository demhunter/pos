/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.constants;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public enum AuthStatusEnum implements CommonByteEnum {

    DISABLE((byte) 1, "未启用"),

    ENABLE((byte) 2, "已启用"),

    CLOSED((byte)3,"已关闭");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    AuthStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AuthStatusEnum getEnum(byte code) {
        for (AuthStatusEnum authStatus : values()) {
            if (Objects.equals(authStatus.code, code)) {
                return authStatus;
            }
        }

        throw new IllegalParamException("非法的权限状态code值");
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }
}