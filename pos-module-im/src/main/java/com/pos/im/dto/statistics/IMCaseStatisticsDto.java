/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 平台数据统计-作品会话统计
 *
 * @author wangbing
 * @version 1.0, 2017/03/31
 */
@Deprecated
public class IMCaseStatisticsDto implements Serializable, Comparable<IMCaseStatisticsDto> {

    @ApiModelProperty("IM会话创建时间")
    private LocalDate imCreateDate;

    @ApiModelProperty("与作品有关的会话数量")
    private Integer imCaseCount;

    @ApiModelProperty("作品浏览数")
    private Integer viewCount;

    @ApiModelProperty("作品收藏数")
    private Integer collectedCount;

    @ApiModelProperty("作品分享数")
    private Integer shareCount;

    @ApiModelProperty("订单数")
    private Integer orderCount;

    public IMCaseStatisticsDto(LocalDate imCreateDate) {
        this.imCreateDate = imCreateDate;
        this.imCaseCount = 0;
        this.viewCount = 0;
        this.collectedCount = 0;
        this.shareCount = 0;
        this.orderCount = 0;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(Integer collectedCount) {
        this.collectedCount = collectedCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public LocalDate getImCreateDate() {
        return imCreateDate;
    }

    public void setImCreateDate(LocalDate imCreateDate) {
        this.imCreateDate = imCreateDate;
    }

    public Integer getImCaseCount() {
        return imCaseCount;
    }

    public void setImCaseCount(Integer imCaseCount) {
        this.imCaseCount = imCaseCount;
    }

    @Override
    public int compareTo(IMCaseStatisticsDto o) {
        if (o == null) {
            return 0;
        }
        if (this.getImCreateDate().isBefore(o.getImCreateDate())) {
            return -1;
        } else if (this.getImCreateDate().equals(o.getImCreateDate())) {
            return 0;
        } else {
            return 1;
        }
    }
}
