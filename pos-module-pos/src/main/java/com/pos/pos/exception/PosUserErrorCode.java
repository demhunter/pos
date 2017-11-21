/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.exception;

import com.pos.common.util.exception.ErrorCode;

/**
 * 快捷收款错误码定义
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
public enum PosUserErrorCode implements ErrorCode {

    CHANNEL_NOT_EXISTED(1801, "渠道商不存在"),

    CHANNEL_APPLY_MONEY_ERROR(1802, "提现金额错误"),

    CURRENT_APPLY_MONEY_NOT_EMPTY(1803, "还有待处理的提现申请，不能再次申请提现"),

    CURRENT_APPLY_MONEY_LESS_THAN_TEN(1804, "当前可提现金额小于10元，不能申请提现"),

    DATA_EXCEPTION(1805, "数据异常"),

    AUDIT_STATUS_ERROR(1806, "身份认证已通过，信息不允许修改"),

    NOT_AUDIT_STATUS_ERROR(1807, "身份认证审核中，信息不允许修改"),

    NOT_SUBMIT_STATUS_ERROR(1808, "身份认证信息还未提交"),

    IDENTITY_INFO_ERROR(1809, "身份证信息尚未提交"),

    IDENTITY_INFO_REFRESHED_ERROR(1810, "用户身份认证信息已过时，请刷新过后重新审核"),

    NOT_SUBMIT_ERROR_FOR_GET(1811, "身份认证信息还未提交，不能进行收款操作，请先提交身份认证信息"),

    PERMISSION_GET_ERROR(1812, "暂无快捷收款权限"),

    BANK_CARD_NOT_EXISTED(1813, "银行卡信息不存在"),

    TRANSACTION_RECORD_NOT_EXISTED(1814, "交易记录不存在"),

    TRANSACTION_DUPLICATE_SMS_CODE(1815, "交易已处理，请勿重新获取短信验证码"),

    TRANSACTION_PROGRESSED_ERROR(1816, "交易已处理，请勿重复操作"),

    TWITTER_PERMISSION_ERROR_FOR_BROKERAGE(1817, "推客权限不足，无法获取佣金信息"),

    TWITTER_PERMISSION_ERROR_FOR_SPREAD(1818, "推客权限不足，无法获取发展收款客户信息"),

    TWITTER_PERMISSION_ERROR_FOR_DEVELOP(1819, "推客权限不足，无法获取发展下级推客信息"),

    TRANSACTION_STATUS_ERROR(1820, "交易状态错误，不能执行此操作"),

    WECHAT_SIGN_ERROR_FOR_ACCESS_TOKEN(1821, "获取微信Sign失败"),

    WECHAT_ERROR_FOR_ACCESS_TOKEN(1822, "获取微信access token失败"),

    WECHAT_ERROR_UPLOAD_IMAGE(1823, "图片上传失败"),

    REJECTED_ERROR_FOR_GET(1824, "身份认证信息审核未通过，不能进行收款操作，请重新身份认证信息");

    private final int code;

    private final String message;

    PosUserErrorCode(final int code, final String message) {
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
