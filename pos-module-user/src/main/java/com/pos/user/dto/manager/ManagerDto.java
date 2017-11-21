/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.manager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.Copyable;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.constant.ManagerType;
import com.pos.user.constant.UserType;
import com.pos.user.dto.UserDto;
import com.pos.user.dto.merchant.MerchantDto;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 平台管理员DTO.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public class ManagerDto extends UserDto implements Serializable, Copyable<MerchantDto> {

    private static final long serialVersionUID = 6956691276501035611L;

    /**
     * 账号实体的UID（区分于用户ID），对象转换时需要该属性
     */
    @JsonIgnore
    private Long entityId;

    @ApiModelProperty("用户细分类型(int)：1 = 普通，2 = 客服，3 = 商务拓展")
    private byte userDetailType;

    @ApiModelProperty("头像")
    private String headImage;

    @ApiModelProperty("是否离职")
    private boolean quitJobs;

    @Override
    public MerchantDto copy() {
        MerchantDto newObj = new MerchantDto();
        BeanUtils.copyProperties(this, newObj);
        return newObj;
    }

    @Override
    public void check(String fieldPrefix) {
        super.check(fieldPrefix);
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(entityId, fieldPrefix + "entityId");
        if (parseUserDetailType() == null) {
            throw new ValidationException("'" + fieldPrefix + "userDetailType'无效值");
        }
    }

    @Override
    public String getShowName() {
        return getName();
    }

    @Override
    public String getUserType() {
        return Strings.isNullOrEmpty(super.getUserType()) ? UserType.MANAGER.getValue() : super.getUserType();
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    @ApiModelProperty("用户细分类型的值描述")
    public String getUserDetailTypeDesc() {
        ManagerType type = parseUserDetailType();
        return type != null ? type.getDesc() : "";
    }

    public ManagerType parseUserDetailType() {
        return ManagerType.getEnum(userDetailType);
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(byte userDetailType) {
        this.userDetailType = userDetailType;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public boolean isQuitJobs() {
        return quitJobs;
    }

    public void setQuitJobs(boolean quitJobs) {
        this.quitJobs = quitJobs;
    }

}