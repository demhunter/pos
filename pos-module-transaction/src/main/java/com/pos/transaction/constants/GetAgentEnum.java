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
public enum GetAgentEnum implements CommonByteEnum {

    NOT_GET((byte) 0, "未提取"),

    APPLY((byte) 1, "已申请"),

    GET((byte)2,"已提现");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    GetAgentEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static GetAgentEnum getEnum(byte code) {
        for (GetAgentEnum getAgent : values()) {
            if (Objects.equals(getAgent.code, code)) {
                return getAgent;
            }
        }

        throw new IllegalParamException("非法的佣金提现状态code值");
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }
}