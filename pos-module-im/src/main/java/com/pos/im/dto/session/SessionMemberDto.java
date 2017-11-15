/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.im.service.IMUserService;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.im.constant.UserJoinSessionType;

import java.io.Serializable;

/**
 * 会话成员DTO.
 *
 * @author wayne
 * @version 1.0, 2016/7/12
 */
public class SessionMemberDto implements Serializable {

    private static final long serialVersionUID = -2033589913426433688L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员")
    private String userType;

    @ApiModelProperty("用户加入类型(int): 1 = 会话发起者, 2 = 会话跟踪者(客服), 3 = 其他参与者")
    private byte userJoinType;

    @ApiModelProperty("用户细分类型(可空，int)，取值根据userType而不同，具体参考User Module")
    private Byte userDetailType;

    @ApiModelProperty("用户头衔(可空，目前即用户细分类型的中文描述)")
    private String userTitle;

    @ApiModelProperty("用户在IM中显示的名称")
    private String showName;

    @ApiModelProperty("用户真实姓名")
    private String realName;

    @ApiModelProperty("用户在IM中显示的头像")
    private String showHead;

    @ApiModelProperty("性别(int)：1 = 男，2 = 女，0 = 保密")
    private byte gender;

    @ApiModelProperty("联系电话(不留电话或者不公开都可能为空)")
    private String phone;

    @ApiModelProperty("呼叫次数")
    private short callTotal;

    @ApiModelProperty("是否可用，false表示已退出会话")
    private boolean available;

    @ApiModelProperty("B端/业者加入会话时所属的公司ID")
    private Long companyId;

    @ApiModelProperty("B端/业者加入会话时所属的公司名称")
    private String companyName;

    @ApiModelProperty("B端/业者所属公司是否启用IM短信")
    private Boolean companySmsEnable;

    @ApiModelProperty("B端/业者所属公司是否启用IM通知")
    private Boolean companyNoticeEnable;

    @ApiModelProperty("用户在第三方IM平台的UID")
    private String imUid;

    @JsonIgnore
    private Long sessionId; // 关联的会话ID，批量查询时作比较用

    @JsonIgnore
    private boolean publicPhone; // 是否公开联系方式

    /**
     * 检查字段完整性.
     *
     * @param fieldPrefix 抛出异常时提示错误字段的前缀名, 可以为空
     * @throws ValidationException 未设置不能为空的字段, 或者字段值不符合约定
     */
    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(userId, fieldPrefix + "userId");
        FieldChecker.checkEmpty(userType, fieldPrefix + "userType");
        FieldChecker.checkEmpty(showName, fieldPrefix + "showName"); // 加入会话后推送消息需要显示成员昵称

        // 业者加入会话时必须传入的属性检测
        if (userIsEmployee()) {
            FieldChecker.checkEmpty(phone, fieldPrefix + "phone");
            FieldChecker.checkEmpty(userTitle, fieldPrefix + "userTitle");
            FieldChecker.checkEmpty(companyId, fieldPrefix + "companyId");
        }
    }

    public UserIdentifier buildUserIdentifier() {
        return new UserIdentifier(userId, userType);
    }

    /**
     * 是否接收短信通知.
     */
    @JsonIgnore
    public boolean isReceiveSms() {
        return userIsEmployee() && (companyId == null
                || (companySmsEnable != null
                && companySmsEnable.equals(Boolean.TRUE)));
    }

    /**
     * 是否接收站内信通知.
     */
    @JsonIgnore
    public boolean isReceiveNotice() {
        return userIsEmployee() && (companyId == null
                || (companyNoticeEnable != null
                && companyNoticeEnable.equals(Boolean.TRUE)));
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionMemberDto that = (SessionMemberDto) o;

        if (!userId.equals(that.userId)) return false;
        return userType.equals(that.userType);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + userType.hashCode();
        return result;
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

    public boolean userIsCustomer() {
        return IMUserService.USER_TYPE_CUSTOMER.equals(userType);
    }

    public boolean userIsEmployee() {
        return IMUserService.USER_TYPE_EMPLOYEE.equals(userType);
    }

    public String getImUid() {
        return imUid;
    }

    public void setImUid(String imUid) {
        this.imUid = imUid;
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

    public byte getUserJoinType() {
        return userJoinType;
    }

    public void setUserJoinType(byte userJoinType) {
        this.userJoinType = userJoinType;
    }

    public Byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(Byte userDetailType) {
        this.userDetailType = userDetailType;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Boolean getCompanySmsEnable() {
        return companySmsEnable;
    }

    public void setCompanySmsEnable(Boolean companySmsEnable) {
        this.companySmsEnable = companySmsEnable;
    }

    public Boolean getCompanyNoticeEnable() {
        return companyNoticeEnable;
    }

    public void setCompanyNoticeEnable(Boolean companyNoticeEnable) {
        this.companyNoticeEnable = companyNoticeEnable;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isPublicPhone() {
        return publicPhone;
    }

    public void setPublicPhone(boolean publicPhone) {
        this.publicPhone = publicPhone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}



