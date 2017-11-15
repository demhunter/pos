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
public class PlatformEmployeeAllInfoDto implements Serializable {

    private static final long serialVersionUID = -3532209852823471058L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String mobilePhone;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("区域ID")
    private Long areaId;

    @ApiModelProperty("是否是客服经理")
    private int userDetailType;

    @ApiModelProperty("是否愿意接受分配")
    private boolean distribution;

    @ApiModelProperty("是否启用")
    private boolean enable;

    @ApiModelProperty("关联推客数")
    private int twitterCount;

    @ApiModelProperty("已成单客户数")
    private int completeCustomerCount;

    @ApiModelProperty("已飞单客户数")
    private int refuseCustomerCount;

    @ApiModelProperty("谈单中的客户数")
    private int talkingCustomerCount;


    @ApiModelProperty("未关闭会话数")
    private int notCloseSessionCount;

    @ApiModelProperty("已关闭会话数")
    private int closeSessionCount;

    @ApiModelProperty("待支付订单数")
    private int waitPayOrderCount;

    @ApiModelProperty("已完成订单数")
    private int completeOrderCount;

    @ApiModelProperty("已关闭订单数")
    private int closeOrderCount;

    @ApiModelProperty("进行中的订单数")
    private int ongoingOrderCount;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(int userDetailType) {
        this.userDetailType = userDetailType;
    }

    public boolean isDistribution() {
        return distribution;
    }

    public void setDistribution(boolean distribution) {
        this.distribution = distribution;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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


    public int getNotCloseSessionCount() {
        return notCloseSessionCount;
    }

    public void setNotCloseSessionCount(int notCloseSessionCount) {
        this.notCloseSessionCount = notCloseSessionCount;
    }

    public int getCloseSessionCount() {
        return closeSessionCount;
    }

    public void setCloseSessionCount(int closeSessionCount) {
        this.closeSessionCount = closeSessionCount;
    }

    public int getWaitPayOrderCount() {
        return waitPayOrderCount;
    }

    public void setWaitPayOrderCount(int waitPayOrderCount) {
        this.waitPayOrderCount = waitPayOrderCount;
    }

    public int getCompleteOrderCount() {
        return completeOrderCount;
    }

    public void setCompleteOrderCount(int completeOrderCount) {
        this.completeOrderCount = completeOrderCount;
    }

    public int getCloseOrderCount() {
        return closeOrderCount;
    }

    public void setCloseOrderCount(int closeOrderCount) {
        this.closeOrderCount = closeOrderCount;
    }

    public int getOngoingOrderCount() {
        return ongoingOrderCount;
    }

    public void setOngoingOrderCount(int ongoingOrderCount) {
        this.ongoingOrderCount = ongoingOrderCount;
    }
}
