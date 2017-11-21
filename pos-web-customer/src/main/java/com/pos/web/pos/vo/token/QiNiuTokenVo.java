/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.token;

import java.io.Serializable;

/**
 * 七牛Token'
 *
 * @author wangbing
 * @version 1.0, 2017/11/15
 */
public class QiNiuTokenVo implements Serializable {

    private String uptoken;

    public String getUptoken() {
        return uptoken;
    }

    public void setUptoken(String uptoken) {
        this.uptoken = uptoken;
    }
}
