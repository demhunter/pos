/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public class SessionListDto implements Serializable {

    private static final long serialVersionUID = -2128634176449629297L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("会话名")
    private String sessionName;

    @ApiModelProperty("所属作品")
    private String caseName;

    @ApiModelProperty("所属公司")
    private String companyName;

    @ApiModelProperty("客户名")
    private String customerName;

    @ApiModelProperty("客户手机")
    private String customerMobilePhone;

    @ApiModelProperty("int 会话的状态")
    private byte sessionStatus;


    @ApiModelProperty("Date 会话创建时间")
    private Date createDate;

    @ApiModelProperty("最新消息接收时间")
    private Date lastDate;

    @ApiModelProperty("关闭者角色名")
    private String closerName;

    @ApiModelProperty("会话关闭者ID")
    private Long closerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobilePhone() {
        return customerMobilePhone;
    }

    public void setCustomerMobilePhone(String customerMobilePhone) {
        this.customerMobilePhone = customerMobilePhone;
    }

    public byte getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(byte sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public String getCloserName() {
        return closerName;
    }

    public void setCloserName(String closerName) {
        this.closerName = closerName;
    }

    public Long getCloserId() {
        return closerId;
    }

    public void setCloserId(Long closerId) {
        this.closerId = closerId;
    }
}
