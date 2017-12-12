/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.descendant;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 各等级的下级统计信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class DescendantLevelStatisticsVo implements Serializable {

    private static final long serialVersionUID = -5085283403665572028L;
    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("等级对应的下级总数")
    private Integer totalDescendantCount;

    @ApiModelProperty("等级对应的直接下级总数")
    private Integer childrenCount;

    @ApiModelProperty("等级对应的间接下级总数")
    private Integer descendantCount;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getTotalDescendantCount() {
        return totalDescendantCount;
    }

    public void setTotalDescendantCount(Integer totalDescendantCount) {
        this.totalDescendantCount = totalDescendantCount;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public Integer getDescendantCount() {
        return descendantCount;
    }

    public void setDescendantCount(Integer descendantCount) {
        this.descendantCount = descendantCount;
    }
}
