/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.v1_0_0;

import com.pos.user.dto.IdentityInfoDto;
import com.pos.user.dto.UserExtensionInfoDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 注册信息DTO
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class RegisterInfoDto implements Serializable {

    @ApiModelProperty("用户注册身份信息")
    private IdentityInfoDto identityInfoDto;

    @ApiModelProperty("用户拓展信息")
    private UserExtensionInfoDto userExtensionInfo;

    @ApiModelProperty("用户注册推荐信息")
    private RegisterRecommendDto registerRecommend;

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

    public RegisterRecommendDto getRegisterRecommend() {
        return registerRecommend;
    }

    public void setRegisterRecommend(RegisterRecommendDto registerRecommend) {
        this.registerRecommend = registerRecommend;
    }
}
