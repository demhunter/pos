/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.vo.response;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class OnlyStringVo implements Serializable {

    private static final long serialVersionUID = 841035637353521345L;

    @ApiModelProperty("返回的结果")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
