/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.helipay.util;

import com.pos.common.util.exception.ErrorCode;

/**
 * @author 睿智
 * @version 1.0, 2017/8/23
 */
public enum PosErrorCode implements ErrorCode {

    POS_NORMAL_FAIL(1001,"失败，%s"),

    CHECK_SIGN_FAIL(1002,"验签失败"),

    REQUEST_FAIL(1003,"请求失败"),

    PAY_EXCEPTION(1004,"交易异常"),

    ALREADY_BIND_CARD(1005,"已经绑定了收款银行卡，不能再次绑定"),

    CARD_IS_WRONG(1006,"卡号错误，请重新填写"),

    IN_CARD_NOT_EXIST(1007,"没有收款银行卡"),

    CARD_MSG_IS_WRONG(1008,"付款银行卡必须跟收款银行卡是同一个持卡人"),

    CARD_ALREADY_EXIST(1009,"卡已经存在"),

    CHANNEL_MSG_ERROR(1010,"渠道商信息错误"),

    LEADER_MSG_ERROR(1011,"上级信息错误");

    private final int code;

    private final String message;

    PosErrorCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
