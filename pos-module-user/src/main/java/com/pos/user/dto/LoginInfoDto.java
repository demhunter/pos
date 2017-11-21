/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户注册或登录信息Dto
 *
 * @author wangbing
 * @version 1.0, 2016/12/15
 */
public class LoginInfoDto implements Serializable {

    /**
     * 用户登录或注册身份信息
     */
    @ApiModelProperty("用户登录或注册身份信息")
    private IdentityInfoDto identityInfoDto;

    /**
     * IP地址
     */
    @ApiModelProperty("IP地址")
    private String ip;

    /**
     * 用户拓展信息
     */
    @ApiModelProperty("用户拓展信息")
    private UserExtensionInfoDto userExtensionInfo;

    @ApiModelProperty("选填，上一级的userId")
    private Long recommendId;

    @ApiModelProperty("选填，登录的类型（int，1 = 推广发展客户的链接，2 = 推广发展渠推客的链接）")
    private Byte recommendType;

    @ApiModelProperty("推客邀请码（可空）")
    private String invitationCode;

    public IdentityInfoDto getIdentityInfoDto() {
        return identityInfoDto;
    }

    public void setIdentityInfoDto(IdentityInfoDto identityInfoDto) {
        this.identityInfoDto = identityInfoDto;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public UserExtensionInfoDto getUserExtensionInfo() {
        return userExtensionInfo;
    }

    public void setUserExtensionInfo(UserExtensionInfoDto userExtensionInfo) {
        this.userExtensionInfo = userExtensionInfo;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public Byte getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(Byte recommendType) {
        this.recommendType = recommendType;
    }
}
