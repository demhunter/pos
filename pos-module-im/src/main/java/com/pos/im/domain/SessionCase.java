/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import java.io.Serializable;

/**
 * IM会话关联的案例领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/7/11
 */
public class SessionCase implements Serializable {

    private static final long serialVersionUID = -3507519103901275266L;

    private Long id;

    private Long sessionId; // 会话ID

    private Long caseId; // 会话关联的案例ID

    private String caseName; // 作品名称，冗余字段且不更新

    private String groupId; // 会话群组id

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

}