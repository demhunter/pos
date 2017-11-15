/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.vo.pos;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.pos.constants.UserAuditStatus;
import com.pos.pos.dto.auth.BaseAuthDto;

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

    @ApiModelProperty("* 身份认证审核状态：0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过")
    private Integer userAuditStatus;

    @ApiModelProperty("* 身份认证审核状态描述")
    public String getUserAuditStatusDesc() {
        return userAuditStatus == null ? "" : UserAuditStatus.getEnum(userAuditStatus).getDesc();
    }

    @ApiModelProperty("是否绑定收款银行卡（true：已绑定，false：未绑定）")
    private Boolean bindingCard;

    @ApiModelProperty("银行名称，仅当bindingCard = true时有效")
    private String bankName;

    @ApiModelProperty("银行卡卡号，仅当bindingCard = true时有效")
    private String cardNo;

    @ApiModelProperty("用户权限信息")
    private BaseAuthDto baseAuth;

    @ApiModelProperty("收款笔数")
    private Integer userPosCount;

    @ApiModelProperty("收款总金额（BigDecimal）")
    private BigDecimal userPosAmount;

    @ApiModelProperty("已提现总金额")
    private BigDecimal totalWithdrawDepositAmount;

    @ApiModelProperty("是否存在提现申请（true：是，false：否）")
    private Boolean withdrawDepositApply;

    @ApiModelProperty("待处理提现金额（BigDecimal，当存在提现申请时，此字段才有意义）")
    private BigDecimal withdrawDepositAmount;

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
