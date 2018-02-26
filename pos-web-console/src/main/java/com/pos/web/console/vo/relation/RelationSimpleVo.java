/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.vo.relation;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 简要客户关系Vo
 *
 * @author wangbing
 * @version 1.0, 2018/2/26
 */
public class RelationSimpleVo implements Serializable {

    private static final long serialVersionUID = -9082761565107987773L;
    @ApiModelProperty("下级用户id")
    private Long childUserId;

    @ApiModelProperty("上级用户id")
    private Long parentUserId;

    public Long getChildUserId() {
        return childUserId;
    }

    public void setChildUserId(Long childUserId) {
        this.childUserId = childUserId;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }
}
