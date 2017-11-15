/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain.v1_0_0;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户基础信息领域对象
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class Customer implements Serializable {

    private Long id;

    private Long userId; // 用户id

    private String mail; // 用户绑定的电子邮箱

    private String nickName; // 用户昵称

    private String headImage; // 用户头像

    private Integer gender; // 性别(int)：0 = 保密，1 = 男，2 = 女

    private Integer age; // 年龄(int)

    private Integer sourceType; // 用户来源类型体系

    private Date updateTime; // 更新时间

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
}
