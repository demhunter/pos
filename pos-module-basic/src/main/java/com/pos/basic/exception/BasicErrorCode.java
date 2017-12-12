/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.exception;

import com.pos.common.util.exception.ErrorCode;

/**
 * 基础服务相关的错误码定义（code：101 - 200）.
 *
 * @author wayne
 * @version 1.0, 2016/6/15
 */
public enum BasicErrorCode implements ErrorCode {

    BRAND_NOT_FOUND(101, "品牌不存在"),
    BRAND_EXISTED(102, "品牌已经存在"),

    QINIU_UPLOAD_ERROR(103, "上传失败"),

    QINIU_UPLOAD_IMAGE_ERROR(104, "图片上传失败"),

    POPULARIZATION_DOCUMENT_NOT_EXISTED(110, "推广文案不存在");

    private final int code;

    private final String message;

    BasicErrorCode(final int code, final String message) {
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
