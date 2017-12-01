/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.descendant;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 下级统计信息VO
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class DescendantStatisticsVo implements Serializable {

    @ApiModelProperty("下级总数")
    private Integer totalDescendantCount;

    @ApiModelProperty("直接下级总数")
    private Integer childrenCount;

    @ApiModelProperty("间接下级总数")
    private Integer descendantCount;

    private List<DescendantLevelStatisticsVo> levelStatistics;

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

    public List<DescendantLevelStatisticsVo> getLevelStatistics() {
        return levelStatistics;
    }

    public void setLevelStatistics(List<DescendantLevelStatisticsVo> levelStatistics) {
        this.levelStatistics = levelStatistics;
    }
}
