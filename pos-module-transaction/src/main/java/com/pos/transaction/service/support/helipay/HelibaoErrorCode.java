/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay;

import com.pos.common.util.exception.ErrorCode;

/**
 * 合利宝请求相关错误定义（code：1200 - 1299）
 *
 * @author wangbing
 * @version 1.0, 2017/12/21
 */
public enum HelibaoErrorCode implements ErrorCode {

    NORMAL_FAIL_MSG(1200, "失败，%s"),

    SIGN_CHECKED_FAIL(1201, "数据验签失败"),

    REQUEST_FAIL(1202, "请求失败"),

    SETTLEMENT_CARD_QUERY_EXCEPTION(1203, "结算卡查询异常"),

    SETTLEMENT_CARD_BIND_EXCEPTION(1204, "结算卡绑定异常"),

    ORDER_QUERY_EXCEPTION(1205, "订单信息查询异常");

    HelibaoErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;

    private final String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
