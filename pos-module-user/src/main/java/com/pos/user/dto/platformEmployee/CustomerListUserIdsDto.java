/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/7/13
 */
public class CustomerListUserIdsDto implements Serializable {

    private static final long serialVersionUID = 2331439975285676467L;
    @ApiModelProperty("未关闭会话数")
    private int notCloseSessionCount;

    @ApiModelProperty("未结束订单数")
    private int notEndOrderCount;

    @ApiModelProperty("userId")
    private Long userId;

    public int getNotCloseSessionCount() {
        return notCloseSessionCount;
    }

    public void setNotCloseSessionCount(int notCloseSessionCount) {
        this.notCloseSessionCount = notCloseSessionCount;
    }

    public int getNotEndOrderCount() {
        return notEndOrderCount;
    }

    public void setNotEndOrderCount(int notEndOrderCount) {
        this.notEndOrderCount = notEndOrderCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
