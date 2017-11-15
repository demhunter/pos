/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.request;

/**
 * @author 睿智
 * @version 1.0, 2017/9/7
 */
public class TeamAddRequestDto {

    private String tid;//网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
    private String owner;//群主用户帐号，最大长度32字符
    private String members;//["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，一次最多拉200个成员
    private int magree;//管理后台建群时，0不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群。其它会返回414
    private String msg;//邀请发送的文字，最大长度150字符
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

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public int getMagree() {
        return magree;
    }

    public void setMagree(int magree) {
        this.magree = magree;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }
}
