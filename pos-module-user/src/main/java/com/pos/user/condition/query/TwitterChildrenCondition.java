/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

import com.pos.common.util.date.SimpleDateUtils;

import java.util.Date;
import java.util.List;

import static com.pos.common.util.date.SimpleDateUtils.DatePattern.YYYY_MM_DD;
import static com.pos.common.util.date.SimpleDateUtils.HourMinSecondFormat.TODAY_END;

/**
 * @author lifei
 * @version 1.0, 2017/10/12
 */
public class TwitterChildrenCondition {

    private Long twitterId; // 推客id

    private Byte childLevel; // 下级推客级别

    private List<Long> childrenTwitterIds; // 包含的下级推客id

    private List<Long> childrenUserIds; // 包含的下级推客userId

    private Boolean available; // 是否可用

    private Byte status; // 资质申请的状态

    private Date beginTime; //查询开始时间

    private Date endTime; //查询结束时间

    @SuppressWarnings("all")
    public void parseAndSetBeginTime(String beginTime) {
        this.beginTime = SimpleDateUtils.parseDate(beginTime, YYYY_MM_DD.toString());
    }

    @SuppressWarnings("all")
    public void parseAndSetEndTime(String endTime) {
        this.endTime = SimpleDateUtils.parseDate(endTime, YYYY_MM_DD.toString(), TODAY_END);
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(Long twitterId) {
        this.twitterId = twitterId;
    }

    public Byte getChildLevel() {
        return childLevel;
    }

    public void setChildLevel(Byte childLevel) {
        this.childLevel = childLevel;
    }

    public List<Long> getChildrenTwitterIds() {
        return childrenTwitterIds;
    }

    public void setChildrenTwitterIds(List<Long> childrenTwitterIds) {
        this.childrenTwitterIds = childrenTwitterIds;
    }

    public List<Long> getChildrenUserIds() {
        return childrenUserIds;
    }

    public void setChildrenUserIds(List<Long> childrenUserIds) {
        this.childrenUserIds = childrenUserIds;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
