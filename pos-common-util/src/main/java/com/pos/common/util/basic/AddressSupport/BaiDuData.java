/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic.AddressSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lifei
 * @version 1.0, 2017/9/11
 */
public class BaiDuData implements Serializable {

    private Byte status;//状态

    private Long t;//时间戳

    private Date set_cache_time;

    private List<BuiResultData> data;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public Date getSet_cache_time() {
        return set_cache_time;
    }

    public void setSet_cache_time(Date set_cache_time) {
        this.set_cache_time = set_cache_time;
    }

    public List<BuiResultData> getData() {
        return data;
    }

    public void setData(List<BuiResultData> data) {
        this.data = data;
    }
}
