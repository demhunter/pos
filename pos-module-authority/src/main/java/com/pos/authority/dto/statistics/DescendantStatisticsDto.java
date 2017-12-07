/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 客户下级统计Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/7
 */
public class DescendantStatisticsDto implements Serializable {

    @ApiModelProperty("下级总数")
    private Integer totalDescendantCount;

    @ApiModelProperty("直接下级总数")
    private Integer childrenCount;

    @ApiModelProperty("间接下级总数")
    private Integer descendantCount;

    @ApiModelProperty("按等级统计直接下级和间接下级数量")
    private List<DescendantLevelStatisticsDto> levelStatistics;

    public DescendantStatisticsDto() {
        totalDescendantCount = 0;
        childrenCount = 0;
        descendantCount = 0;
    }

    public void statisticsDescendant() {
        if (!CollectionUtils.isEmpty(levelStatistics)) {
            this.levelStatistics.forEach(e -> {
                this.childrenCount = this.childrenCount + e.getChildrenCount();
                this.descendantCount = this.descendantCount + e.getDescendantCount();
                this.totalDescendantCount = this.totalDescendantCount + e.getTotalDescendantCount();
            });
        }
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

    public List<DescendantLevelStatisticsDto> getLevelStatistics() {
        return levelStatistics;
    }

    public void setLevelStatistics(List<DescendantLevelStatisticsDto> levelStatistics) {
        this.levelStatistics = levelStatistics;
    }
}
