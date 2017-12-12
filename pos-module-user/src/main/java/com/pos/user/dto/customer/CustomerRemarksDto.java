/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.customer;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.validation.FieldChecker;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author lifei
 * @version 1.0, 2017/9/7
 */
public class CustomerRemarksDto implements Serializable {

    private static final long serialVersionUID = -7401711348387105943L;
    @ApiModelProperty("客户ID")
    private Long customerId;

    @ApiModelProperty("是否是意向客户")
    private Boolean intention;

    @ApiModelProperty("备注信息")
    private String remarks;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Boolean getIntention() {
        return intention;
    }

    public void setIntention(Boolean intention) {
        this.intention = intention;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void check(){
        FieldChecker.checkEmpty(customerId, "customerId");
        if (StringUtils.isNotEmpty(remarks) && remarks.trim().length()>0){
            FieldChecker.checkMaxLength(remarks, 200, "备注字数");
        }
    }
}
