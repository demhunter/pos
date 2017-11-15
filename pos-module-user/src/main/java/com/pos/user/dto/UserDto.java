/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.pos.user.constant.UserGender;
import com.pos.user.constant.UserType;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;

import java.util.Date;

/**
 * 用户信息DTO的抽象基类.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public abstract class UserDto {

    public static final String DEFAULT_HEAD_IMAGE = "http://o8nljewkg.bkt.clouddn.com/o_1ao3jmbhk11qj1q031k9s10685qac.png?width=256&height=256";

    @ApiModelProperty("用户编号")
    private Long id;

    @ApiModelProperty("登录账号名，如果注册时使用的是手机号，则默认为手机号")
    private String userName;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("登录手机号，如果注册时使用的是账号名，则默认为账号名")
    private String userPhone;

    @ApiModelProperty("用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员")
    private String userType;

    @JsonIgnore
    private Long userTypeId; // 用户类型ID，对象转换时需要该属性

    @ApiModelProperty("是否删除")
    private Boolean deleted;

    @ApiModelProperty("是否可用")
    private boolean available;

    @ApiModelProperty("真实姓名")
    private String name;

    @ApiModelProperty("性别(int)：0 = 保密，1 = 男，2 = 女")
    private Byte gender;

    @ApiModelProperty("年龄(int)")
    private Byte age;

    @ApiModelProperty("电子邮箱")
    private String mail;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("身份证正面照")
    private String idImageA;

    @ApiModelProperty("身份证背面照")
    private String idImageB;

    @ApiModelProperty("身份证持证正面照")
    private String idHoldImage;

    @ApiModelProperty("身份证持证背面照")
    private String idHoldImageB;

    @ApiModelProperty("创建人UID，如果等于userId，表示自主注册")
    private Long createUserId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("最近一次登录时间")
    private Date lastLoginTime;

    /**
     * 登录IP
     */
    @ApiModelProperty("用户登录IP")
    private String loginIp;

    /**
     * 登录IP所在地
     */
    @ApiModelProperty("用户登录IP所在地")
    private String loginAddress;

    /**
     * 注册IP
     */
    @ApiModelProperty("用户注册IP")
    private String registerIp;

    /**
     * 注册IP说在地
     */
    @ApiModelProperty("注册IP所在地")
    private String registerAddress;

    @ApiModelProperty(value = "用户显示给其他人看到的头像")
    public String getShowHead() {
        return !Strings.isNullOrEmpty(getHeadImage()) ? getHeadImage() : DEFAULT_HEAD_IMAGE;
    }

    @ApiModelProperty(value = "用户显示给其他人看到的名称")
    public abstract String getShowName();

    @ApiModelProperty("用户在第三方IM平台的UID（此字段视情况返回，与IM相关时此字段才会返回，其它获取用户信息时此字段可不返回）")
    private String imUid;

    public abstract String getHeadImage();

    public UserIdentifier buildUserIdentifier() {
        return new UserIdentifier(id, userType);
    }

    /**
     * 获取一个默认的UserDto实现类, 当调用需要子类实现的方法时会抛出UnsupportedOperationException.
     *
     * @return
     */
    public static UserDto getDefault() {
        return new UserDto() {
            @Override
            public String getShowName() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String getHeadImage() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * 检查字段完整性.
     *
     * @param fieldPrefix 抛出异常时提示错误字段的前缀名, 可以为空
     * @throws ValidationException 未设置不能为空的字段, 或者字段值不符合约定
     */
    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(id, fieldPrefix + "id");
        FieldChecker.checkEmpty(userTypeId, fieldPrefix + "userTypeId");
        FieldChecker.checkEmpty(createTime, fieldPrefix + "createTime");
        FieldChecker.checkEmpty(createUserId, fieldPrefix + "createUserId");

        if (parseGender() == null) {
            throw new ValidationException("'" + fieldPrefix + "gender'无效值");
        }
        if (parseUserType() == null) {
            throw new ValidationException("'" + fieldPrefix + "userType'无效值");
        }

        Validator.checkUserName(userName);
        Validator.checkMobileNumber(userPhone);
        if (!Strings.isNullOrEmpty(name)) {
            Validator.checkCnName(name);
        }
        if (!Strings.isNullOrEmpty(idCard)) {
            Validator.checkIdNumber(idCard);
        }
    }

    public UserType parseUserType() {
        return UserType.getEnum(userType);
    }

    public UserGender parseGender() {
        return UserGender.getEnum(gender);
    }

    public String getImUid() {
        return imUid;
    }

    public void setImUid(String imUid) {
        this.imUid = imUid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdImageA() {
        return idImageA;
    }

    public void setIdImageA(String idImageA) {
        this.idImageA = idImageA;
    }

    public String getIdImageB() {
        return idImageB;
    }

    public void setIdImageB(String idImageB) {
        this.idImageB = idImageB;
    }

    public String getIdHoldImage() {
        return idHoldImage;
    }

    public void setIdHoldImage(String idHoldImage) {
        this.idHoldImage = idHoldImage;
    }

    public String getIdHoldImageB() {
        return idHoldImageB;
    }

    public void setIdHoldImageB(String idHoldImageB) {
        this.idHoldImageB = idHoldImageB;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLoginAddress() {
        return loginAddress;
    }

    public void setLoginAddress(String loginAddress) {
        this.loginAddress = loginAddress;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }
}