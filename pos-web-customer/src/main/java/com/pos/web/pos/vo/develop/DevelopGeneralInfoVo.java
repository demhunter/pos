/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.develop;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 发展渠道商概要信息
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
public class DevelopGeneralInfoVo implements Serializable {

    private static final long serialVersionUID = 1441788049758802955L;
    @ApiModelProperty("累计发展人数")
    private Integer developCount;

    public Integer getDevelopCount() {
        return developCount;
    }

    public void setDevelopCount(Integer developCount) {
        this.developCount = developCount;
    }
}
