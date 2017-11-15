/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import com.pos.user.constant.InvitationChannel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/7/3
 */
public class EmployeeMaterial implements Serializable {

    private static final long serialVersionUID = 4265997167226072914L;

    private Long id;

    private Long employeeUserId;//推荐的设计师的ID

    private Long customerUserId;//用户的ID

    private Date createDate;//创建时间

    /**
     * @see  InvitationChannel
     */
    private byte channel;//渠道

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeUserId() {
        return employeeUserId;
    }

    public void setEmployeeUserId(Long employeeUserId) {
        this.employeeUserId = employeeUserId;
    }

    public Long getCustomerUserId() {
        return customerUserId;
    }

    public void setCustomerUserId(Long customerUserId) {
        this.customerUserId = customerUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }
}
