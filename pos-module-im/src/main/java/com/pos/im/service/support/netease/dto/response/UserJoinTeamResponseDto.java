/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.response;

import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class UserJoinTeamResponseDto {

    private String code;

    private int count;

    private List<UserJoinTeamInfo> infos;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UserJoinTeamInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<UserJoinTeamInfo> infos) {
        this.infos = infos;
    }
}
