/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.exception;

import com.pos.common.util.exception.ErrorCode;

/**
 * POS交易服务相关的错误码定义（code：1100 - 1199）
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public enum TransactionErrorCode implements ErrorCode {

    AUTHORITY_AUDIT_STATUS_NOT_SUBMIT(1100, "身份认证未提交，不能进行收款操作！"),

    AUTHORITY_AUDIT_STATUS_REJECTED(1101, "身份认证未通过，不能进行收款操作！");


    private final int code;

    private final String message;

    TransactionErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
