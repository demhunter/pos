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
public enum CardUsageEnum implements CommonByteEnum {

    IN_CARD((byte) 1, "转入"),

    OUT_CARD((byte) 2, "转出");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    CardUsageEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CardUsageEnum getEnum(byte code) {
        for (CardUsageEnum cardUsage : values()) {
            if (Objects.equals(cardUsage.code, code)) {
                return cardUsage;
            }
        }

        throw new IllegalParamException("非法的CardUsageEnum code值");
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }
}