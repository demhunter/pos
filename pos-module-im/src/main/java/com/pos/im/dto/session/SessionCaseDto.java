/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session;

import java.io.Serializable;

/**
 * 会话作品关联Dto
 *
 * @author wangbing
 * @version 1.0, 2017/9/15
 */
public class SessionCaseDto implements Serializable {

    private static final long serialVersionUID = -9203882247876748824L;

    private Long id;

    private Long sessionId; // 会话ID

    private String groupId; // 群组通信id

    private Long caseId; // 会话关联的案例ID

    private String caseName; // 作品名称，冗余字段且不更新

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
