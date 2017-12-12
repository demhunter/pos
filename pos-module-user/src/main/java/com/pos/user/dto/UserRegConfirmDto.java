/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.user.dto.customer.CustomerDto;

import java.io.Serializable;

/**
 * 用户注册确认Dto
 *
 * @author cc
 * @version 1.0, 16/8/11
 */
public class UserRegConfirmDto implements Serializable {

    private static final long serialVersionUID = -3429136510291785558L;
    /**
     * 是否需要用户确认开通角色
     */
    @ApiModelProperty("是否需要用户确认开通角色")
    private Boolean needConfirm;

    /**
     * 提示消息
     */
    @ApiModelProperty("提示消息")
    private String message;

    /**
     * 身份认证token
     */
    @ApiModelProperty("身份认证token")
    private String token;

    @ApiModelProperty("用户dto")
    private CustomerDto customerDto;

    public CustomerDto getCustomerDto() {
        return customerDto;
    }

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getNeedConfirm() {
        return needConfirm;
    }

    public void setNeedConfirm(Boolean needConfirm) {
        this.needConfirm = needConfirm;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
