/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.response;

import com.pos.im.service.support.netease.dto.request.UserInfoRequestDto;

import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/9/6
 */
public class GetUserInfoResponseDto {

    private String code;

    private List<UserInfoRequestDto> uinfos;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserInfoRequestDto> getUinfos() {
        return uinfos;
    }

    public void setUinfos(List<UserInfoRequestDto> uinfos) {
        this.uinfos = uinfos;
    }
}
