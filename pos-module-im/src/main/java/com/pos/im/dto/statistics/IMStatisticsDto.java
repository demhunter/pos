/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 平台数据统计-会话数据统计Dto
 *
 * @author wangbing
 * @version 1.0, 2017/03/30
 */
@Deprecated
public class IMStatisticsDto implements Serializable, Comparable<IMStatisticsDto> {

    @ApiModelProperty("IM会话创建时间")
    private LocalDate imCreateDate;

    @ApiModelProperty("创建者先说话的数量")
    private Integer createFirstCount;

    @ApiModelProperty("非创建者先说话的数量")
    private Integer notCreateFirstCount;

    @ApiModelProperty("无人说话的数量")
    private Integer noFirstCount;

    /**
     * 时间搜索类型
     */
    private Byte timeSearchType;

    public IMStatisticsDto(LocalDate imCreateDate) {
        this.imCreateDate = imCreateDate;
        createFirstCount = 0;
        notCreateFirstCount = 0;
        noFirstCount = 0;
    }

    public Byte getTimeSearchType() {
        return timeSearchType;
    }

    public void setTimeSearchType(Byte timeSearchType) {
        this.timeSearchType = timeSearchType;
    }

    public LocalDate getImCreateDate() {
        return imCreateDate;
    }

    public void setImCreateDate(LocalDate imCreateDate) {
        this.imCreateDate = imCreateDate;
    }

    public Integer getCreateFirstCount() {
        return createFirstCount;
    }

    public void setCreateFirstCount(Integer createFirstCount) {
        this.createFirstCount = createFirstCount;
    }

    public Integer getNotCreateFirstCount() {
        return notCreateFirstCount;
    }

    public void setNotCreateFirstCount(Integer notCreateFirstCount) {
        this.notCreateFirstCount = notCreateFirstCount;
    }

    public Integer getNoFirstCount() {
        return noFirstCount;
    }

    public void setNoFirstCount(Integer noFirstCount) {
        this.noFirstCount = noFirstCount;
    }

    @Override
    public int compareTo(IMStatisticsDto o) {
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
