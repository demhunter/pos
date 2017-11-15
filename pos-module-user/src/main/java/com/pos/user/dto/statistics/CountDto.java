/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.statistics;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/7/12
 */
public class CountDto implements Serializable{

    private int status;

    private int count;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
