/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.message;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class SingleMsgOption {

    private boolean roam;//: 该消息是否需要漫游，默认true（需要app开通漫游消息功能）； 
    private boolean history;// 该消息是否存云端历史，默认true；
    private boolean sendersync;// 该消息是否需要发送方多端同步，默认true；
    private boolean push;// 该消息是否需要APNS推送或安卓系统通知栏推送，默认true；
    private boolean route;// 该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能);
    private boolean badge;//该消息是否需要计入到未读计数中，默认true;
    private boolean needPushNick;// 推送文案是否需要带上昵称，不设置该参数时默认true;
    private boolean persistent;// 是否需要存离线消息，不设置该参数时默认true。

    public boolean isRoam() {
        return roam;
    }

    public void setRoam(boolean roam) {
        this.roam = roam;
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    public boolean isSendersync() {
        return sendersync;
    }

    public void setSendersync(boolean sendersync) {
        this.sendersync = sendersync;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public boolean isRoute() {
        return route;
    }

    public void setRoute(boolean route) {
        this.route = route;
    }

    public boolean isBadge() {
        return badge;
    }

    public void setBadge(boolean badge) {
        this.badge = badge;
    }

    public boolean isNeedPushNick() {
        return needPushNick;
    }

    public void setNeedPushNick(boolean needPushNick) {
        this.needPushNick = needPushNick;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
}
