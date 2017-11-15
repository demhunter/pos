/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.transaction;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.constants.PosConstants;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class SelectCardRequestDto implements Serializable {

    @ApiModelProperty("卡的ID")
    private Long id;

    @ApiModelProperty("需要支付的金额（BigDecimal）")
    private BigDecimal amount;

    @ApiModelProperty("* 有效期")
    private String validDate;

    @ApiModelProperty("* CVV2")
    private String cvv2;

    /**
     * 参数校验
     *
     * @param fieldPrefix  异常提示前缀
     * @param posConstants 参数限制
     * @throws IllegalParamException 参数校验异常，参数不合法
     */
    public void check(String fieldPrefix, PosConstants posConstants) throws IllegalParamException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(id, fieldPrefix + "id");
        FieldChecker.checkMinMaxValue(amount, posConstants.getPosAmountDownLimit(),
                posConstants.getPosAmountUpLimit(), fieldPrefix + "amount");
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
