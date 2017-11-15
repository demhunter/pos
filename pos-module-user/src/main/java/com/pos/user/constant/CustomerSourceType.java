/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonIntEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 用户来源体系分类枚举
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public enum CustomerSourceType implements CommonIntEnum {

    SELF(1, "自有账号体系"),

    RECORD(2, "第三方账号体系（微信、QQ、微博等）");

    private final int code;

    private final String value;

    CustomerSourceType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static CustomerSourceType getEnum(Integer code) {
        for (CustomerSourceType type : CustomerSourceType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalParamException("非法的用户来源体系分类code值！");
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
