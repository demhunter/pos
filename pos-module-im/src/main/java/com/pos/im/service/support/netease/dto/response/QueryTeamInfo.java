/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.response;

import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class QueryTeamInfo {

    private String tname;

    private String announcement;

    private String owner;

    private int maxusers;

    private int joinmode;

    private int tid;

    private String intro;

    private int size;

    private String custom;

    private boolean mute;

    private List<String> admins;

    private List<String> members;

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public int getJoinmode() {
        return joinmode;
    }

    public void setJoinmode(int joinmode) {
        this.joinmode = joinmode;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
