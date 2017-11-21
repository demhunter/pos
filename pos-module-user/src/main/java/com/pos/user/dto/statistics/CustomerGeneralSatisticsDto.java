/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 平台数据统计-C端用户简要信息
 *
 * @author wangbing
 * @version 1.0, 2017/03/31
 */
@Deprecated
public class CustomerGeneralSatisticsDto implements Serializable {

    @ApiModelProperty("C端用户量")
    private int customerCount;

    @ApiModelProperty("C端注册量")
    private int registerCount;

    @ApiModelProperty("转化率(float)，测试时需要验证精度")
    public float getConversionPercent() {
        return customerCount > 0 ? (float) registerCount / customerCount : 0f;
    }

    public CustomerGeneralSatisticsDto() {
        this.customerCount = 0;
        this.registerCount = 0;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }
}
