/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.request;

/**
 * @author 睿智
 * @version 1.0, 2017/9/7
 */
public class TeamRemoveRequestDto {

    private String tid;//网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符

    private String owner;//群主用户帐号，最大长度32字符

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
