/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 平台管理员类型定义.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public enum PlatformEmployeeType implements UserDetailType, CommonByteEnum {

    //Customer service manager
    CSM((byte) 1, "客服经理"),
    //Home Consultant
    HC((byte) 2, "家居顾问");

    private final byte code;

    private final String desc;

    PlatformEmployeeType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public byte getValue() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean compare(byte code) {
        return this.code == code;
    }

    public static PlatformEmployeeType getEnum(byte code) {
        for (PlatformEmployeeType type : PlatformEmployeeType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalParamException("非法的value值！");
    }

    @Override
    public byte getCode() {
        return code;
    }
}