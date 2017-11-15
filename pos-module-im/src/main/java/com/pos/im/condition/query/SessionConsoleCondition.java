/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.condition.query;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 会话的公共查询条件.
 *
 * @author wayne
 * @version 1.0, 2016/8/8
 */
public class SessionConsoleCondition implements Serializable {

    private Byte state; // 会话状态

    private Byte groupState; // 群组状态

    private LocalDate createTimeStart;//创建会话开始时间段

    private LocalDate createTimeEnd;//创建会话结束时间段

    private String employeeName;//业者姓名

    private String employeePhone;//业者手机号码

    private String customerPhone;//业主手机号

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Byte getGroupState() {
        return groupState;
    }

    public void setGroupState(Byte groupState) {
        this.groupState = groupState;
    }

    public LocalDate getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(LocalDate createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public LocalDate getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(LocalDate createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}