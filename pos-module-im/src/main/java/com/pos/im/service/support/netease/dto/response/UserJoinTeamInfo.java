/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.response;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class UserJoinTeamInfo {

    private String owner;
    private String tname;
    private int maxusers;
    private String tid;
    private int size;
    private String custom;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }
}
