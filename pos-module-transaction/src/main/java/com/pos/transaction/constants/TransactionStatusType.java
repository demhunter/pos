/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.constants;

import com.pos.basic.enumHandler.CommonIntEnum;
import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * 交易状态类型枚举
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public enum TransactionStatusType implements CommonIntEnum {

    ORIGIN_TRANSACTION(-1, "已创建"),

    PREDICT_TRANSACTION(0, "已下单"),

    TRANSACTION_IN_PROGRESS(1, "交易处理中"),

    TRANSACTION_FAILED(2, "交易失败"),

    TRANSACTION_SUCCESS(3, "交易成功"),

    TRANSACTION_HANDLED_SUCCESS(4, "已手动处理");

    private final int code;

    private final String desc;

    TransactionStatusType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TransactionStatusType getEnum(int code) {
        for (TransactionStatusType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的交易状态类型code值");
    }

    public static TransactionStatusType getEnum(String symbol) {
        for (TransactionStatusType type : values()) {
            if (Objects.equals(type.toString(), symbol)) {
                return type;
            }
        }

        throw new IllegalParamException("非法的交易状态类型symbol值！");
    }

    /**
     * 当前状态是否允许确认支付
     *
     * @return true：允许用户发起确认支付，false：不允许发起确认支付
     */
    public boolean canConfirmPay() {
        return this.equals(PREDICT_TRANSACTION);
    }

    /**
     * 当前状态是否允许轮询
     *
     * @return true：允许轮询；false：不允许轮询
     */
    public boolean canPolling() {
        return this.equals(TRANSACTION_IN_PROGRESS);
    }

    /**
     * 交易金额是否已到用户收款银行卡
     *
     * @return true：已到账，false：未到账
     */
    public boolean isArrived() {
        return this.equals(TRANSACTION_SUCCESS) || this.equals(TRANSACTION_HANDLED_SUCCESS);
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public int getCode() {
        return code;
    }
}
