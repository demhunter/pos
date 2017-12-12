/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.condition.query;

import com.pos.common.util.date.SimpleDateUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * 推广相关查询条件定义
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
public class PopularizationCondition {

    private Date beginTime;

    private Date endTime;

    private Boolean available;

    private String searchKey;

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public void setBeginTime(String beginTimeStr) {
        if (!StringUtils.isEmpty(beginTimeStr)) {
            Date beginTime = SimpleDateUtils.parseDate(beginTimeStr, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            this.beginTime = SimpleDateUtils.getDateOfMidNight(beginTime);
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTimeStr) {
        if (!StringUtils.isEmpty(endTimeStr)) {
            Date endTime = SimpleDateUtils.parseDate(endTimeStr, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            this.endTime = SimpleDateUtils.getDateOfTodayEnd(endTime);
        }
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
