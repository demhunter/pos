/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.user.dto.customer.CustomerDto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户信息vo
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class CustomerVo implements Serializable {

    private static final long serialVersionUID = 7076393946830362702L;

    @ApiModelProperty("客户登录信息")
    private CustomerDto customerDto;

    @ApiModelProperty("v1.0.0 * 身份认证审核状态，0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过")
    private Integer auditStatus;

    @ApiModelProperty("v1.0.0 * 身份认证审核未通过原因，当auditStatus = 3时此字段有效")
    private String rejectReason;

    @ApiModelProperty("v2.0.0 * 累计收款金额(BigDecimal)")
    private BigDecimal totalWithdrawAmount;

    @ApiModelProperty("v2.0.0 * 今日收益(BigDecimal)")
    private BigDecimal todayBrokerage;

    @ApiModelProperty("v2.0.0 * 用户当前等级")
    private Integer currentLevel;

    @ApiModelProperty("v2.0.0 * 用户当前等级描述")
    private String currentLevelDesc;

    @ApiModelProperty("v2.0.0 * 推荐人信息")
    private RecommendSimpleVo recommendInfo;

    @Deprecated
    @JsonIgnore
    private Boolean showGetRedDot;

    public RecommendSimpleVo getRecommendInfo() {
        return recommendInfo;
    }

    public void setRecommendInfo(RecommendSimpleVo recommendInfo) {
        this.recommendInfo = recommendInfo;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    @Deprecated
    public Boolean getShowGetRedDot() {
        return showGetRedDot;
    }

    @Deprecated
    public void setShowGetRedDot(Boolean showGetRedDot) {
        this.showGetRedDot = showGetRedDot;
    }

    public CustomerDto getCustomerDto() {
        return customerDto;
    }

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    public BigDecimal getTotalWithdrawAmount() {
        return totalWithdrawAmount;
    }

    public void setTotalWithdrawAmount(BigDecimal totalWithdrawAmount) {
        this.totalWithdrawAmount = totalWithdrawAmount;
    }

    public BigDecimal getTodayBrokerage() {
        return todayBrokerage;
    }

    public void setTodayBrokerage(BigDecimal todayBrokerage) {
        this.todayBrokerage = todayBrokerage;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getCurrentLevelDesc() {
        return currentLevelDesc;
    }

    public void setCurrentLevelDesc(String currentLevelDesc) {
        this.currentLevelDesc = currentLevelDesc;
    }
}
