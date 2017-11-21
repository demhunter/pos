/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 热词Dto
 *
 * @author wangbing
 * @version 1.0, 2017/01/06
 */
public class HotKeyDto implements Serializable{

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("热词名称")
    private String keyName;

    @ApiModelProperty("热词类型(int)（1 = 作品）")
    private Byte keyType;

    @ApiModelProperty("热词被搜索的总次数")
    private Long totalCount;

    @ApiModelProperty("热词状态(int)（0 = 不可用， 1 = 可用）")
    private Byte status;

    @ApiModelProperty("热词创建时间")
    private Date createTime;

    public HotKeyDto() {
    }

    public HotKeyDto(String keyName, Byte keyType) {
        this.keyName = keyName;
        this.keyType = keyType;
        this.totalCount = 1L;
        this.createTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Byte getKeyType() {
        return keyType;
    }

    public void setKeyType(Byte keyType) {
        this.keyType = keyType;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
