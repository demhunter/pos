/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.exception;

import com.pos.common.util.exception.ErrorCode;

/**
 * @author wangbing
 * @version 1.0, 2017/03/17
 */
public enum BuildingErrorCode implements ErrorCode {

    BUILDING_NOT_EXIST(1101, "楼盘信息不存在或已被删除"),

    BUILDING_LAYOUT_NOT_EXIST(1102, "户型信息不存在或已被删除");

    private int code;

    private String message;

    BuildingErrorCode(int code, String message) {
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
