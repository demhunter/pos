/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.user;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * IM用户常用语DTO.
 *
 * @author wayne
 * @version 1.0, 2017/5/2
 */
@ApiModel
public class UserLanguageDto implements Serializable {

    @ApiModelProperty("编号")
    private Long id;

    @ApiModelProperty("用户的常用问题/回复")
    private String content;

    public UserLanguageDto() {
    }

    public UserLanguageDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}