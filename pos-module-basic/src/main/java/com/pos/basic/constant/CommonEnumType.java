/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonIntEnum;

import java.util.Objects;

/**
 * 枚举类型-用于对应返回前端相应的枚举列表
 *
 * @author wangbing
 * @version 1.0, 2017/8/15
 */
public enum CommonEnumType implements CommonIntEnum {

    HOUSE_TYPE(1, "房型枚举");

    /**
     * 标识码
     */
    private final int code;

    /**
     * 描述内容
     */
    private final String desc;

    CommonEnumType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CommonEnumType getEnum(int code) {
        for (CommonEnumType type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalStateException("非法的枚举类型code值！");
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
