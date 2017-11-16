/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;

/**
 * 标识用户的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/12/5
 */
@ApiModel
public class UserIdentifier implements Serializable {

    private static final long serialVersionUID = 6591343792417378075L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户类型：c = C端用户，m = 平台管理员")
    private String userType;

    public UserIdentifier() {
    }

    public UserIdentifier(Long userId, String userType) {
        this.userId = userId;
        this.userType = userType;
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
    }

    @Override
    public String toString() {
        return userType + "#" + userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserIdentifier that = (UserIdentifier) o;

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

}