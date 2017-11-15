/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.user;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * IM用户问候语DTO.
 *
 * @author wayne
 * @version 1.0, 2017/5/3
 */
@ApiModel
public class UserGreetingDto implements Serializable {

    @ApiModelProperty("系统默认的问候语")
    private String defaultGreeting;

    @ApiModelProperty("用户追加的问候语(未曾设置过时返回空)")
    private String appendGreeting;

    public UserGreetingDto() {
    }

    public UserGreetingDto(String defaultGreeting, String appendGreeting) {
        this.defaultGreeting = defaultGreeting;
        this.appendGreeting = appendGreeting;
    }

    public String getDefaultGreeting() {
        return defaultGreeting;
    }

    public void setDefaultGreeting(String defaultGreeting) {
        this.defaultGreeting = defaultGreeting;
    }

    public String getAppendGreeting() {
        return appendGreeting;
    }

    public void setAppendGreeting(String appendGreeting) {
        this.appendGreeting = appendGreeting;
    }

}