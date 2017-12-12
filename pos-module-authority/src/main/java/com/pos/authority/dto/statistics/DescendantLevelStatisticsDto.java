/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 各等级的下级统计信息
 *
 * @author wangbing
 * @version 1.0, 2017/12/7
 */
public class DescendantLevelStatisticsDto implements Serializable, Comparable<DescendantLevelStatisticsDto> {

    private static final long serialVersionUID = -5764358829041058906L;
    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("等级对应的下级总数")
    private Integer totalDescendantCount;

    @ApiModelProperty("等级对应的直接下级总数")
    private Integer childrenCount;

    @ApiModelProperty("等级对应的间接下级总数")
    private Integer descendantCount;

    public DescendantLevelStatisticsDto(Integer level) {
        this.level = level;
        totalDescendantCount = 0;
        childrenCount = 0;
        descendantCount = 0;
    }

    @Override
    public int compareTo(DescendantLevelStatisticsDto o) {
        if (o == null) {
            return 0;
        }
        if (this.level > o.getLevel()) {
            return 1;
        } else if (this.level < o.getLevel()) {
            return -1;
        }
        return 0;
    }

    public void incrementChild() {
        this.childrenCount = this.childrenCount + 1;
        incrementTotal();
    }

    public void incremnetDescendant() {
        this.descendantCount = this.descendantCount + 1;
        incrementTotal();
    }

    private void incrementTotal() {
        this.totalDescendantCount = this.totalDescendantCount + 1;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getTotalDescendantCount() {
        return totalDescendantCount;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public Integer getDescendantCount() {
        return descendantCount;
    }
}
