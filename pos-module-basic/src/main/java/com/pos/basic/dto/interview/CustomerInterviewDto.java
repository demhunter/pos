/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.interview;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户回访信息DTO
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
public class CustomerInterviewDto implements Serializable {

    @ApiModelProperty("自增主键id")
    private Long id;

    @ApiModelProperty("回访用户id")
    private Long userId;

    @ApiModelProperty("回访内容记录（新增时，此字段必填）")
    private String content;

    @ApiModelProperty("回访记录创建人")
    private Long createUserId;

    @ApiModelProperty("回访时间")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
