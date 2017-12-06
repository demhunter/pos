/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.condition.query;

import com.pos.common.util.date.SimpleDateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 交易记录查询条件
 *
 * @author wangbing
 * @version 1.0, 2017/10/20
 */
public class PosTransactionCondition {

    private Long id; // 主键id

    private String recordNum; // 订单号

    private Long userId; // 提现用户userId

    private List<Long> includeUserIds; // 包含在内的体现呢用户userId列表

    private Integer status; // 交易状态

    private List<Integer> excludedStatuses; // 排除的交易状态

    private Date beginDate; // 创建时间-交易开始时间

    private Date endDate; // 创建时间-交易结束时间

    private Integer transactionType; // 交易类型

    /**
     * 解析开始截止日期（格式：yyyy-MM-dd）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     */
    public void parseBeginAndEndTime(String beginTime, String endTime) {
        if (StringUtils.isNotEmpty(beginTime)) {
            Date beginDate = SimpleDateUtils.parseDate(
                    beginTime, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            this.beginDate = SimpleDateUtils.getDateOfMidNight(beginDate);
        }
        if (StringUtils.isNotEmpty(endTime)) {
            Date endDate = SimpleDateUtils.parseDate(
                    endTime, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            this.endDate = SimpleDateUtils.getDateOfTodayEnd(endDate);
        }
    }

    public List<Integer> getExcludedStatuses() {
        return excludedStatuses;
    }

    public void setExcludedStatuses(List<Integer> excludedStatuses) {
        this.excludedStatuses = excludedStatuses;
    }

    public List<Long> getIncludeUserIds() {
        return includeUserIds;
    }

    public void setIncludeUserIds(List<Long> includeUserIds) {
        this.includeUserIds = includeUserIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }
}
