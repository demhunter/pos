/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.user.constant.UserGender;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.dto.UserIdentifier;

import java.io.Serializable;

/**
 * 用户认证信息DTO.
 *
 * @author wayne
 * @version 1.0, 2017/1/3
 */
public class UserAuthInfoDto implements Serializable {

    private static final long serialVersionUID = -6685563625937193508L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员")
    private String userType;

    @ApiModelProperty("真实姓名")
    private String name;

    @ApiModelProperty("性别(int)：0 = 保密，1 = 男，2 = 女")
    private byte gender;

    @ApiModelProperty("年龄(int)")
    private Byte age;

    @ApiModelProperty("手机号码")
    private String phone;

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

    @JsonIgnore
    public String getSirOrMadam() {
        UserGender ug = UserGender.getEnum(gender);
        if (ug != null) {
            if (ug.equals(UserGender.MALE)) {
                return "先生";
            } else if (ug.equals(UserGender.FEMALE)) {
                return "女士";
            }
        }
        return "";
    }

    public UserIdentifier buildUserIdentifier() {
        return new UserIdentifier(userId, userType);
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

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

}