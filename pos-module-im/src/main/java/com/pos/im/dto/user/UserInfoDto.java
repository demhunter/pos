/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.user;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;
import java.util.Date;

/**
 * IM用户信息的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/8/8
 */
@ApiModel
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 6516142160732218701L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员")
    private String userType;

    @ApiModelProperty("用户细分类型(int,可空)，取值根据userType而不同，具体参考User Module")
    private Byte userDetailType;

    @ApiModelProperty("用户头衔(可空，目前即用户细分类型的中文描述)")
    private String userTitle;

    @ApiModelProperty("用户在IM中显示的名称")
    private String showName;

    @ApiModelProperty("用户在IM中显示的头像")
    private String showHead;

    @ApiModelProperty("B端/业者所属的公司ID")
    private Long companyId;

    @ApiModelProperty("性别(int)：1 = 男，2 = 女，0 = 保密")
    private byte gender;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("是否公开电话号码")
    private boolean publicPhone;

    @ApiModelProperty("用户追加的问候语")
    private String appendGreeting;

    @ApiModelProperty("用户信息初始化到IM的时间")
    private Date createTime;

    @ApiModelProperty("用户信息最近一次同步到IM的时间")
    private Date updateTime;

    @ApiModelProperty("是否可用")
    private boolean available;

    @ApiModelProperty("用户在第三方IM平台的UID")
    private String imUid;

    public String getImUid() {
        return imUid;
    }

    public void setImUid(String imUid) {
        this.imUid = imUid;
    }

    public UserIdentifier buildUserIdentifier() {
        return new UserIdentifier(userId, userType);
    }

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
        FieldChecker.checkEmpty(showName, fieldPrefix + "showName");
        FieldChecker.checkEmpty(showHead, fieldPrefix + "showHead");
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfoDto that = (UserInfoDto) o;

        if (!userId.equals(that.userId)) return false;
        return userType.equals(that.userType);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + userType.hashCode();
        return result;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public boolean isPublicPhone() {
        return publicPhone;
    }

    public void setPublicPhone(boolean publicPhone) {
        this.publicPhone = publicPhone;
    }

    public String getAppendGreeting() {
        return appendGreeting;
    }

    public void setAppendGreeting(String appendGreeting) {
        this.appendGreeting = appendGreeting;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}