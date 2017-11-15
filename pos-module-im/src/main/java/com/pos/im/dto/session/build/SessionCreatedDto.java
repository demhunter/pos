/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session.build;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.im.domain.Session;

import java.io.Serializable;

/**
 * 会话被创建的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/9/1
 */
@ApiModel
public class SessionCreatedDto implements Serializable {

    private static final long serialVersionUID = 154196243491914160L;

    @ApiModelProperty("会话ID")
    private Long id;

    @ApiModelProperty("对接IM平台的群组ID")
    private String groupId;

    @ApiModelProperty("会话名称")
    private String name;

    public SessionCreatedDto() {
    }

    public SessionCreatedDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SessionCreatedDto(Long id, String groupId, String name) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
    }

    public SessionCreatedDto(Session session) {
        this.id = session.getId();
        this.groupId = session.getGroupId();
        this.name = session.getName();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}