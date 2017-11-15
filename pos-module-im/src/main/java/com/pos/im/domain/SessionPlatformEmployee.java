/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import java.io.Serializable;

/**
 * IM会话与家居顾问的关联关系
 * @author 睿智
 * @version 1.0, 2017/7/4
 */
public class SessionPlatformEmployee implements Serializable {

    private static final long serialVersionUID = 5867625959506674841L;

    private Long id;

    private Long sessionId;//会话的ID

    private Long peUserId;//家居顾问的userid

    private boolean joined;//是否已经加入会话

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getPeUserId() {
        return peUserId;
    }

    public void setPeUserId(Long peUserId) {
        this.peUserId = peUserId;
    }
}
