/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.exception;

import com.pos.common.util.exception.ErrorCode;

/**
 * IM服务相关的错误码定义（code：501 - 600）.
 *
 * @author wayne
 * @version 1.0, 2016/7/8
 */
public enum IMErrorCode implements ErrorCode {

    GET_TOKEN_FAILED(501, "获取Token失败(IM)"),
    REFRESH_USER_INFO_FAILED(502, "刷新用户信息失败(IM)"),
    SESSION_EXISTED(511, "会话已存在"),
    SESSION_NOT_EXISTED(512, "会话不存在"),
    SESSION_IS_CLOSED(513, "会话已关闭"),
    SESSION_CREATE_FAILED(514, "会话创建失败"),
    SESSION_CLOSE_FAILED(515, "会话关闭失败"),
    SESSION_JOIN_FAILED(516, "会话加入失败"),
    SESSION_QUIT_FAILED(517, "会话退出失败"),
    SESSION_RENAME_FAILED(518, "修改会话名称失败"),
    SESSION_HAS_SERVANT(519, "会话已经有客服在跟进"),
    SESSION_MEMBER_LIMIT(520, "会话成员已达上限"),
    SESSION_UPDATE_FAILED(521, "更新会话信息失败"),
    SESSION_QUIT_FAILED_PE(522, "您是家居顾问，不允许主动退出会话"),
    MEMBER_IS_JOINED(531, "已经加入会话"),
    MEMBER_NOT_JOIN(532, "尚未加入会话"),
    MEMBER_ADD_FAILED(533, "添加成员失败"),
    MEMBER_REMOVE_FAILED(533, "移除成员失败"),
    SESSION_ORDER_EXISTED(541, "会话订单已存在，请等待商家确认"),
    IM_NETEASE_ERROR(542,"失败，%s"),
    USER_IN_JOIN_IM(543,"用户已在会话成员中");

    private final int code;

    private final String message;

    IMErrorCode(final int code, final String message) {
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
