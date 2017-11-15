/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 用户类型定义.
 *
 * @author wayne
 * @version 1.0, 2016/6/7
 */
public enum UserType implements CommonByteEnum {

    /**
     * C端用户
     */
    CUSTOMER((byte) 1, "c", true),
    /**
     * B端用户
     */
    BUSINESS((byte) 2, "b", false),
    /**
     * B端从业者
     */
    EMPLOYEE((byte) 3, "e", true),
    /**
     * 平台管理员
     */
    MANAGER((byte) 4, "m", false);

    private final byte code;

    private final String value;

    private final boolean usePhone; // 是否使用手机号登录&注册，默认使用用户名

    UserType(final byte code, final String value, final boolean usePhone) {
        this.code = code;
        this.value = value;
        this.usePhone = usePhone;
    }

    @Override
    public byte getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean isUsePhone() {
        return usePhone;
    }

    public boolean compare(String value) {
        return this.value.equals(value);
    }

    public static UserType getEnum(String value) {
        for (UserType type : UserType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalParamException("非法的value值！");
    }

}
