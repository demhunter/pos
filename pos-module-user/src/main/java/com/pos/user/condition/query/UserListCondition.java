/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

/**
 * 用户列表查询条件
 *
 * @author wangbing
 * @version 1.0, 2017/01/09
 */
public class UserListCondition {

    private String userPhone;

    private String searchKey;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
