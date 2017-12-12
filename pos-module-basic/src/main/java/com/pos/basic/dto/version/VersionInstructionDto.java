/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.version;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 版本更新说明信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
public class VersionInstructionDto implements Serializable {

    private static final long serialVersionUID = 4499608623026553391L;
    @ApiModelProperty("自增主键id（做更新操作时，必传此字段）")
    private Long id;

    @ApiModelProperty("版本号，如xx.xx.xx")
    private String version;

    @ApiModelProperty("版本介绍")
    private String instruction;

    @ApiModelProperty("是否启用")
    private Boolean available;

    @ApiModelProperty("更新操作人id")
    private Long updateUserId;

    @ApiModelProperty("更新操作时间")
    private Date updateTime;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
