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
public enum ManagerType implements UserDetailType, CommonByteEnum {

    DEFAULT((byte) 1, "普通"), CS((byte) 2, "客服"), BD((byte) 3, "商务拓展");

    private final byte code;

    private final String desc;

    ManagerType(byte code, String desc) {
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

    public static ManagerType getEnum(byte code) {
        for (ManagerType type : ManagerType.values()) {
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