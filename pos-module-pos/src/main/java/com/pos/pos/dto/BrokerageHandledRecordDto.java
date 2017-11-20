/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto;

import com.pos.pos.constants.PayModeEnum;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现申请处理记录Dto
 *
 * @author wangbing
 * @version 1.0, 2017/8/25
 */
public class BrokerageHandledRecordDto implements Serializable {

    @ApiModelProperty("记录自增id（新增提现申请处理时，不填此字段）")
    private Long id;

    @ApiModelProperty("快捷收款用户userId")
    private Long userId;

    @ApiModelProperty("提现金额（BigDecimal）")
    private BigDecimal amount;

    @ApiModelProperty("打款方式（1 = 微信，2 = 支付，3 = 网银，4 = 线下）")
    private Byte payMode;

    @ApiModelProperty("打款方式描述（新增时不传此字段）")
    public String getPayModeDesc() {
        return payMode == null ? null : PayModeEnum.getEnum(payMode).getDesc();
    }

    @ApiModelProperty("打款凭证")
    private String voucher;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("打款日期（Date，新增提现申请处理时，不填此字段）")
    private Date payDate;

    /**
     * 新增佣金处理记录时，校验参数信息
     *
     * @param fieldPrefix 参数异常提示前缀
     * @throws IllegalParamException 缺少必填参数或参数值异常
     */
    public void check(String fieldPrefix) throws IllegalParamException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(userId, fieldPrefix + "userId");
        FieldChecker.checkEmpty(amount, fieldPrefix + "amount");
        FieldChecker.checkEmpty(PayModeEnum.getEnum(payMode), fieldPrefix + "payMode");
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

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
}
