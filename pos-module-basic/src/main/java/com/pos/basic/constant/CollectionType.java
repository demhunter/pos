/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 收藏类型
 *
 * @author wangbing
 * @version 1.0, 2016/12/29
 */
public enum CollectionType implements CommonByteEnum {

    CASE_COLLECTION((byte) 1, "收藏作品"),

    EMPLOYEE_COLLECTION((byte) 2, "收藏业者"),

    COMPANY_COLLECTION((byte) 3, "收藏公司");

    private final byte code;

    private final String desc;

    @Override
    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    CollectionType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CollectionType getEnum(byte code) {
        for (CollectionType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalParamException("非法的code值");
    }
}
