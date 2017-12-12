/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto;

import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.basic.dto.CommonEnumDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 快捷收款相关枚举Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/11
 */
public class CustomerEnumsDto implements Serializable {

    private static final long serialVersionUID = 5697044967211519796L;
    @ApiModelProperty("身份认证审核状态枚举")
    private List<CommonEnumDto> auditStatusTypes;

    @ApiModelProperty("用户等级枚举")
    private List<CommonEnumDto> levelTypes;

    public List<CommonEnumDto> getLevelTypes() {
        return levelTypes;
    }

    public void setLevelTypes(List<CommonEnumDto> levelTypes) {
        this.levelTypes = levelTypes;
    }

    public List<CommonEnumDto> getAuditStatusTypes() {
        return auditStatusTypes;
    }

    public void setAuditStatusTypes(List<CommonEnumDto> auditStatusTypes) {
        this.auditStatusTypes = auditStatusTypes;
    }
}
