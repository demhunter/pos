/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.response;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/9/6
 */
public class OnlyCodeResponseDto implements Serializable {

    private static final long serialVersionUID = 8466296467347199696L;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
