/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.v1_0_0;

import com.pos.user.session.UserSession;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 客户信息DTO
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class CustomerDto extends UserDto {

    /* ----------------------customer_base--------------------------*/
    @ApiModelProperty("用户绑定的电子邮箱")
    private String mail;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
    private String headImage;

    @ApiModelProperty("性别(int)：0 = 保密，1 = 男，2 = 女")
    private Integer gender;

    @ApiModelProperty("年龄(int)")
    private Integer age;

    @ApiModelProperty("用户来源类型体系")
    private Integer sourceType;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    /* ----------------------customer_personal--------------------------*/
    @ApiModelProperty("用户真实姓名")
    private String realName;

    @ApiModelProperty("身份证号")
    private String idCardNo;

    @ApiModelProperty("身份证正面照")
    private String idImageA;

    @ApiModelProperty("身份证反面照")
    private String idImageB;

    @ApiModelProperty("身份证本人持证照")
    private String idHoldImage;

    /* ----------------------User Session--------------------------*/
    @ApiModelProperty("用户会话信息")
    private UserSession userSession;

    public CustomerDto() {
    }

    @Override
    public String getShowName() {
        return StringUtils.isEmpty(nickName) ? realName : nickName;
    }

    @Override
    public String getShowHead() {
        return StringUtils.isEmpty(headImage) ? DEFAULT_HEAD_IMAGE : headImage;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
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

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
