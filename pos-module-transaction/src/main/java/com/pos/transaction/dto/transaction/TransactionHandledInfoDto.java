/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.transaction;

import com.pos.transaction.constants.PayModeEnum;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 手动处理结算信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class TransactionHandledInfoDto implements Serializable {

    private static final long serialVersionUID = -8571274535606357068L;
    @ApiModelProperty("自增id")
    private Long id;

    @ApiModelProperty("处理关联交易记录id")
    private Long recordId;

    @ApiModelProperty("结算金额（BigDecimal）")
    private BigDecimal amount;

    @ApiModelProperty("打款方式int，1=微信，2=支付，3=网银，4=线下")
    private Byte payMode;

    @ApiModelProperty("打款方式描述")
    public String getPayModeDesc() {
        return payMode == null ? "" : PayModeEnum.getEnum(payMode).getDesc();
    }

    @ApiModelProperty("打款凭证")
    private String voucher;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("打款日期")
    private Date createDate;

    @ApiModelProperty("创建人UserId")
    private Long createUserId;

    /**
     * 参数校验
     *
     * @param fieldPrefix 异常提示前缀
     * @throws IllegalParamException 校验异常，参数不合法
     */
    public void check(String fieldPrefix) throws IllegalParamException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(amount, fieldPrefix + "amount");
        FieldChecker.checkEmpty(payMode, fieldPrefix + "payMode");
        FieldChecker.checkEmpty(voucher, fieldPrefix + "voucher");
        if (StringUtils.isNotEmpty(remark)) {
            FieldChecker.checkMaxLength(remark, 50, fieldPrefix + "remark");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Byte getPayMode() {
        return payMode;
    }

    public void setPayMode(Byte payMode) {
        this.payMode = payMode;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
}
