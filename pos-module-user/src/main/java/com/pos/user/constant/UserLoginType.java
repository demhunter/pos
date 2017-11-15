/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonIntEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 用户登录类型枚举
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public enum UserLoginType implements CommonIntEnum {

    APP_CUSTOMER_LOGIN(1, "APP客户端登录"),

    WEB_MANAGER_LOGIN(2, "APP客户端登录");

    private final int code;

    private final String desc;

    UserLoginType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserLoginType getEnum(byte code) {
        for (UserLoginType type : UserLoginType.values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw  new IllegalParamException("非法的用户登录类型code值");
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
