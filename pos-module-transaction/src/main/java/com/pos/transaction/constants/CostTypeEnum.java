/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.constants;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * @author 睿智
 * @version 1.0, 2017/8/23
 */
public enum CostTypeEnum implements CommonByteEnum {

    DESIGN((byte) 1, "设计费"),

    CONSTRUCTION((byte) 2, "施工费"),

    MATERIAL((byte)3,"材料费");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    CostTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CostTypeEnum getEnum(byte code) {
        for (CostTypeEnum costType : values()) {
            if (Objects.equals(costType.code, code)) {
                return costType;
            }
        }

        throw new IllegalParamException("非法的费用类型code值");
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }
}