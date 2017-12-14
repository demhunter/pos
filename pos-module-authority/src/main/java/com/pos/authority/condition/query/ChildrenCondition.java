/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.condition.query;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 直接下级查询条件
 *
 * @author wangbing
 * @version 1.0, 2017/12/14
 */
public class ChildrenCondition {

    @ApiModelProperty("上级用户id")
    private Long parentUserId;

    @ApiModelProperty("直接下级等级")
    private Integer level;

    @ApiModelProperty("搜索关键字（姓名或备注）")
    private String searchKey;

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
