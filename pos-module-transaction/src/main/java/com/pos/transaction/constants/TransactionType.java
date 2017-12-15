/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.constants;

import com.pos.basic.enumHandler.CommonIntEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 交易类型枚举
 *
 * @author wangbing
 * @version 1.0, 2017/12/6
 */
public enum TransactionType implements CommonIntEnum {

    NORMAL_WITHDRAW(1, "普通收款"),

    BROKERAGE_WITHDRAW(2, "佣金提现"),

    LEVEL_UPGRADE(3, "等级晋升");

    private final int code;

    private final String desc;

    TransactionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TransactionType getEnum(int code) {
        for (TransactionType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的交易类型code值");
    }

    /**
     * 是否获取支付验证码
     *
     * @return true：可以；false：不可以
     */
    public boolean canGetPaySmsCode() {
        return this.equals(NORMAL_WITHDRAW) || this.equals(LEVEL_UPGRADE);
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
