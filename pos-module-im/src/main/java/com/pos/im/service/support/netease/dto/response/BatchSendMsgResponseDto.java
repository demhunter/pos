/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.response;

import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class BatchSendMsgResponseDto {

    private String code;

    private List<String> unregister;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getUnregister() {
        return unregister;
    }

    public void setUnregister(List<String> unregister) {
        this.unregister = unregister;
    }
}
