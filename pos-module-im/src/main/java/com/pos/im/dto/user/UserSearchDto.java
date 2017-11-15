/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.user;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.dto.UserIdentifier;

import java.io.Serializable;

/**
 * 在IM中搜索用户的结果集DTO.
 *
 * @author wayne
 * @version 1.0, 2016/12/16
 */
@ApiModel
public class UserSearchDto implements Serializable {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员")
    private String userType;

    @ApiModelProperty("用户头衔(可空，目前即用户细分类型的中文描述)")
    private String userTitle;

    @ApiModelProperty("用户在IM中显示的名称")
    private String showName;

    @ApiModelProperty("用户在IM中显示的头像")
    private String showHead;

    @ApiModelProperty("性别(int)：1 = 男，2 = 女，0 = 保密")
    private byte gender;

    @ApiModelProperty("B端/业者所属的公司ID")
    private Long companyId;

    @ApiModelProperty("B端/业者所属的公司名称")
    private String companyName;

    @ApiModelProperty("是否已加入会话")
    private boolean joinSession;

    @ApiModelProperty("用户在第三方IM平台的UID")
    private String imUid;

    public void setImUid(String imUid) {
        this.imUid = imUid;
    }

    public String getImUid() {
        return imUid;
    }

    public boolean equalsUser(UserIdentifier user) {
        return userId.equals(user.getUserId()) && userType.equals(user.getUserType());
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

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowHead() {
        return showHead;
    }

    public void setShowHead(String showHead) {
        this.showHead = showHead;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isJoinSession() {
        return joinSession;
    }

    public void setJoinSession(boolean joinSession) {
        this.joinSession = joinSession;
    }

}