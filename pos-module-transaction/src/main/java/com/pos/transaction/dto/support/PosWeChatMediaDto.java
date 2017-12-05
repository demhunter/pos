/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.support;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 获取微信素材返回信息dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/27
 */
public class PosWeChatMediaDto implements Serializable {

    private String errcode; // 错误码

    private String errmsg; // 错误信息

    private byte[] data; // 成功返回数据

    public boolean isSuuc() {
        return StringUtils.isEmpty(errcode) && data.length > 0;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
