/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic.AddressSupport;

import java.io.Serializable;

/**
 * @author lifei
 * @version 1.0, 2017/9/11
 */
public class TBResultData implements Serializable {

    private Byte code; //数据请求是否成功

    private Object data;//ip解析数据

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
