/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/10/24
 */
public class SessionChangeDto implements Serializable {

    private static final long serialVersionUID = 3532245602128450553L;

    private Long userId;

    private String userType;

    public SessionChangeDto() {
    }

    public SessionChangeDto(Long userId, String userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
