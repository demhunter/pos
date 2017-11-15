/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import com.pos.user.constant.UserGender;

import java.io.Serializable;

/**
 * 用户领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/6/2
 */
public class User implements Serializable {

    public Long id;

    private String userName; // 登录账号名，如果注册时使用的是手机号，则默认为手机号

    private String userPhone; // 登录手机号，如果注册时使用的是账号名，则默认为账号名

    private String password;

    private Boolean deleted; // 是否删除

    private String mail; // 电子邮箱

    private String name; // 真实姓名

    private Byte gender; // 性别

    private Byte age; // 年龄

    private String idCard; // 身份证号

    private String idImageA; // 身份证正面照

    private String idImageB; // 身份证背面照

    private String idHoldImage; // 身份证持证正面照

    private String idHoldImageB; // 身份证持证背面照

    public String acquireGenderAddress() {
        if (gender == UserGender.MALE.getCode()) {
            return "先生";
        } else if (gender == UserGender.FEMALE.getCode()) {
            return "女士";
        } else {
            return "";
        }
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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