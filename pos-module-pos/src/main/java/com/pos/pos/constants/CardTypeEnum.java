/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.constants;

import com.pos.basic.enumHandler.CommonByteEnum;

import java.util.Objects;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public enum CardTypeEnum implements CommonByteEnum {

    DEBIT_CARD((byte) 1, "储蓄卡"),

    CREDIT_CARD((byte) 2, "信用卡");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    CardTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CardTypeEnum getEnum(byte code) {
        for (CardTypeEnum cardType : values()) {
            if (Objects.equals(cardType.code, code)) {
                return cardType;
            }
        }
        return null;

    }

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }
}
