/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.response;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/9/6
 */
public class CreateResponseDto implements Serializable{

    private static final long serialVersionUID = -7504113127259038159L;

    private String code;

    private CreateInfo info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CreateInfo getInfo() {
        return info;
    }

    public void setInfo(CreateInfo info) {
        this.info = info;
    }
}
