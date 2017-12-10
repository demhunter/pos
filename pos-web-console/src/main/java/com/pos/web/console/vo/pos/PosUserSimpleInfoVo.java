/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.vo.pos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.transaction.constants.UserAuditStatus;
import com.pos.transaction.dto.auth.BaseAuthDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 快捷收款用户简要信息Vo
 *
 * @author wangbing
 * @version 1.0, 2017/8/24
 */
public class PosUserSimpleInfoVo implements Serializable {

    @ApiModelProperty("自增id")
    private Long id;

    @ApiModelProperty("快捷收款用户id")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("注册快捷收款时间（Date）")
    private Date registerTime;

    @ApiModelProperty("v2.0.0 * 账号状态（true：启用；false：禁用）")
    private Boolean userAvailable;

    @ApiModelProperty("v2.0.0 * 是否存在上级用户（true：存在；false：不存在）")
    private Boolean existedParent;

    @ApiModelProperty("v2.0.0 * 上级用户id")
    private Long parentUserId;

    @ApiModelProperty("v2.0.0 * 上级用户姓名")
    private String parentName;

    @ApiModelProperty("v2.0.0 * 上级用户电话")
    private String parentPhone;

    @ApiModelProperty("身份认证审核状态：0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过")
    private Integer userAuditStatus;

    @ApiModelProperty("身份认证审核状态描述")
    public String getUserAuditStatusDesc() {
        return userAuditStatus == null ? "" : UserAuditStatus.getEnum(userAuditStatus).getDesc();
    }

    @ApiModelProperty("是否绑定收款银行卡（true：已绑定，false：未绑定）")
    private Boolean bindingCard;

    @ApiModelProperty("银行名称，仅当bindingCard = true时有效")
    private String bankName;

    @ApiModelProperty("银行卡卡号，仅当bindingCard = true时有效")
    private String cardNo;

    @ApiModelProperty("v2.0.0 * 用户等级（1：Lv1；2：Lv2；3：Lv3；4：Lv4）")
    private Integer level;

    @ApiModelProperty("v2.0.0 * 收款费率（具体百分数值，如0.45%返回0.45）（BigDecimal）")
    private BigDecimal withdrawRate;

    @ApiModelProperty("v2.0.0 * 收款额外手续费（单位：元）（BigDecimal）")
    private BigDecimal extraServiceCharge;

    @ApiModelProperty("收款笔数")
    private Integer userPosCount;

    @ApiModelProperty("收款总金额（BigDecimal）")
    private BigDecimal userPosAmount;

    @ApiModelProperty("v2.0.0 * 直接下级数量")
    private Integer childrenCount;

    @ApiModelProperty("v2.0.0 * 间接下级数量")
    private Integer descendantCount;

    @ApiModelProperty("v2.0.0 * 当前可提现佣金（BigDecimal）")
    private BigDecimal currentBrokerage;

    @ApiModelProperty("v2.0.0 * 佣金提现次数")
    private Integer brokerageAppliedCount;

    @ApiModelProperty("v2.0.0 * 累计已提现佣金（BigDecimal）")
    private BigDecimal appliedBrokerage;

    @ApiModelProperty("v2.0.0 * 回访次数")
    private Integer interviewCount;

    @Deprecated
    @JsonIgnore
    private BigDecimal totalWithdrawDepositAmount;

    @Deprecated
    @JsonIgnore
    private Boolean withdrawDepositApply;

    @Deprecated
    @JsonIgnore
    private BigDecimal withdrawDepositAmount;

    @Deprecated
    @JsonIgnore
    private BaseAuthDto baseAuth;

    public Integer getInterviewCount() {
        return interviewCount;
    }

    public void setInterviewCount(Integer interviewCount) {
        this.interviewCount = interviewCount;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public Integer getDescendantCount() {
        return descendantCount;
    }

    public void setDescendantCount(Integer descendantCount) {
        this.descendantCount = descendantCount;
    }

    public Boolean getUserAvailable() {
        return userAvailable;
    }

    public void setUserAvailable(Boolean userAvailable) {
        this.userAvailable = userAvailable;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getWithdrawRate() {
        return withdrawRate;
    }

    public void setWithdrawRate(BigDecimal withdrawRate) {
        this.withdrawRate = withdrawRate;
    }

    public BigDecimal getExtraServiceCharge() {
        return extraServiceCharge;
    }

    public void setExtraServiceCharge(BigDecimal extraServiceCharge) {
        this.extraServiceCharge = extraServiceCharge;
    }

    public BigDecimal getCurrentBrokerage() {
        return currentBrokerage;
    }

    public void setCurrentBrokerage(BigDecimal currentBrokerage) {
        this.currentBrokerage = currentBrokerage;
    }

    public Integer getBrokerageAppliedCount() {
        return brokerageAppliedCount;
    }

    public void setBrokerageAppliedCount(Integer brokerageAppliedCount) {
        this.brokerageAppliedCount = brokerageAppliedCount;
    }

    public BigDecimal getAppliedBrokerage() {
        return appliedBrokerage;
    }

    public void setAppliedBrokerage(BigDecimal appliedBrokerage) {
        this.appliedBrokerage = appliedBrokerage;
    }

    public Boolean getExistedParent() {
        return existedParent;
    }

    public void setExistedParent(Boolean existedParent) {
        this.existedParent = existedParent;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public Integer getUserPosCount() {
        return userPosCount;
    }

    public void setUserPosCount(Integer userPosCount) {
        this.userPosCount = userPosCount;
    }

    public BigDecimal getUserPosAmount() {
        return userPosAmount;
    }

    public void setUserPosAmount(BigDecimal userPosAmount) {
        this.userPosAmount = userPosAmount;
    }

    public BaseAuthDto getBaseAuth() {
        return baseAuth;
    }

    public void setBaseAuth(BaseAuthDto baseAuth) {
        this.baseAuth = baseAuth;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getUserAuditStatus() {
        return userAuditStatus;
    }

    public void setUserAuditStatus(Integer userAuditStatus) {
        this.userAuditStatus = userAuditStatus;
    }

    public BigDecimal getTotalWithdrawDepositAmount() {
        return totalWithdrawDepositAmount;
    }

    public void setTotalWithdrawDepositAmount(BigDecimal totalWithdrawDepositAmount) {
        this.totalWithdrawDepositAmount = totalWithdrawDepositAmount;
    }

    public Boolean getWithdrawDepositApply() {
        return withdrawDepositApply;
    }

    public void setWithdrawDepositApply(Boolean withdrawDepositApply) {
        this.withdrawDepositApply = withdrawDepositApply;
    }

    public BigDecimal getWithdrawDepositAmount() {
        return withdrawDepositAmount;
    }

    public void setWithdrawDepositAmount(BigDecimal withdrawDepositAmount) {
        this.withdrawDepositAmount = withdrawDepositAmount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Boolean getBindingCard() {
        return bindingCard;
    }

    public void setBindingCard(Boolean bindingCard) {
        this.bindingCard = bindingCard;
    }
}
