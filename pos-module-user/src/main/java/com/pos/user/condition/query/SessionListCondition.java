/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public class SessionListCondition {

    private String available;

    private String sessionName;

    private String companyName;

    private String mobilePhone;

    private Long peUserId;

    public Long getPeUserId() {
        return peUserId;
    }

    public void setPeUserId(Long peUserId) {
        this.peUserId = peUserId;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
