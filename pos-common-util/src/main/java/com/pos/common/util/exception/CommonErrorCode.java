/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.exception;

/**
 * 公共错误码定义（code：1 - 100）.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public enum CommonErrorCode implements ErrorCode {

    REQUIRED_PARAM(1, "缺少必需的参数"),
    ILLEGAL_PARAM(2, "不合法的参数"),
    VALIDATION_ERROR(3, "验证不通过"),
    ACCESS_TIMEOUT(4, "访问超时"),
    UNSUPPORTED_OPERATION(5, "不支持的操作"),
    NO_PERMISSIONS(6, "没有权限执行此操作"),
    SMS_SEND_FAILED(7, "短信发送失败"),
    VERIFY_CODE_ERROR(11, "验证码错误"),
    VERIFY_CODE_EXPIRED(12, "验证码过期"),
    HTTP_REQUEST_ERROR(13, "Http请求错误"),
    CACHE_OPERATE_ERROR(14, "缓存操作失败"),
    INVALID_INVITATION_CODE(15, "无效的邀请码"),
    MODIFY_NOT_CHANGED(21, "修改前后没有变化"),
    DATA_NOT_INITIALIZED(22, "数据尚未初始化"),
    DATA_NOT_FOUND(23, "没有找到匹配的数据"),
    MODIFY_ONLY_ONE(24, "只能修改一次，请勿重复提交"),
    NO_EMOJI_INPUT(31, "不能输入表情符号"),
    FILE_UPLOAD_FAILED(32, "文件上传失败"),
    VERSION_ERROR(41, "版本号错误"),
    VERSION_NOT_FOUND(42, "服务器未配置版本信息"),
    VERSION_NOT_BETA(43, "没有可供测试的版本"),
    VERSION_TOO_LOW(44, "版本过低，请先升级"),
    SUCC_NO_REPEAT_OP(45, "已成功，请勿重复提交"),
    ILLEGAL_STATE(46, "状态错误"),
    DATA_ERROR(47, "数据错误"),
    URL_ERROR(48, "URL资源定位错误"),
    PHONE_NUMBER_ERROR(49, "电话号码格式错误"),
    HTTP_REQUEST_TIMEOUT(51, "请求超时"),
    HTTP_RESPONSE_TIMEOUT(52, "请求响应超时");

    private final int code;

    private final String message;

    CommonErrorCode(final int code, final String message) {
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
