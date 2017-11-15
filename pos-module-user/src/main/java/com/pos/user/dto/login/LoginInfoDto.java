/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.login;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户登录信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class LoginInfoDto implements Serializable {

    @ApiModelProperty("用户登录身份信息")
    private IdentityInfoDto identityInfoDto;

    @ApiModelProperty("用户登陆拓展信息")
    private UserExtensionInfoDto userExtensionInfo;


    public IdentityInfoDto getIdentityInfoDto() {
        return identityInfoDto;
    }

    public void setIdentityInfoDto(IdentityInfoDto identityInfoDto) {
        this.identityInfoDto = identityInfoDto;
    }


    public UserExtensionInfoDto getUserExtensionInfo() {
        return userExtensionInfo;
    }

    public void setUserExtensionInfo(UserExtensionInfoDto userExtensionInfo) {
        this.userExtensionInfo = userExtensionInfo;
    }
}
