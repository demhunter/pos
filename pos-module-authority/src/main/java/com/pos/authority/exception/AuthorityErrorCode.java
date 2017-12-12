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

    UPGRADE_ERROR_LEVEL_LESS_THAN_CURRENT(1004, "目前等级只能晋升，不能降低"),

    UPGRADE_ERROR_GREATER_THAN_MAX(1005, "晋升等级不能超过当前系统的最大等级"),

    UPGRADE_ERROR_RATE_LESS_THAN_LIMIT(1006, "提现手续费率不能低于最低下限"),

    UPGRADE_ERROR_EXTRA_LESS_THAN_LIMIT(1007, "提现额外手续费不能低于最低下限"),

    AUDIT_STATUS_ERROR_AUDITED_FOR_UPDATE(1010, "实名认证已通过，不允许修改实名信息"),

    AUDIT_STATUS_ERROR_NOT_AUDIT_FOR_UPDATE(1011, "实名认证审核中，不允许修改实名信息！"),

    AUDIT_STATUS_ERROR_NOU_SUBMIT_FOR_AUTHORIZE(1012, "实名认证信息未提交！"),

    AUDIT_STATUS_ERROR_IDENTITY_DATED(1013, "用户实名认证信息已过时，请刷新过后重新审核！");

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
