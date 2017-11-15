/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import com.pos.im.constant.UserJoinSessionType;
import com.pos.im.dto.session.SessionMemberDto;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话成员的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/7/11
 */
public class SessionMember implements Serializable {

    private static final long serialVersionUID = -2534040863760912218L;

    private Long id;

    private Long sessionId; // 会话ID

    private Long userId; // 参与者ID

    private String userType; // 参与者的用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员

    private Byte userJoinType; // 参与者加入类型

    private Long companyId; // B端/业者加入会话时所属的公司ID

    private short callTotal; // 呼叫次数（冗余字段）;

    private Date createTime; // 创建时间

    private Date updateTime; // 更新时间

    private Date lastRenameTime; // 最近一次修改群名称的时间

    private boolean available; // 是否可用，false表示已退出会话

    public boolean isSameUser(UserInfo userInfo) {
        return userId.equals(userInfo.getUserId()) && userType.equals(userInfo.getUserType());
    }

    public boolean isSameUser(SessionMemberDto memberDto) {
        return userId.equals(memberDto.getUserId()) && userType.equals(memberDto.getUserType());
    }

    public boolean isCreator() {
        return UserJoinSessionType.CREATOR.compare(userJoinType);
    }

    public boolean isMembers() {
        return UserJoinSessionType.MEMBERS.compare(userJoinType);
    }

    public boolean isServant() {
        return UserJoinSessionType.SERVANT.compare(userJoinType);
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Byte getUserJoinType() {
        return userJoinType;
    }

    public void setUserJoinType(Byte userJoinType) {
        this.userJoinType = userJoinType;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastRenameTime() {
        return lastRenameTime;
    }

    public void setLastRenameTime(Date lastRenameTime) {
        this.lastRenameTime = lastRenameTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}