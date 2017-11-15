/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.vo.response;

import com.pos.user.dto.v1_0_0.CustomerDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 客户首页展示内容
 *
 * @author wangbing
 * @version 1.0, 2017/8/22
 */
public class CustomerVo implements Serializable {

    private static final long serialVersionUID = 7076393946830362702L;

    @ApiModelProperty("客户基本信息信息")
    private CustomerDto customerDto;

    @ApiModelProperty("卡号")
    private String cardNO;

    @ApiModelProperty("银行名")
    private String bankName;

    @ApiModelProperty("* 银行LOGO")
    private String bankLogo;

    @ApiModelProperty("* 银行灰度LOGO")
    private String bankGrayLogo;

    @ApiModelProperty("* 身份认证审核状态，0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过")
    private Integer auditStatus;

    @ApiModelProperty("* 身份认证审核未通过原因，当auditStatus = 3时此字段有效")
    private String rejectReason;

    @ApiModelProperty("* 是否显示快捷收款上的小红点，true：显示，false：不显示")
    private Boolean showGetRedDot;

    @ApiModelProperty("* 推客状态，1 = 未启用，2 = 已启用，3 = 已关闭（PS：当状态为2或3时需要展示佣金提现）")
    private Integer twitterStatus;

    @ApiModelProperty("是否显示发展收款客户（当twitterStatus = 2时此字段有效）")
    private boolean showSpread;

    @ApiModelProperty("是否显示发展下级推客（当twitterStatus = 2时此字段有效）")
    private boolean showDevelop;

    @ApiModelProperty("是否可以收款（true：可以，false：不可以）")
    private boolean canGet;

    public String getBankGrayLogo() {
        return bankGrayLogo;
    }

    public void setBankGrayLogo(String bankGrayLogo) {
        this.bankGrayLogo = bankGrayLogo;
    }

    public boolean isCanGet() {
        return canGet;
    }

    public void setCanGet(boolean canGet) {
        this.canGet = canGet;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
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

    public Boolean getShowGetRedDot() {
        return showGetRedDot;
    }

    public void setShowGetRedDot(Boolean showGetRedDot) {
        this.showGetRedDot = showGetRedDot;
    }

    public Integer getTwitterStatus() {
        return twitterStatus;
    }

    public void setTwitterStatus(Integer twitterStatus) {
        this.twitterStatus = twitterStatus;
    }

    public CustomerDto getCustomerDto() {
        return customerDto;
    }

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isShowSpread() {
        return showSpread;
    }

    public void setShowSpread(boolean showSpread) {
        this.showSpread = showSpread;
    }

    public boolean isShowDevelop() {
        return showDevelop;
    }

    public void setShowDevelop(boolean showDevelop) {
        this.showDevelop = showDevelop;
    }
}
