/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.login;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 注册推荐信息DTO
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class RegisterRecommendDto implements Serializable {

    @ApiModelProperty("v1.0.0 * 推荐类型（1 = 推广发展客户的链接，2 = 推广发展推客的链接）")
    private Integer recommendType;

    @ApiModelProperty("v1.0.0 * 推荐人userId，即推客UserId")
    private Long recommendUserId;

    public Integer getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(Integer recommendType) {
        this.recommendType = recommendType;
    }

    public Long getRecommendUserId() {
        return recommendUserId;
    }

    public void setRecommendUserId(Long recommendUserId) {
        this.recommendUserId = recommendUserId;
    }
}
