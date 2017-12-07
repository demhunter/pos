/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.constants;

import com.pos.basic.enumHandler.CommonIntEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 佣金状态类型枚举
 *
 * @author wangbing
 * @version 1.0, 2017/12/7
 */
public enum BrokerageStatusType implements CommonIntEnum {

    NOT_WITHDRAW(0, "未提现"),

    WITHDRAWING(1, "提现处理中"),

    WITHDRAWAL(2, "已提现");

    private final int code;

    private final String desc;

    public static BrokerageStatusType getEnum(int code) {
        for (BrokerageStatusType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的佣金状态code值！");
    }

    BrokerageStatusType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public int getCode() {
        return code;
    }
}
