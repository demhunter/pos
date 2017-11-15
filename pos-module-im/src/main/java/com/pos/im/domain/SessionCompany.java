/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import java.io.Serializable;

/**
 * 与IM会话关联的公司领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/9/19
 */
public class SessionCompany implements Serializable {

    private static final long serialVersionUID = -6400117480812852273L;

    private Long id;

    private Long sessionId;

    private Long companyId;

    private short callTotal; // 呼叫次数（冗余字段）

    private boolean available; // 是否可用，false表示该公司的所有成员被移出会话

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public short getCallTotal() {
        return callTotal;
    }

    public void setCallTotal(short callTotal) {
        this.callTotal = callTotal;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}