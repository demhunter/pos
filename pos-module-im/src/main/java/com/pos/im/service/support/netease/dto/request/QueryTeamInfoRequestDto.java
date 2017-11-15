/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.request;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class QueryTeamInfoRequestDto {

    private String tids;//群id列表，如["3083","3084"]

    private int ope;//1表示带上群成员列表，0表示不带群成员列表，只返回群信息

    public String getTids() {
        return tids;
    }

    public void setTids(String tids) {
        this.tids = tids;
    }

    public int getOpe() {
        return ope;
    }

    public void setOpe(int ope) {
        this.ope = ope;
    }
}
