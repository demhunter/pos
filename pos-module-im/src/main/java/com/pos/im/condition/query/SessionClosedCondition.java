/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.condition.query;

import com.pos.im.constant.SessionClosedReason;

import java.io.Serializable;

/**
 * 已关闭会话的查询条件.
 *
 * @author wayne
 * @version 1.0, 2016/12/7
 */
public class SessionClosedCondition implements Serializable {

    private Long companyId; // 会话所属的公司ID(可空)

    private SessionClosedReason reason; // 会话关闭原因(可空)

    private Boolean warning; // true表示只查询有风险的会话, false表示只查询无风险的会话, null表示查询所有会话

    public Byte getReasonValue() {
        return reason != null ? reason.getCode() : null;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public SessionClosedCondition setCompanyId(Long companyId) {
        this.companyId = companyId;
        return this;
    }

    public SessionClosedReason getReason() {
        return reason;
    }

    public SessionClosedCondition setReason(SessionClosedReason reason) {
        this.reason = reason;
        return this;
    }

    public Boolean getWarning() {
        return warning;
    }

    public SessionClosedCondition setWarning(Boolean warning) {
        this.warning = warning;
        return this;
    }

}