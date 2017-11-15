/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

/**
 * 用户登录信息
 *
 * @author lifei
 * @version 1.0, 2017/7/26
 */
public class SessionStatics extends Session{

    //用户登陆信息
    private String loginInfo;

    public String getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(String loginInfo) {
        this.loginInfo = loginInfo;
    }
}
