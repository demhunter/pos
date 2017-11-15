/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.employee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/7/6
 */
public class RecommendDto implements Serializable{
    private static final long serialVersionUID = 2053428791883935090L;
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("客户名字")
    private String customerName;

    @ApiModelProperty("客户手机号码")
    private String mobilePhone;

    @ApiModelProperty("推荐人名字")
    private String referee;

    @ApiModelProperty("推荐人手机号")
    private String refereeMobilePhone;

    @ApiModelProperty("公司名字")
    private String company;

    @ApiModelProperty("推荐时间")
    private Date createDate;

    @ApiModelProperty("渠道")
    private byte channel;

    @ApiModelProperty("渠道描述")
    private String channelDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getRefereeMobilePhone() {
        return refereeMobilePhone;
    }

    public void setRefereeMobilePhone(String refereeMobilePhone) {
        this.refereeMobilePhone = refereeMobilePhone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getChannelDesc() {
        return channelDesc;
    }

    public void setChannelDesc(String channelDesc) {
        this.channelDesc = channelDesc;
    }
}
