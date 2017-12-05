/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.transaction.constants.TransactionStatusType;
import com.pos.transaction.dto.PosOutCardInfoDto;
import com.pos.transaction.dto.card.PosCardDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class TransactionRecordDto implements Serializable {

    @ApiModelProperty("交易记录自增id")
    private Long id;

    @ApiModelProperty("流水号")
    private String recordNum;

    @ApiModelProperty("收款银行卡ID")
    private Long inCardId;

    @ApiModelProperty("收款银行卡详细信息")
    private PosCardDto inCardInfo;

    @ApiModelProperty("转出卡信息")
    private PosOutCardInfoDto outCardInfo;

    @ApiModelProperty("提现用户userId")
    private Long userId;

    @ApiModelProperty("提现用户名称")
    private String userName;

    @ApiModelProperty("提现用户电话号码")
    private String userPhone;

    @ApiModelProperty("提现的金额")
    private BigDecimal amount;

    @ApiModelProperty("手续费，用户提现时所有需要扣除的金额")
    private BigDecimal serviceCharge;

    @ApiModelProperty("实际到账金额")
    private BigDecimal arrivalAmount;

    @ApiModelProperty("支付手续费（用户发起支付，支付公司到平台扣除的）")
    private BigDecimal payCharge;

    @ApiModelProperty("提现手续费（平台支付给用户时，支付公司扣除的）")
    private BigDecimal posCharge;

    @Deprecated
    @JsonIgnore
    private Long companyId;

    @Deprecated
    @JsonIgnore
    private Integer costType;

    @ApiModelProperty("状态：0 = 已下单，1 = 交易处理中，2 = 交易失败，3 = 交易成功，4 = 已手动处理")
    private Integer status;

    @ApiModelProperty("状态描述")
    public String getStatusDesc() {
        TransactionStatusType type = parseTransactionStatus();
        return type == null ? null : type.getDesc();
    }

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("用户支付的时间")
    private Date payDate;

    @ApiModelProperty("公司支付给用户的时间")
    private Date completeDate;

    @ApiModelProperty("合利宝支付的流水号")
    private String helibaoZhifuNum;

    @ApiModelProperty("合利宝提现的流水号")
    private String helibaoTixianNum;

    @ApiModelProperty("v2.0.0 * 交易失败次数")
    private Integer failureTimes;

    public TransactionStatusType parseTransactionStatus() {
        return status == null ? null : TransactionStatusType.getEnum(status);
    }

    public Integer getFailureTimes() {
        return failureTimes;
    }

    public void setFailureTimes(Integer failureTimes) {
        this.failureTimes = failureTimes;
    }

    public PosCardDto getInCardInfo() {
        return inCardInfo;
    }

    public void setInCardInfo(PosCardDto inCardInfo) {
        this.inCardInfo = inCardInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public Long getInCardId() {
        return inCardId;
    }

    public void setInCardId(Long inCardId) {
        this.inCardId = inCardId;
    }

    public PosOutCardInfoDto getOutCardInfo() {
        return outCardInfo;
    }

    public void setOutCardInfo(PosOutCardInfoDto outCardInfo) {
        this.outCardInfo = outCardInfo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getArrivalAmount() {
        return arrivalAmount;
    }

    public void setArrivalAmount(BigDecimal arrivalAmount) {
        this.arrivalAmount = arrivalAmount;
    }

    public BigDecimal getPayCharge() {
        return payCharge;
    }

    public void setPayCharge(BigDecimal payCharge) {
        this.payCharge = payCharge;
    }

    public BigDecimal getPosCharge() {
        return posCharge;
    }

    public void setPosCharge(BigDecimal posCharge) {
        this.posCharge = posCharge;
    }

    @Deprecated
    @JsonIgnore
    public Long getCompanyId() {
        return companyId;
    }

    @Deprecated
    @JsonIgnore
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Deprecated
    @JsonIgnore
    public Integer getCostType() {
        return costType;
    }

    @Deprecated
    @JsonIgnore
    public void setCostType(Integer costType) {
        this.costType = costType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getHelibaoZhifuNum() {
        return helibaoZhifuNum;
    }

    public void setHelibaoZhifuNum(String helibaoZhifuNum) {
        this.helibaoZhifuNum = helibaoZhifuNum;
    }

    public String getHelibaoTixianNum() {
        return helibaoTixianNum;
    }

    public void setHelibaoTixianNum(String helibaoTixianNum) {
        this.helibaoTixianNum = helibaoTixianNum;
    }
}
