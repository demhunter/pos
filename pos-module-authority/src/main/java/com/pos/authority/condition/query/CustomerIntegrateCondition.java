/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.condition.query;

import com.pos.common.util.date.SimpleDateUtils;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 聚合用户信息查询条件
 *
 * @author wangbing
 * @version 1.0, 2017/12/11
 */
public class CustomerIntegrateCondition {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("实名认证状态（0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过）")
    private Integer auditStatus;

    @ApiModelProperty("用户等级")
    private Integer level;

    @ApiModelProperty("用户账号启用/禁用状态")
    private Boolean enable;

    @ApiModelProperty("用户是否被回访过")
    private Boolean interviewed;

    @ApiModelProperty("快捷收款注册开始时间")
    private Date beginTime;

    @ApiModelProperty("快捷收款注册结束时间")
    private Date endTime;

    @ApiModelProperty("用户id列表类型：1 = 用户自身信息，2 = 用户的直接下级信息，默认为1")
    private Integer includeUserIdsType;

    @ApiModelProperty("包含在内的用户id列表")
    private List<Long> includeUserIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getInterviewed() {
        return interviewed;
    }

    public void setInterviewed(Boolean interviewed) {
        this.interviewed = interviewed;
    }

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

    public Integer getIncludeUserIdsType() {
        return includeUserIdsType;
    }

    public void setIncludeUserIdsType(Integer includeUserIdsType) {
        this.includeUserIdsType = includeUserIdsType;
    }

    public List<Long> getIncludeUserIds() {
        return includeUserIds;
    }

    public void setIncludeUserIds(List<Long> includeUserIds) {
        this.includeUserIds = includeUserIds;
    }
}
