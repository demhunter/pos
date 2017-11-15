/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

import com.google.common.base.Strings;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.date.SimpleDateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.pos.common.util.date.SimpleDateUtils.DatePattern.YYYY_MM_DD;
import static com.pos.common.util.date.SimpleDateUtils.HourMinSecondFormat.TODAY_END;

/**
 * M端客户列表查询的条件
 * @author 睿智
 * @version 1.0, 2017/7/17
 */
public class CustomerConsoleListCondition implements Serializable {

    private List<Long> userIds;

    private Byte customerType;

    private Boolean intention;

    private Boolean login;

    private String nikeName;

    private String userPhone;

    private Date registerStartTime;

    private Date registerEndTime;

    public void checkCustomerInfo(String customerInfo){
        if (!Strings.isNullOrEmpty(customerInfo)) {
            if (SimpleRegexUtils.isMobile(customerInfo)) {
                // 输入的搜索关键字是手机号
                this.userPhone = customerInfo;
            } else {
                this.nikeName = customerInfo;
            }
        }
    }

    @SuppressWarnings("all")
    public void parseAndSetBeginTime(String beginTime) {
        this.registerStartTime = SimpleDateUtils.parseDate(beginTime, YYYY_MM_DD.toString());
    }

    @SuppressWarnings("all")
    public void parseAndSetEndTime(String endTime) {
        this.registerEndTime = SimpleDateUtils.parseDate(endTime, YYYY_MM_DD.toString(), TODAY_END);
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Boolean getIntention() {
        return intention;
    }

    public void setIntention(Boolean intention) {
        this.intention = intention;
    }

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Date getRegisterStartTime() {
        return registerStartTime;
    }

    public void setRegisterStartTime(Date registerStartTime) {
        this.registerStartTime = registerStartTime;
    }

    public Date getRegisterEndTime() {
        return registerEndTime;
    }

    public void setRegisterEndTime(Date registerEndTime) {
        this.registerEndTime = registerEndTime;
    }
}
