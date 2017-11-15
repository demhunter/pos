/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.user;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户获取的IM Token DTO.
 *
 * @author wayne
 * @version 1.0, 2016/7/7
 */
@ApiModel
public class UserTokenDto implements Serializable {

    private static final long serialVersionUID = -8136662966445985125L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员")
    private String userType;

    @ApiModelProperty("用户访问IM的令牌")
    private String token;

    @ApiModelProperty("用户在第三方IM平台的UID")
    private String imUid;

    public UserTokenDto() {
    }

    public UserTokenDto(Long userId, String userType, String token) {
        this.userId = userId;
        this.userType = userType;
        this.token = token;
    }

    public void setImUid(String imUid) {
        this.imUid = imUid;
    }

    public String getImUid() {
        return imUid;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}