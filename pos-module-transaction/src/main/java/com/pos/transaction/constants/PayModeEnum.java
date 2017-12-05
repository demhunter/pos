/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.constants;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * @author 睿智
 * @version 1.0, 2017/8/24
 */
public enum PayModeEnum implements CommonByteEnum {

    WECHAT((byte) 1, "微信"),

    ALIPAY((byte) 2, "支付宝"),

    CYBERBANK((byte)3,"网银转账"),

    OFFLINE((byte)4,"线下打款");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    PayModeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PayModeEnum getEnum(byte code) {
        for (PayModeEnum payMode : values()) {
            if (Objects.equals(payMode.code, code)) {
                return payMode;
            }
        }

        throw new IllegalParamException("非法的付款方式code值");
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }
}