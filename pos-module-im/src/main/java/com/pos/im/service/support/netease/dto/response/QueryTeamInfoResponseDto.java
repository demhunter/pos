/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.response;

import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class QueryTeamInfoResponseDto {

    private String code;

    private List<QueryTeamInfo> tinfos;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<QueryTeamInfo> getTinfos() {
        return tinfos;
    }

    public void setTinfos(List<QueryTeamInfo> tinfos) {
        this.tinfos = tinfos;
    }
}
