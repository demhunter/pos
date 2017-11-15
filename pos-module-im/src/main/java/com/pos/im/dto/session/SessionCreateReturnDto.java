/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session;

import com.pos.common.util.validation.FieldChecker;
import com.pos.im.domain.Session;
import com.pos.im.domain.SessionMember;
import com.pos.im.dto.user.UserInfoDto;

import java.util.List;

/**
 * 群组创建成功Service返回dto
 *
 * @author wangbing
 * @version 1.0, 2017/9/30
 */
public class SessionCreateReturnDto {

    // 会话信息
    private Session session;

    // 会话成员信息
    private List<SessionMember> members;

    // 会话关联家居顾问信息
    private UserInfoDto platformEmployee;

    /**
     * 校验群组创建返回信息
     */
    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(session, fieldPrefix + "session");
        FieldChecker.checkEmpty(members, fieldPrefix + "members");
        FieldChecker.checkEmpty(platformEmployee, fieldPrefix + "platformEmployee");
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<SessionMember> getMembers() {
        return members;
    }

    public void setMembers(List<SessionMember> members) {
        this.members = members;
    }

    public UserInfoDto getPlatformEmployee() {
        return platformEmployee;
    }

    public void setPlatformEmployee(UserInfoDto platformEmployee) {
        this.platformEmployee = platformEmployee;
    }
}
