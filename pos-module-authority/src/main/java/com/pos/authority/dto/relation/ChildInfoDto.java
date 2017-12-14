/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.relation;

import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.dto.statistics.DescendantStatisticsDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 直接下级信息
 *
 * @author wangbing
 * @version 1.0, 2017/12/14
 */
public class ChildInfoDto implements Serializable {

    private static final long serialVersionUID = -4516527418382711068L;

    @ApiModelProperty("下级用户id")
    private Long userId;

    @ApiModelProperty("下级用户姓名")
    private String name;

    @ApiModelProperty("下级用户电话")
    private String phone;

    @ApiModelProperty("下级用户等级")
    private Integer level;

    @ApiModelProperty("下级用户身份认证状态")
    private Integer auditStatus;

    @ApiModelProperty("下架用户身份认证状态描述")
    public String getAuditStatusDesc() {
        return auditStatus == null ? null : CustomerAuditStatus.getEnum(auditStatus).getDesc();
    }

    @ApiModelProperty("上级对下级的备注")
    private String remark;

    @ApiModelProperty("上下级关联关系建立时间")
    private Date relationTime;

    @ApiModelProperty("下级客户的统计（不包含分等级统计）")
    private DescendantStatisticsDto childStatistics;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(Date relationTime) {
        this.relationTime = relationTime;
    }

    public DescendantStatisticsDto getChildStatistics() {
        return childStatistics;
    }

    public void setChildStatistics(DescendantStatisticsDto childStatistics) {
        this.childStatistics = childStatistics;
    }
}
