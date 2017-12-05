/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto;

/**
 * @author 睿智
 * @version 1.0, 2017/9/13
 */
public class WechatAccessTokenDto {

    private int errcode; // 0:请求成功,其它失败

    private String errmsg; // 错误信息

    private String access_token; // 获取到的凭证

    private int expires_in; // 凭证有效时间，单位：秒

    // 获取微信access_token是否成功
    public boolean accessTokenRequestSucc() {
        return errcode == 0;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
