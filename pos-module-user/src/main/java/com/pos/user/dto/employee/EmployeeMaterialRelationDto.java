/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.employee;

import java.io.Serializable;
import java.util.Date;

/**
 * 主材内购业者客户关系Dto
 *
 * @author lifei
 * @version 1.0, 2017/7/5.
 */
public class EmployeeMaterialRelationDto implements Serializable{

    private Long id;

    private Long employeeUserId;//推荐的设计师的ID

    private Long customerUserId;//用户的ID

    private Date createDate;//创建时间

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

}
