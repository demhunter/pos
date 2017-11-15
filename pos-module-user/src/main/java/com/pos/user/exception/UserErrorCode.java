/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.exception;

import com.pos.common.util.exception.ErrorCode;

/**
 * 用户服务相关的错误码定义（code：301 - 400）.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public enum UserErrorCode implements ErrorCode {

    USER_EXISTED(301, "用户已存在"),
    USER_NOT_EXISTED(302, "用户不存在"),
    USER_NOT_AVAILABLE(303, "用户已失效"),
    ACCOUNT_DELETED(304, "账户已被禁用"),
    USER_NOT_LOGIN(312, "用户未登录"),
    FIRST_LOGIN_VERIFY(313, "首次登录需要进行身份验证或重置密码"),
    USER_TOKEN_EXPIRED(314, "用户令牌已过期"),
    USER_TOKEN_REFRESHED(315, "用户令牌被刷新"),
    USER_TOKEN_CANT_REFRESH(316, "用户令牌未到刷新时间"),
    USER_TOKEN_LOGGED_OTHER(317, "用户在其它设备登录"),
    USER_PWD_DUPLICATE(321, "用户名密码重复"),
    USER_OR_PWD_ERROR(322, "用户名或密码错误"),
    OLD_PWD_ERROR(323, "旧密码错误"),
    USER_AND_PHONE_ERROR(324, "用户名与手机号不匹配"),
    USER_REGISTER_FAILED(325, "用户注册失败"),
    CONFIRM_TIMEOUT(326, "确认操作超时"),
    HAS_EMPLOYEE_NO_BUSINESS(327, "手机号已注册为业者账户，不能注册为商家管理员"),
    HAS_BUSINESS_NO_EMPLOYEE(328, "手机号已注册为商家管理员，不能注册为业者账户"),
    NO_LOGIN_CONTACT_EMPLOYER(329, "无法登录，请联系您的雇主处理"),
    NO_LOGIN_CONTACT_CUSTOMER_SERVICE(330, "无法登录，请联系鹦鹉美家客服"),
    MERCHANT_PHONE_USED(331, "管理员手机号已被占用"),
    USER_NOT_REGISTERED_E(332, "手机号码%s没有注册，请联系公司录入"),
    USER_NOT_REGISTERED_C(333, "手机号码%s没有注册，返回注册页面注册"),
    RESET_PASSWORD_FAILED(334, "重置密码失败"),
    NO_BUSINESS(335, "不存在B端账号"),
    TOKEN_INVALID(336, "访问已超时"),
    NOT_COLLECTED_EMPLOYEE(351, "尚未收藏过该业者"),
    USER_COLLECTED_EMPLOYEE(352, "已收藏过该业者"),
    USER_NOT_CUSTOMER(353, "该用户还未注册成为C端客户"),
    USER_NOT_COMPANY(354, "该业者没有对应公司信息，无法定位"),
    HAS_MANAGER_NO_EMPLOYEE(355, "手机号已注册为平台管理员，不能注册为业者账户"),
    USER_ALREADY_EMPLOYEE(356, "该账号属于%s在职员工，无法挂靠"),
    USER_HAS_CASE_INFO(357, "该业者存在需要更换员工绑定关系的作品的修改信息未上传"),
    RESULT_COUNT_TO_MORE(358, "返回的查询结果信息过多，请输入更精确的查询信息");

    private final int code;

    private final String message;

    UserErrorCode(final int code, final String message) {
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
