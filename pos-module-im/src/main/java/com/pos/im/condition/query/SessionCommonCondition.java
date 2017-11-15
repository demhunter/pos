/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.condition.query;

import java.io.Serializable;

/**
 * 会话的公共查询条件.
 *
 * @author wayne
 * @version 1.0, 2016/8/8
 */
public class SessionCommonCondition implements Serializable {

    private boolean queryMembers; // 是否查询会话的成员列表

    private boolean allMembers; // true表示查询所有成员, false表示只查询当前还在会话里的成员(queryMembers为true有效)

    private Boolean available; // true表示只查询正在进行的会话, false表示只查询已经关闭的会话, null表示查询所有会话

    private Boolean warning; // true表示只查询有风险的会话, false表示只查询无风险的会话, null表示查询所有会话

    public boolean isQueryMembers() {
        return queryMembers;
    }

    public SessionCommonCondition setQueryMembers(boolean queryMembers) {
        this.queryMembers = queryMembers;
        return this;
    }

    public boolean isAllMembers() {
        return allMembers;
    }

    public SessionCommonCondition setAllMembers(boolean allMembers) {
        this.allMembers = allMembers;
        return this;
    }

    public Boolean getAvailable() {
        return available;
    }

    public SessionCommonCondition setAvailable(Boolean available) {
        this.available = available;
        return this;
    }

    public Boolean getWarning() {
        return warning;
    }

    public SessionCommonCondition setWarning(Boolean warning) {
        this.warning = warning;
        return this;
    }

}