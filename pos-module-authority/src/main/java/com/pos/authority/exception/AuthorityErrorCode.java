/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.exception;

import com.pos.common.util.exception.ErrorCode;

/**
 * 权限服务相关错误枚举（code：1000 - 1099）
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public enum AuthorityErrorCode implements ErrorCode {

    UPGRADE_ERROR_TARGET_IS_ARRIVED(1000, "您已达到或超过目标等级，无需晋升！"),

    UPGRADE_ERROR_TARGET_IS_UNREACHABLE(1001, "目标等级不能通过升级达到，请咨询平台客服！"),

    UPGRADE_ERROR_AUDIT_STATUS_FOR_PAY(1002, "身份认证未通过，不能进行付费升级！"),

    UPGRADE_ERROR_CHARGE_NOT_ENOUGH(1003, "支付金额不足以升级到目标等级"),

    AUDIT_STATUS_AUDITED_FOR_UPDATE(1010, "身份认证已通过，不允许修改实名信息"),

    AUDIT_STATUS_NOT_AUDIT_FOR_UPDATE(1011, "身份认证审核中，不允许修改实名信息！");

    private final int code;

    private final String message;

    AuthorityErrorCode(int code, String message) {
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
