/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.response;

import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/10/24
 */
public class QueryTeamMsgResponseDto {

    private String code;

    private int size;

    private List<String> msgs;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<String> msgs) {
        this.msgs = msgs;
    }
}
