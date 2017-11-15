/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.user;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * Description.
 *
 * @author wayne
 * @version 1.0, 2017/4/20
 */
@ApiModel
public class UserTokenErrorDto implements Serializable {

    @ApiModelProperty("提交失败的IM Key")
    private String imKey;

    @ApiModelProperty("提交失败的IM用户ID")
    private String imUserId;

    @ApiModelProperty("提交失败的IM Token")
    private String imToken;

    @ApiModelProperty("错误消息")
    private String message;

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public String getImKey() {
        return imKey;
    }

    public void setImKey(String imKey) {
        this.imKey = imKey;
    }

    public String getImUserId() {
        return imUserId;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}