/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public class OrderListCondition implements Serializable {

    private String type;

    private String status;

    private Long peUserId;

    public Long getPeUserId() {
        return peUserId;
    }

    public void setPeUserId(Long peUserId) {
        this.peUserId = peUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
