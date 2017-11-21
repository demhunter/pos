/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/7/11
 */
public class PlatEmployeeListDto implements Serializable {

    private static final long serialVersionUID = -3720561967964262394L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机")
    private String mobilePhone;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("关联的推客数")
    private int twitterCount;

    @ApiModelProperty("已成单客户数")
    private int completeCustomerCount;

    @ApiModelProperty("已飞单客户数")
    private int refuseCustomerCount;

    @ApiModelProperty("谈单中的客户数")
    private int talkingCustomerCount;

    @ApiModelProperty("未关闭会话数")
    private int notCloseImCount;

    @ApiModelProperty("待支付订单数")
    private int waitPayOrderCount;

    @ApiModelProperty("进行中的订单数")
    private int ongoingOrderCount;

    @ApiModelProperty("是否是客服经理")
    private int userDetailType;

    @ApiModelProperty("是否接受分配")
    private boolean isDistribution;

    @ApiModelProperty("账户状态 false==禁用  true==启用")
    private boolean accountStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getTwitterCount() {
        return twitterCount;
    }

    public void setTwitterCount(int twitterCount) {
        this.twitterCount = twitterCount;
    }

    public int getCompleteCustomerCount() {
        return completeCustomerCount;
    }

    public void setCompleteCustomerCount(int completeCustomerCount) {
        this.completeCustomerCount = completeCustomerCount;
    }

    public int getRefuseCustomerCount() {
        return refuseCustomerCount;
    }

    public void setRefuseCustomerCount(int refuseCustomerCount) {
        this.refuseCustomerCount = refuseCustomerCount;
    }

    public int getTalkingCustomerCount() {
        return talkingCustomerCount;
    }

    public void setTalkingCustomerCount(int talkingCustomerCount) {
        this.talkingCustomerCount = talkingCustomerCount;
    }

    public int getNotCloseImCount() {
        return notCloseImCount;
    }

    public void setNotCloseImCount(int notCloseImCount) {
        this.notCloseImCount = notCloseImCount;
    }

    public int getWaitPayOrderCount() {
        return waitPayOrderCount;
    }

    public void setWaitPayOrderCount(int waitPayOrderCount) {
        this.waitPayOrderCount = waitPayOrderCount;
    }

    public int getOngoingOrderCount() {
        return ongoingOrderCount;
    }

    public void setOngoingOrderCount(int ongoingOrderCount) {
        this.ongoingOrderCount = ongoingOrderCount;
    }

    public int getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(int userDetailType) {
        this.userDetailType = userDetailType;
    }

    public boolean isDistribution() {
        return isDistribution;
    }

    public void setDistribution(boolean distribution) {
        isDistribution = distribution;
    }

    public boolean isAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }
}
