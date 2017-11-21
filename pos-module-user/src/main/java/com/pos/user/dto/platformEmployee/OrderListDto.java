/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public class OrderListDto implements Serializable {

    private static final long serialVersionUID = 3255684241917068367L;

    private Long id;

    private String orderNum;

    @ApiModelProperty("int 订单类型")
    private Byte orderType;


    @ApiModelProperty("int 订单状态")
    private Byte orderStatus;

    @ApiModelProperty("订单的阶段")
    private Byte orderPhase;

    @ApiModelProperty("（bigDecimal），已付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("装修公司名字")
    private String companyName;

    @ApiModelProperty("客户电话")
    private String customerMobilePhone;

    @ApiModelProperty("date 创建时间")
    private Date createDate;

    @ApiModelProperty("date 结束时间")
    private Date endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Byte getOrderType() {
        return orderType;
    }

    public void setOrderType(Byte orderType) {
        this.orderType = orderType;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Byte getOrderPhase() {
        return orderPhase;
    }

    public void setOrderPhase(Byte orderPhase) {
        this.orderPhase = orderPhase;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerMobilePhone() {
        return customerMobilePhone;
    }

    public void setCustomerMobilePhone(String customerMobilePhone) {
        this.customerMobilePhone = customerMobilePhone;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
