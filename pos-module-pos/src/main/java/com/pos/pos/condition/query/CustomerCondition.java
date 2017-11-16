/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.condition.query;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.date.SimpleDateUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 快捷收款用户查询条件
 *
 * @author wangbing
 * @version 1.0, 2017/8/25
 */
public class CustomerCondition implements Serializable {

    @ApiModelProperty("自增id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("身份认证状态（0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过）")
    private Integer userAuditStatus;

    @ApiModelProperty("收款功能权限（true：有收款权限，false：无收款权限）")
    private Boolean getPermission;

    @ApiModelProperty("是否是推客（true：是推客，false：不是推客）")
    private Boolean twitterPermission;

    @ApiModelProperty("是否有绑定银行卡")
    private Boolean bindingCard;

    @ApiModelProperty("是否存在提现申请")
    private Boolean withdrawDeposit;

    @ApiModelProperty("快捷收款注册开始时间")
    private Date beginTime;

    @ApiModelProperty("快捷收款注册结束时间")
    private Date endTime;

    @ApiModelProperty("包含在内的用户列表")
    private List<Long> includeUserIds;

    public Integer getUserAuditStatus() {
        return userAuditStatus;
    }

    public void setUserAuditStatus(Integer userAuditStatus) {
        this.userAuditStatus = userAuditStatus;
    }

    public Boolean getGetPermission() {
        return getPermission;
    }

    public void setGetPermission(Boolean getPermission) {
        this.getPermission = getPermission;
    }

    public Boolean getTwitterPermission() {
        return twitterPermission;
    }

    public void setTwitterPermission(Boolean twitterPermission) {
        this.twitterPermission = twitterPermission;
    }

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

    public Boolean getBindingCard() {
        return bindingCard;
    }

    public void setBindingCard(Boolean bindingCard) {
        this.bindingCard = bindingCard;
    }

    public Boolean getWithdrawDeposit() {
        return withdrawDeposit;
    }

    public void setWithdrawDeposit(Boolean withdrawDeposit) {
        this.withdrawDeposit = withdrawDeposit;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public void setBeginTime(String beginTimeStr) {
        if (!StringUtils.isEmpty(beginTimeStr)) {
            Date beginTime = SimpleDateUtils.parseDate(beginTimeStr, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            this.beginTime = SimpleDateUtils.getDateOfMidNight(beginTime);
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTimeStr) {
        if (!StringUtils.isEmpty(endTimeStr)) {
            Date endTime = SimpleDateUtils.parseDate(endTimeStr, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            this.endTime = SimpleDateUtils.getDateOfTodayEnd(endTime);
        }
    }

    public List<Long> getIncludeUserIds() {
        return includeUserIds;
    }

    public void setIncludeUserIds(List<Long> includeUserIds) {
        this.includeUserIds = includeUserIds;
    }
}
