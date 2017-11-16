/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.vo.token;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 七牛Token
 *
 * @author wangbing
 * @version 1.0, 2017/11/16
 */
public class QiNiuTokenVo implements Serializable {

    private static final long serialVersionUID = 6354020066280261614L;

    @ApiModelProperty("七牛上传uptoken")
    private String uptoken;

    public String getUptoken() {
        return uptoken;
    }

    public void setUptoken(String uptoken) {
        this.uptoken = uptoken;
    }
}
