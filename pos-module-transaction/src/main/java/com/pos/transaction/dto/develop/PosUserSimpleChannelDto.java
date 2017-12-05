/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.develop;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 发展推客概要信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class PosUserSimpleChannelDto implements Serializable {

    private static final long serialVersionUID = -5310545990600536694L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("上线推客UserId")
    private Long parentUserId;

    @ApiModelProperty("推客用户ID")
    private Long channelUserId;

    @ApiModelProperty("推客电话")
    private String channelPhone;

    @ApiModelProperty("关联时间（Date，被发展为推客的时间）")
    private Date relationTime;

    @ApiModelProperty("上下级推客关系是否有效，当为false时表示解除上下级推客推客关系")
    private Boolean relationAvailable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public Long getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Long channelUserId) {
        this.channelUserId = channelUserId;
    }

    public Date getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(Date relationTime) {
        this.relationTime = relationTime;
    }

    public Boolean getRelationAvailable() {
        return relationAvailable;
    }

    public void setRelationAvailable(Boolean relationAvailable) {
        this.relationAvailable = relationAvailable;
    }

    public String getChannelPhone() {
        return channelPhone;
    }

    public void setChannelPhone(String channelPhone) {
        this.channelPhone = channelPhone;
    }
}
