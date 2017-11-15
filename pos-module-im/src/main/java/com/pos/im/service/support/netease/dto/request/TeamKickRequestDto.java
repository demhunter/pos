/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.request;

/**
 * @author 睿智
 * @version 1.0, 2017/9/7
 */
public class TeamKickRequestDto {

    private String tid;//网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
    private String owner;//群主的accid，用户帐号，最大长度32字符
    private String member;//被移除人的accid，用户账号，最大长度字符
    private String attach;//自定义扩展字段，最大长度512

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

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }
}
