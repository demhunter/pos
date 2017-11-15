/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.pos.user.constant.CustomerOrderStatus;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public class CustomerListDto implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("userid")
    private Long userId;


    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("手机号")
    private String mobilePhone;

    @ApiModelProperty("账户是否启用 1 = 正常 2=异常 3=禁用  4= 删除")
    private byte status;

    @ApiModelProperty("int 关联来源")
    private byte relationType;

    /**
     * @see CustomerOrderStatus
     */
    @ApiModelProperty("是否飞单")
    private byte orderStatus;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("未关闭会话数")
    private int notCloseSessionCount;

    @ApiModelProperty("未结束订单数")
    private int notEndOrderCount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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


    public byte getRelationType() {
        return relationType;
    }

    public void setRelationType(byte relationType) {
        this.relationType = relationType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getNotCloseSessionCount() {
        return notCloseSessionCount;
    }

    public void setNotCloseSessionCount(int notCloseSessionCount) {
        this.notCloseSessionCount = notCloseSessionCount;
    }

    public int getNotEndOrderCount() {
        return notEndOrderCount;
    }

    public void setNotEndOrderCount(int notEndOrderCount) {
        this.notEndOrderCount = notEndOrderCount;
    }
}
