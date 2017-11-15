/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import com.pos.basic.dto.UserIdentifier;
import com.pos.im.constant.SessionClosedReason;

import java.io.Serializable;
import java.util.Date;

/**
 * IM会话领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/11/23
 */
public class Session implements Serializable {

    private static final long serialVersionUID = -2540190024286406128L;

    private Long id; // 会话ID，自增ID

    private String groupId; // 对接IM平台的群组ID，最大128位字符

    private Long userId; // 发起会话的用户ID

    private String userType; // 发起会话的用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员

    private String name; // 会话名称，同时也是对接IM平台的群组名称

    private short callTotal; // 呼叫次数（冗余字段）

    private byte warning; // 会话内容是否有风险

    private boolean available; // 是否可用，false表示会话关闭

    private Long closedUserId; // 关闭会话的操作人ID（系统清理的为空）

    /**
     * 会话关闭的原因
     *
     * @see SessionClosedReason
     */
    private byte closedReason;

    private Date createTime;

    private Date updateTime;

    private Long updateUserId;

    private Long firstMsgUserId; // 会话第一条消息的发起人ID（系统自动发的不算）

    private String exclusiveTwitter; // 如果创建会话时客户有关联推客，且该推客是会话关联公司的业者，则记录专属推客的基本信息：userId,userType,name

    public boolean isCreator(UserIdentifier user) {
        return userId.equals(user.getUserId()) && userType.equals(user.getUserType());
    }

    public boolean isCreator(Long userId, String userType) {
        return this.userId.equals(userId) && this.userType.equals(userType);
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getCallTotal() {
        return callTotal;
    }

    public void setCallTotal(short callTotal) {
        this.callTotal = callTotal;
    }

    public byte getWarning() {
        return warning;
    }

    public void setWarning(byte warning) {
        this.warning = warning;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getClosedUserId() {
        return closedUserId;
    }

    public void setClosedUserId(Long closedUserId) {
        this.closedUserId = closedUserId;
    }

    public byte getClosedReason() {
        return closedReason;
    }

    public void setClosedReason(byte closedReason) {
        this.closedReason = closedReason;
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

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Long getFirstMsgUserId() {
        return firstMsgUserId;
    }

    public void setFirstMsgUserId(Long firstMsgUserId) {
        this.firstMsgUserId = firstMsgUserId;
    }

    public String getExclusiveTwitter() {
        return exclusiveTwitter;
    }

    public void setExclusiveTwitter(String exclusiveTwitter) {
        this.exclusiveTwitter = exclusiveTwitter;
    }

}