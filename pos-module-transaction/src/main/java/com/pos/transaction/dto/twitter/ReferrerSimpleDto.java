/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.twitter;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 推荐人简要信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/11/6
 */
public class ReferrerSimpleDto implements Serializable {

    @ApiModelProperty("推荐人UserId")
    private Long userId;

    @ApiModelProperty("推荐人姓名")
    private String referrerName;

    @ApiModelProperty("推荐人电话号码（隐藏中间四位）")
    private String referrerPhone;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }

    public String getReferrerPhone() {
        return referrerPhone;
    }

    public void setReferrerPhone(String referrerPhone) {
        this.referrerPhone = referrerPhone;
    }
}
