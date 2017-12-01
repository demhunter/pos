/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.response;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 推荐人简要信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@ApiModel("推荐人简要信息")
public class RecommendSimpleVo implements Serializable {

    @ApiModelProperty("推荐人id")
    private Long userId;

    @ApiModelProperty("推荐人姓名")
    private String name;

    @ApiModelProperty("推荐人电话号码")
    private String phone;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
