/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.develop;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 发展推客概要信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class SimpleTwitterDto implements Serializable {

    private static final long serialVersionUID = -5310545990600536694L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("上线推客UserId")
    private Long parentUserId;

    @ApiModelProperty("推客用户ID")
    private Long userId;

    @ApiModelProperty("推客电话")
    private String phone;

    @ApiModelProperty("关联时间（Date，被发展为推客的时间）")
    private Date createTime;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getRelationAvailable() {
        return relationAvailable;
    }

    public void setRelationAvailable(Boolean relationAvailable) {
        this.relationAvailable = relationAvailable;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
