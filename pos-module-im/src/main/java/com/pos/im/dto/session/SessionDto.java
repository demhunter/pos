/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * IM会话DTO的抽象基类.
 *
 * @author wayne
 * @version 1.0, 2016/11/23
 */
@ApiModel
public class SessionDto implements Serializable {

    private static final long serialVersionUID = 6784845635652310054L;

    @ApiModelProperty("会话ID，自增id")
    private Long id;

    @ApiModelProperty("v1.9.0 * 更换IM服务商后对接IM平台的群组ID")
    private String groupId;

    @ApiModelProperty("发起会话的用户ID")
    private Long userId;

    @ApiModelProperty("发起会话的用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员")
    private String userType;

    @ApiModelProperty("会话名称，同时也是对接IM平台的群组名称")
    private String name;

    @ApiModelProperty("呼叫次数")
    private short callTotal;

    @ApiModelProperty("会话内容是否有风险")
    private boolean warning;

    @ApiModelProperty("是否可用，false表示会话关闭")
    private boolean available;

    @ApiModelProperty("关闭会话的操作人ID（系统清理的为空）")
    private Long closedUserId;

    @ApiModelProperty("会话关闭的原因(int)：1 = 用户关闭会话，2 = 客服关闭会话，3 = 清理临时会话，4 = 清理不活跃的会话")
    private byte closedReason;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("会话成员列表")
    private List<SessionMemberDto> members;

    @ApiModelProperty("会话关联的案例ID")
    private Long relatedCaseId;

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
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

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
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

    public List<SessionMemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<SessionMemberDto> members) {
        this.members = members;
    }

    public Long getRelatedCaseId() {
        return relatedCaseId;
    }

    public void setRelatedCaseId(Long relatedCaseId) {
        this.relatedCaseId = relatedCaseId;
    }

}