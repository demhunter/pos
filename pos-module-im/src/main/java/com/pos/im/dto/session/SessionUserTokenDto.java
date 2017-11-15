/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session;

/**
 * @author 睿智
 * @version 1.0, 2017/10/25
 */
public class SessionUserTokenDto {

    private String token;

    private String uid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
